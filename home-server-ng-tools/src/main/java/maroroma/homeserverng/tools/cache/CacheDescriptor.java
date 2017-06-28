package maroroma.homeserverng.tools.cache;

import lombok.Builder;
import lombok.Data;

/**
 * Permet retourner la decription d'un cache.
 * @author rlevexie
 *
 */
@Data
@Builder
public class CacheDescriptor {

	/**
	 * Nom du cache.
	 */
	private String name;
	
	/**
	 * Nombre d'éléments du cache.
	 */
	private int nbElements;
	
}
