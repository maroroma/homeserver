package maroroma.homeserverng.tools.kodi.methods;

import maroroma.homeserverng.tools.kodi.model.KodiSource;

/**
 * Méthode pour lancer le scan d'une librairie vidéo donnée.
 */
public class VideoLibraryScan extends AbstractKodiMethodBuilder<Integer, String, VideoLibraryScan> {

    private static final String SHOWDIALOGS = "showdialogs";

    /**
     * Constructeur.
     */
    private VideoLibraryScan() {
        super("VideoLibrary.Scan", String.class);
    }

    /**
     * Création de la méthode.
     * @return -
     */
    public static VideoLibraryScan create() {
        return new VideoLibraryScan().id(1);
    }

    /**
     * Mise en place de la source initiale à scanner.
     * @param kodiSource -
     * @return -
     */
    public VideoLibraryScan kodiSource(KodiSource kodiSource) {
        return this.param("directory", kodiSource.getFile());
    }

    /**
     * SCan sans afficher de popup
     * @return -
     */
    public VideoLibraryScan withoutDialog() {
        return this.param(SHOWDIALOGS, false);
    }

    /**
     * Scan avec popup
     * @return -
     */
    public VideoLibraryScan withDialog() {
        return this.param(SHOWDIALOGS, true);
    }

    @Override
    protected void prepareDatasForBuild() {
        super.prepareDatasForBuild();
        this.params.putIfAbsent(SHOWDIALOGS, true);
    }
}
