package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Décrit une source kodi, ie un répertoire associé à un label.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KodiSource {

    /**
     * Répertoire (smb ou local)
     */
    private String file;
    /**
     * Nom de la source.
     */
    private String label;

}
