package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.kodi.methods.VideoLibraryScan;

import java.util.stream.Stream;


/**
 * Décrit une source kodi, ie un répertoire associé à un label.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KodiSource extends FromKodiInstance<KodiSource> {

    /**
     * Répertoire (smb ou local)
     */
    private String file;
    /**
     * Nom de la source.
     */
    private String label;

    public boolean matcheOneAlias(String... aliases) {
        return Stream.of(aliases).anyMatch(label::equalsIgnoreCase);
    }

    public VideoLibraryScan mapToLibraryScan() {
        return VideoLibraryScan.create().kodiSource(this).withDialog();
    }

}
