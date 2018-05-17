package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contient les {@link KodiSource}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSourcesResult {
    /**
     * Liste des sources.
     */
    private List<KodiSource> sources;
}
