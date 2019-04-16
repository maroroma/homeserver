package maroroma.homeserverng.tools.kodi.methods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import maroroma.homeserverng.tools.kodi.model.KodiResponse;
import org.springframework.web.client.RestTemplate;

/**
 * Interface de définition d'un executor des requêtes KODI
 */
@NoArgsConstructor
@AllArgsConstructor
public class KodiClient {

    /**
     * Url du kodi.
     */
    @Getter
    @Setter
    private String kodiUrl;

    /**
     * Execution d'une {@link KodiMethod}
     * @param kodiMethod méthode à executer
     * @param <I> type de l'identifiant de la méthode
     * @param <R> type de la réponse
     * @param <T> type de la méthode
     * @return
     */
    public <I, R, T extends KodiMethod<I, R>> KodiResponse<R> execute(T kodiMethod) {
        RestTemplate rt = new RestTemplate();
        R response = rt.postForObject(this.kodiUrl, kodiMethod, kodiMethod.getResponseClazz());
        return KodiResponse.<R>fromInstance(this).withResponse(response);
    }

}
