package maroroma.homeserverng.seedbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Contenu des torrents à supprimer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TorrentsToDelete {

    private List<Integer> idsToDelete;

}
