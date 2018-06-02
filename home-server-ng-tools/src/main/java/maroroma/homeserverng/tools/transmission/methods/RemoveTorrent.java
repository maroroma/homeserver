package maroroma.homeserverng.tools.transmission.methods;

import maroroma.homeserverng.tools.transmission.model.MeaningLessResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * {"method":"torrent-remove","arguments":{"ids":[72]}}
 */
public class RemoveTorrent extends AbstractTransmissionMethodBuilder<MeaningLessResponse, RemoveTorrent> {

    /**
     * Liste des identifiants pour la suppression
     */
    List<Integer> idsToRemove = new ArrayList<>();

    /**
     * Constructeur.
     */
    protected RemoveTorrent() {
        super("torrent-remove", MeaningLessResponse.class);
    }

    /**
     * Création de la méthode de suppression des torrents
     * @return
     */
    public static RemoveTorrent create() {
        return new RemoveTorrent();
    }

    /**
     * Ajout d'un identifiant de torrent à supprimer.
     * @param idToRemove identifiant du torrent à supprimer
     * @return -
     */
    public RemoveTorrent remove(int idToRemove) {
        this.idsToRemove.add(idToRemove);
        return this.paramIfAbsent("ids", this.idsToRemove);
    }

    /**
     * Ajout d'une liste d'identifiant de torrents à supprimer.
     * @param idsToRemove liste des ids à remover
     * @return -
     */
    public RemoveTorrent remove(List<Integer> idsToRemove) {
        this.idsToRemove.addAll(idsToRemove);
        return this.paramIfAbsent("ids", this.idsToRemove);
    }

}
