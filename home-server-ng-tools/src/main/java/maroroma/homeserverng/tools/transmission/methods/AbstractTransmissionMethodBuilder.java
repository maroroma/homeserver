package maroroma.homeserverng.tools.transmission.methods;

import maroroma.homeserverng.tools.transmission.model.BasicResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTransmissionMethodBuilder<R extends BasicResponse, T extends AbstractTransmissionMethodBuilder<R, T>> {

    /**
     * Nom de la méthode.
     */
    protected String methodId;

    /**
     * Type de la réponse.
     */
    protected Class<R> responseClazz;

    /**
     * Liste des paramètres
     */
    protected Map<String, Object> params = new HashMap<>();

    /**
     * Uri pour compléter l'appel.
     */
    protected String transmissionUri;

    /**
     * Constructeur.
     * @param method nom de la méthode
     */
    protected AbstractTransmissionMethodBuilder(final String method, String transmissionUri, final Class<R> clazz) {
        this.responseClazz = clazz;
        this.methodId = method;
        this.transmissionUri = transmissionUri;
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
     * Ajoute un paramètre si absent.
     * @param id -
     * @param value -
     * @return -
     */
    public T paramIfAbsent(String id, Object value) {
        this.params.putIfAbsent(id, value);
        return returnThis();
    }


    /**
     * Retourn le this, casté dans le type qui va bien.
     * @return -
     */
    protected T returnThis() {
        return (T) this;
    }

    public TransmissionMethod<R> build() {
        return new TransmissionMethod<>(this.methodId, this.params, this.responseClazz, this.transmissionUri);
    }
}
