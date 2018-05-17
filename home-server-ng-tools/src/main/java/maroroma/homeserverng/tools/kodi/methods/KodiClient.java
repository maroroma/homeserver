package maroroma.homeserverng.tools.kodi.methods;

import org.springframework.web.client.RestTemplate;

/**
 * Interface de définition d'un executor des requêtes KODI
 */
public class KodiClient {

    /**
     * Url du kodi.
     */
    private String kodiUrl;

    /**
     * Constructeur.
     * @param kodiUrl -
     */
    public KodiClient(final String kodiUrl) {
        this.kodiUrl = kodiUrl;
    }

    /**
     * Execution d'une {@link KodiMethod}
     * @param kodiMethod méthode à executer
     * @param <I> type de l'identifiant de la méthode
     * @param <R> type de la réponse
     * @param <T> type de la méthode
     * @return
     */
    public <I, R, T extends KodiMethod<I, R>> R execute(T kodiMethod) {
        RestTemplate rt = new RestTemplate();
        return rt.postForObject(this.kodiUrl, kodiMethod, kodiMethod.getResponseClazz());
    }
}
