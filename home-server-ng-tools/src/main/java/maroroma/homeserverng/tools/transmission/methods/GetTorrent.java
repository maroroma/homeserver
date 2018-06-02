package maroroma.homeserverng.tools.transmission.methods;

import maroroma.homeserverng.tools.transmission.model.GetTorrentResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * Builder pour les requêtes de récupération des torrents en cours.
 */
public class GetTorrent extends AbstractTransmissionMethodBuilder<GetTorrentResponse, GetTorrent> {

    /**
     * Liste des champs à récupérer sur le torrent en cours.
     */
    private List<String> fields = new ArrayList<>();

    /**
     * Constructeur.
     */
    protected GetTorrent() {
        super("torrent-get", "/rpc", GetTorrentResponse.class);
    }


    /**
     * Création du builder.
     * @return -
     */
    public static GetTorrent create() {
        return new GetTorrent().addField("id")
                .addField("name")
                .addField("totalSize")
                .addField("isFinished")
                .addField("leftUntilDone")
                .addField("percentDone")
                .addField("sizeWhenDone");
    }

    /**
     * Méthode pour l'ajout de propriété à récupérer sur le torrent.
     * @param fieldName -
     * @return -
     */
    public GetTorrent addField(String fieldName) {
        this.fields.add(fieldName);
        return this.paramIfAbsent("fields", this.fields);
    }


}
