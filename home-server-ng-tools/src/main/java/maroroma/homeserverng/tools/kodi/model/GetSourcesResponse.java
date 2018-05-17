package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Réponse du listing des resources kodi.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSourcesResponse {
    /**
     * R2sult.
     */
    private GetSourcesResult result;
}
