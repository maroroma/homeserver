package maroroma.homeserverng.kodimanager.services;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.kodi.methods.*;
import maroroma.homeserverng.tools.kodi.model.KodiResponse;
import maroroma.homeserverng.tools.kodi.model.KodiSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Service de centralisation des accès et des manipulation kodi
 */
@Service
public class KodiManagerService {

    /**
     * Liste des urls kodi
     */
    @Property("homeserver.kodimanager.instances.urls")
    HomeServerPropertyHolder kodiInstancesUrls;


    /**
     * Temps d'affichage de la notification sur les kodi.
     */
    @Property("homeserver.kodimanager.notification.displaytime")
    private HomeServerPropertyHolder displayTime;

    /**
     * Emission des notitifications vers l'ensemble des kodis
     * @param message message à émettre
     * @param title titre du message
     * @return
     */
    public long sendNotifications(final String message, final String title) {
        return this.execute(ShowNotification.create()
                .displayTime(this.displayTime.asInt())
                .message(message)
                .title(title))
            .count();
    }


    /**
     * Liste l'état des différentes instances kodi connues
     * @return -
     */
    public List<KodiResponse<Boolean>> pingInstances() {
        return this.execute(Ping.create())
        .map(wrappedResponse -> {
            if(wrappedResponse.hasError()) {
                return wrappedResponse.map(v -> false);
            } else {
                return wrappedResponse.map(value -> value.contains("pong"));
            }
        }).collect(Collectors.toList());
    }

    /**
     * Retourne la liste des différentes sources rattachées au différentes instances de kodi
     * @return -
     */
    public List<KodiResponse<List<KodiSource>>> getKodiSources() {
        return this.execute(GetSources.create())
                // on ne garde que les réponses en succès
                .filter(KodiResponse::hasNoError)
                // on map sur la liste de sources pour la sortie
                .map(oneResponse -> oneResponse
                        .map(oneValue -> oneValue.extractKodiSources(oneResponse.getKodiClient()))
                )
                .collect(Collectors.toList());
    }

    /**
     * Lance un scan pour la liste d'alias donnés.
     * Le scan est multi instance
     * @param aliases -
     */
    @Async
    public void scanAliases(String... aliases) {
        this.getKodiSources().stream()
                .flatMap(sourcesForClient -> sourcesForClient.getResponse().stream())
                .filter(oneSource -> oneSource.matcheOneAlias(aliases))
                .forEach(oneSource ->
                    oneSource.mapToLibraryScan().build().execute(oneSource.getKodiClient())
                );
    }

    /**
     * Execute une méthode kodi et place la réponse dans une {@link KodiResponse}
     * @param builder builder de la méthode à executer
     * @param <T> type de la réponse
     * @return la un stream de réponse (une répoonse par instance kodi)
     */
    private <T> Stream<KodiResponse<T>> execute(AbstractKodiMethodBuilder<?,T,?> builder) {
        return this.getClients()
                .map(oneClient -> {
                     try {
                         return builder.build().execute(oneClient);
                     } catch(Exception e) {
                         return KodiResponse.<T>fromInstance(oneClient).withError();
                     }
                });
    }

    /**
     * Produit un stream de l'ensemble des clients kodi disponibles.
     * @return -
     */
    private Stream<KodiClient> getClients() {
        return kodiInstancesUrls.asStringList()
                .parallelStream()
                .map(KodiClient::new);
    }



}
