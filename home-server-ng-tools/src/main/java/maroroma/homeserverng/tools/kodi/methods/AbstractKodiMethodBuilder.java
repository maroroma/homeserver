package maroroma.homeserverng.tools.kodi.methods;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite pour la constructin de {@link KodiMethod}
 * @param <I> type de l'identifiant
 * @param <R> type de retour de la méthode
 * @param <T> type de builder
 */
public abstract class AbstractKodiMethodBuilder<I, R, T extends AbstractKodiMethodBuilder<I, R, T>> {

    /**
     * Représentation de la version 2 de json rpc.
     */
    public static final String JSON_RPC_V_2_0 = "2.0";

    /**
     * Nom de la méthode.
     */
    protected String methodId;

    /**
     * RPC version.
     */
    protected String jsonRpcVersion;

    /**
     * Identifiant.
     */
    protected I id;

    /**
     * Type de la réponse.
     */
    protected Class<R> responseClazz;

    /**
     * Liste des paramètres
     */
    protected Map<String, Object> params = new HashMap<>();

    /**
     * Constructeur.
     * @param method nom de la méthode
     */
    protected AbstractKodiMethodBuilder(final String method, final Class<R> clazz) {
        this.responseClazz = clazz;
        this.methodId = method;
    }

    /**
     * Placement de l'identifiant sous forme de string
     * @param id -
     * @return -
     */
    public T id(final I id) {
        this.id = id;
        return returnThis();
    }

    /**
     * Génération de la {@link KodiMethod}
     * @return -
     */
    public KodiMethod<I, R> build() {

        this.prepareDatasForBuild();

        return this.createKodiMethod();
    }

    /**
     * Création de la {@link KodiMethod}
     * @return -
     */
    protected KodiMethod<I, R> createKodiMethod() {
        return new KodiMethod<>(this.id, this.jsonRpcVersion, this.methodId, this.params, this.responseClazz);
    }

    /**
     * Prépération des données.
     */
    protected void prepareDatasForBuild() {
        this.resolveJsonRpcVersion();
    }

    /**
     * Si la version est non renseignée, elle bascule automatiquement à JSON_RPC_V_2_0
     */
    protected void resolveJsonRpcVersion() {
        if(this.jsonRpcVersion == null || this.jsonRpcVersion.isEmpty()) {
            this.jsonRpcVersion = JSON_RPC_V_2_0;
        }
    }


    /**
     * Ajout d'un paramètre pour la méthode
     * @param id identifiant du paramètre
     * @param value valeur du paramètre
     * @return -
     */
    public T param(String id, Object value) {
        this.params.put(id, value);
        return returnThis();
    }

    /**
     * Retourn le this, casté dans le type qui va bien.
     * @return -
     */
    protected T returnThis() {
        return (T) this;
    }

}
