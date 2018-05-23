package maroroma.homeserverng.tools.transmission.methods;

import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.transmission.model.BasicResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Client d'accès à un serveur transmission
 */
public class TransmissionClient {

    /**
     * Header pour le jeton d'identifiant de session transmission
     */
    public static final String X_TRANSMISSION_SESSION_ID = "X-Transmission-Session-Id";

    /**
     * Url du serveur transmission.
     */
    private String transmissionUrl;

    /**
     * Correlation Id à utilisé
     */
    private Optional<String> lastCorrelationID = Optional.empty();

    /**
     * Construction du client.
     * @param transmissionUrl -
     */
    public TransmissionClient(String transmissionUrl) {
        this.transmissionUrl = transmissionUrl;
    }

    /**
     * Execution de la requete, désérialisation de la réponse
     * @param method méthode transmission à émettre
     * @param <R> Type de la réponse désérialisée
     * @param <T> Type de la requête
     * @return -
     */
    public <R extends BasicResponse, T extends TransmissionMethod<R>> R execute(T method) {

        R returnValue;

        try {
            returnValue = this.innerExecute(method);
        } catch (HttpClientErrorException e) {
            // si on rencontre ce type d'erreurn on a potentiellement le header à récupérer et réémettre
            this.lastCorrelationID = Optional
                    .ofNullable(e.getResponseHeaders().getFirst(X_TRANSMISSION_SESSION_ID))
                    .map(s -> Optional.ofNullable(s))
                    // si pas de header dans la réponse en erreur, on se casse
                    .orElseThrow(() -> new RuntimeHomeServerException("Impossible de réaliser l'appel sans correlation ID"));

            // rappel avec header
            returnValue = this.innerExecute(method);
        }
        return returnValue;
    }

    /**
     * Execution de l'appel rest avec prise en compte des headers.
     * @param method méthode à executer
     * @param <R> type de la répose
     * @param <T> type de la requete
     * @return -
     */
    private <R extends BasicResponse, T extends TransmissionMethod<R>> R innerExecute(T method) {
        RestTemplate rt = new RestTemplate();
        R returnValue = rt.postForEntity(this.transmissionUrl + method.getTransmissionUri(),
                new HttpEntity<>(method, generateHeaders()),
                method.getResponseClazz()).getBody();

        if(!"success".equals(returnValue.getResult())) {
            throw new RuntimeHomeServerException("Erreur rencontrée lors de l'appel à tranmission");
        }

        return returnValue;
    }


    /**
     * Génération des headers en fonction de la présence du session ID.
     * @return
     */
    private HttpHeaders generateHeaders() {
        return this.lastCorrelationID
                .map(cid -> {
                    HttpHeaders returnValue = new HttpHeaders();
                    returnValue.add(X_TRANSMISSION_SESSION_ID, cid);
                    return returnValue;
                })
                .orElse(new HttpHeaders());
    }

}
