package maroroma.homeserverng.tools.transmission.methods;

import maroroma.homeserverng.tools.transmission.model.MeaningLessResponse;

/**
 * Requête de rajout de torrent à télécharger.
 */
public class AddTorrent extends AbstractTransmissionMethodBuilder<MeaningLessResponse, AddTorrent> {

    /**
     * Paramètre pour la mise en pause au démarrage.
     */
    public static final String PAUSED = "paused";

    /**
     * Constructeur.
     */
    protected AddTorrent() {
        super("torrent-add", "/rpc", MeaningLessResponse.class);
    }

    /**
     * Création du builder.
     * @return
     */
    public static AddTorrent create() {
        return new AddTorrent();
    }

    /**
     * Gestion du lien de téléchargement (maglink)
     * @param magnetLink -
     * @return -
     */
    public AddTorrent magnetLink(String magnetLink) {
        return this.param("filename", magnetLink);
    }

    /**
     * Détermine si le téléchargement doit démarré directement ou non.
     * @param paused -
     * @return -
     */
    public AddTorrent paused(boolean paused) {
        return this.param(PAUSED, paused);
    }

    /**
     * Emplacement de téléchargement
     * @param downloadDir -
     * @return -
     */
    public AddTorrent downloadDir(String downloadDir) {
        return this.param("download-dir", downloadDir);
    }

    @Override
    public TransmissionMethod<MeaningLessResponse> build() {
        this.paramIfAbsent(PAUSED, false);
        return super.build();
    }
}
