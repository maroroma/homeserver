package maroroma.homeserverng.tools.kodi.methods;

/**
 * Builder pour la méthode d'affichage de notification dans KODI
 */
public class ShowNotification extends AbstractKodiMethodBuilder<Integer, String, ShowNotification> {

    /**
     * Nom de la méthode pour les notifications.
     */
    private static final String KODI_METHOD_NAME_GUI_SHOW_NOTIFICATION = "GUI.ShowNotification";

    /**
     * Constructeur.
     */
    private ShowNotification() {
        super(KODI_METHOD_NAME_GUI_SHOW_NOTIFICATION, String.class);
    }

    /**
     * Retourne un builder pour les notitications.
     * @return -
     */
    public static ShowNotification create() {
        return new ShowNotification().id(1);
    }

    /**
     * Temps d'affichage de la notification.
     * @param displayTime -
     * @return -
     */
    public ShowNotification displayTime(final int displayTime) {
        return this.param("displaytime", displayTime);
    }

    /**
     * Message pour la notification.
     * @param message -
     * @return -
     */
    public ShowNotification message(final String message) {
        return this.param("message", message);
    }

    /**
     * Titre pour la notification?
     * @param title -
     * @return -
     */
    public ShowNotification title(final String title) {
        return this.param("title", title);
    }

    @Override
    protected void prepareDatasForBuild() {
        super.prepareDatasForBuild();
        // si le temps d'affichage est absent, on ajoute une valeur par défaut
        this.params.putIfAbsent("displaytime", 10000);
    }
}
