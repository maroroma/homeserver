package maroroma.homeserverng.seedbox.services;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import maroroma.homeserverng.seedbox.model.EpisodeParseResult;
import maroroma.homeserverng.tools.directorymonitoring.DirectoryEvent;
import maroroma.homeserverng.tools.model.MoveRequest;
import maroroma.homeserverng.tools.model.MovedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.seedbox.tools.SeedboxModuleConstants;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.directorymonitoring.DirectoryMonitor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;

/**
 * Bean dédié à la surveillance en live du répertoire de téléchargements complétés.
 * <br /> Permet de lever des notifications lors de la détection de modification dans le répertoire.
 * <br /> Le but de base est de faire afficher sur le média center la fin d'un téléchargement.
 *
 * @author rlevexie
 */
@Component
@Log4j2
public class TodoDirectoryMonitor {


    /**
     * {@link DirectoryMonitor} pour lancer la surveillance continue du répertoire de todo.
     * On laisse une référence, de fait statique, pour gérer la mise à jour auto et l'arrêt relance.
     */
    private DirectoryMonitor todoDirectoryMonitor;

    /**
     * Permet d'émettre les notifications.
     */
    @Autowired
    private NotifyerContainer notifyer;

    /**
     * Permet de résoudre les épisodes de séries.
     */
    @Autowired
    private EpisodeParser episodeParser;

    /**
     * Permet de réaliser le déplacement automatique si possible
     */
    @Autowired
    private SeedBoxTodoService seedBoxTodoService;

    /**
     * Répertoire pour les fichiers à trier.
     */
    @Property(SeedboxModuleConstants.HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY)
    private HomeServerPropertyHolder todoDirectory;

    /**
     * à l'init du serveur, mise en place de la surveillance du répertoire todo.
     *
     * @throws HomeServerException -
     */
    @PostConstruct
    private void initWatchDirectory() throws HomeServerException {

        try {

            // création d'un monitor
            this.todoDirectoryMonitor = new DirectoryMonitor(this.todoDirectory.asFile())
                    // abonnement pour la création d'un fichier.
                    .watchCreation(this::handleFileCreation)
                    .start();

        } catch (Exception e) {
            log.warn("Une erreur est survenue lors de l'init de la surveillance. Celle-ci sera rétablie au prochain essai");
        }
    }

    /**
     * Arrêt du monitoring.
     *
     * @throws HomeServerException -
     */
    @PreDestroy
    private void stopWatchDirectory() throws HomeServerException {
        // si une init s'est mal passée, le monitor peut être null
        if (this.todoDirectoryMonitor != null) {
            this.todoDirectoryMonitor.stop();
        }
    }

    /**
     * sur une modification de l'emplacement du répertoire de todo, réinitialisation du monitoring du répertoire.
     *
     * @throws HomeServerException -
     */
    @PropertyRefreshHandlers(value = {SeedboxModuleConstants.HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY})
    private void restartMonitoring() throws HomeServerException {
        log.info("changement de répertoire surveillé !");
        this.stopWatchDirectory();
        this.initWatchDirectory();
    }


    /**
     * Gestion de la détection de la création d'un fichier.
     * @param event -
     */
    private void handleFileCreation(final DirectoryEvent event) {
        try {

            // préparation de l'event
            NotificationEvent.NotificationEventBuilder notificationEventBuilder = NotificationEvent.builder()
                    .creationDate(new Date())
                    .title("Téléchargement terminé")
                    .message("Le fichier \"" + event.getFile().getName() + "\" a fini d'être téléchargé.");

            // si le fichier ressemble à une série TV
            if (this.episodeParser.isTvShowEpisode(event.getFile())) {

                // on parse le fichier de l'épisode, avec toutes les informations permettant de le déplacer
                EpisodeParseResult episodeParseResult = this.episodeParser.parseFile(event.getFile());

                // si le parsing à réussi
                if (episodeParseResult.isSuccess()
                        // et que l'intégralité des déplacement est ok
                        && this.seedBoxTodoService.moveFiles(episodeParseResult.generateMoveRequest())
                        .stream()
                        .allMatch(MovedFile::isSuccess)) {

                    // modification des propriétés à émettre
                    notificationEventBuilder
                            .title("Téléchargement trié")
                            .message("Le fichier\"" + event.getFile().getName() + "\" a été déplacé et scanné.");
                }
            }

            // sur cet event, utilisation du notifier pour diffuser
            this.notifyer.notify(notificationEventBuilder.build());

        } catch (HomeServerException e) {
            log.warn("Une erreur est survenue lors de la notification de la création d'un répertoire", e);
        }
    }


}
