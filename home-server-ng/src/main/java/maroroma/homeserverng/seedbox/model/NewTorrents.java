package maroroma.homeserverng.seedbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entrée pour le service d'ajout de torrent.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTorrents {
    /**
     * Magnet links pour l'upload.
     */
    private List<String> magnetLinks;
}
