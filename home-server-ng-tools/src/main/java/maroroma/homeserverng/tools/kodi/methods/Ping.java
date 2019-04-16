package maroroma.homeserverng.tools.kodi.methods;

/**
 * Ping d'une instance kodi
 */
public class Ping extends AbstractKodiMethodBuilder<Integer, String, Ping> {

    /**
     * Nom de la méthode pour le ping.
     */
    private static final String KODI_METHOD_NAME_JSONRPC_PING = "JSONRPC.Ping";

    /**
     * Constructeur.
     */
    protected Ping() {
        super(KODI_METHOD_NAME_JSONRPC_PING, String.class);
    }

    public static Ping create() {
        return new Ping().id(1);
    }
}
