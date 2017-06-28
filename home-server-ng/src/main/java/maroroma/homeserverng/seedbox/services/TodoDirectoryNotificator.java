package maroroma.homeserverng.seedbox.services;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
 * @author rlevexie
 *
 */
@Component
@Log4j2
public class TodoDirectoryNotificator {
	
	
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
	 * Répertoire pour les fichiers à trier.
	 */
	@Property(SeedboxModuleConstants.HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY)
	private HomeServerPropertyHolder todoDirectory;
	
	/**
	 * à l'init du serveur, mise en place de la surveillance du répertoire todo.
	 * @throws HomeServerException -
	 */
	@PostConstruct
	private void initWatchDirectory() throws HomeServerException {

		try {
		
			// création d'un monitor
			this.todoDirectoryMonitor = new DirectoryMonitor(this.todoDirectory.asFile())
					// abonnement pour la création d'un fichier.
					.watchCreation(event -> {
						try {
							// sur cet event, utilisation du notifier pour diffuser
							this.notifyer.notify(
									NotificationEvent.builder()
									.creationDate(new Date())
									.title("Téléchargement terminé")
									.message("Le fichier \"" + event.getFile().getName() + "\" a fini d'être téléchargé.")
									.build());
						} catch (HomeServerException e) {
							log.warn("Une erreur est survenue lors de la notification de la création d'un répertoire", e);
						}
					})
					.start();
		
		} catch (Exception e) {
			log.warn("Une erreur est survenue lors de l'init de la surveillance. Celle-ci sera rétablie au prochain essai");
		}
	}

	/**
	 * Arrêt du monitoring.
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
	 * @throws HomeServerException -
	 */
	@PropertyRefreshHandlers(value = {SeedboxModuleConstants.HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY})
	private void restartMonitoring() throws HomeServerException {
		log.info("changement de répertoire surveillé !");
		this.stopWatchDirectory();
		this.initWatchDirectory();
	}
}
