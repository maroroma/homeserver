package maroroma.homeserverng.tools.transmission.methods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.transmission.model.BasicResponse;

import java.util.Map;

/**
 * Requête émise vers transmission, on mode json rpc.
 * @param <R> type de la réponse en sortie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"responseClazz", "transmissionUri"})
public class TransmissionMethod<R extends BasicResponse> {

    /**
     * Nom de la méthode.
     */
    private String method;

    /**
     * Liste des arguements.
     */
    private Map<String, Object> arguments;

    /**
     * Type de réponse pour la désérialisation.
     */
    private Class<R> responseClazz;

    /**
     * Uri pour compléter les appels.
     */
    private String transmissionUri;

    /**
     * Exécution de la méthode.
     * @param client client transmission
     * @return -
     */
    public R execute(TransmissionClient client) {
        return client.execute(this);
    }
}
