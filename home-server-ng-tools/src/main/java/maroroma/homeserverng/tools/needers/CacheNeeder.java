package maroroma.homeserverng.tools.needers;

/**
 * Interface de définition pour définir un besoin en cache.
 * @author rlevexie
 *
 */
public interface CacheNeeder {

	/**
	 * Retourne les noms des caches requis demandé au homeserver.
	 * @return -
	 */
	CacheNeed getCacheNeeded();
	
}
