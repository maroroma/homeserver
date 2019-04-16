package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.kodi.methods.KodiClient;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<KodiSource> extractKodiSources(final KodiClient kodiClient) {
        return this.getResult().getSources().stream()
                .map(oneSource -> oneSource.withKodiClient(kodiClient))
                .collect(Collectors.toList());
    }
}
