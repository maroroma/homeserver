package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.kodi.methods.GetSources;
import maroroma.homeserverng.tools.kodi.methods.KodiClient;
import maroroma.homeserverng.tools.kodi.methods.VideoLibraryScan;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTargetDirectoryLoader implements TargetDirectoryLoader {

    /**
     * urls kodi exploitables.
     */
    @Property("homeserver.notifyer.kodi.urls")
    private HomeServerPropertyHolder kodiUrls;

    @Override
    public abstract String getKodiAlias();

    @Override
    public void executeScanOnKodiInstances() {
        kodiUrls.asStringList()
                // pour chaque url kodi
                .stream()
                // création du client qui va bien
                .map(KodiClient::new)
                // association client kodi avec les scan à appeler
                .map(this::getClientWithLibraryToScan)
                // on ne garde que les clients avec des scans à appeler
                .filter(clientWithSources -> !clientWithSources.getItem2().isEmpty())
                // execution de chaque scan pour chaque client
                .forEach(clientWithSources -> clientWithSources.getItem2()
                            .forEach(method -> method.build().execute(clientWithSources.getItem1()))
                );
    }

    /**
     * Création association entre client kodi et la liste des scans lançable par kodi
     * @param kodiClient
     * @return
     */
    public Tuple<KodiClient, List<VideoLibraryScan>> getClientWithLibraryToScan(KodiClient kodiClient) {
        return Tuple.from(kodiClient,
                // création de la méthode de récupération des sources du kodi pointé par le client
                GetSources.create()
                        .build()
                        .execute(kodiClient)
                        .getResult()
                        // récupération des sources de la réponse
                        .getSources()
                        .stream()
                        // on ne garde que les sources correspondant à l'alias de la source
                        .filter(kodiSource -> kodiSource.getLabel().equalsIgnoreCase(this.getKodiAlias()))
                        // création de la méthode d'appel de scan de librairie
                        .map(kodiSource -> VideoLibraryScan.create().withDialog().kodiSource(kodiSource))
                        .collect(Collectors.toList()));
    }
}
