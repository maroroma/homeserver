package maroroma.homeserverng.tools.kodi.methods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.kodi.model.KodiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant une méthode remote json rpc de kodi.
 * Ce sont les builders qui alimentent le contenu de cette classe, qui sera la seule transmise lors des requêtes vers kodi.
 * @param <T> type de l'identifiant de la méthode
 * @param <U> type de retour de la méthode
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"responseClazz"})
public class KodiMethod<T, U> {

    /**
     * Identifiant de la requête.
     */
    private T id;

    /**
     * Version du jsonrpc utilisée.
     */
    protected String jsonrpc;
    /**
     * Nom de la méthode.
     */
    protected String method;

    /**
     * Liste des paramètres.
     */
    protected Map<String, Object> params = new HashMap<>();

    /**
     * {@link Class} utilisée pour la désérialisation.
     */
    private Class<U> responseClazz;

    /**
     * Execution de la méthode à travers un {@link KodiClient}
     * @param kodiClient executor de la requete
     * @return type attendu en réponse
     */
    public KodiResponse<U> execute(KodiClient kodiClient) {
        return kodiClient.execute(this);
    }

}
