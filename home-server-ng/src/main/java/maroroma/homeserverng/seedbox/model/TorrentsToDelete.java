package maroroma.homeserverng.seedbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contenu des torrents à supprimer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TorrentsToDelete {

    private List<Integer> idsToDelete;

}
