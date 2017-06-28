package maroroma.homeserverng.tools.needers;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.helpers.Tuple;


/**
 * Décrit le besoin en cache pour {@link CacheNeeder}.
 * @author rlevexie
 *
 */
@Data
@Builder
public class CacheNeed {

	/**
	 * Liste des caches simples.
	 */
	private List<String> simpleCaches;
	
	/**
	 * Liste des caches fichiers à deux niveaux.
	 */
	private List<Tuple<String, String>> twoLevelFileCaches;
	
	/**
	 * Liste des caches fichiers simples.
	 */
	private List<Tuple<String, String>> fileCaches;
	
}
