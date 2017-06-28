package maroroma.homeserverng.tools.cache;

import java.util.List;

/**
 * Décrit les opérations pour un cache manipulable.
 * @author rlevexie
 *
 */
public interface CacheAdvancedOperator {
	/**
	 * Taille du cache.
	 * @return -
	 */
	int size();
	/**
	 * Nom du cache.
	 * @return -
	 */
	String getName();
	/**
	 * Liste des clefs du cache.
	 * @return -
	 */
	List<CacheKeyDescriptor> getKeys();
	
	/**
	 * Construit le {@link CacheKeyDescriptor} correspondant à ce cache.
	 * @return -
	 */
	CacheDescriptor buildCacheDescriptor();
	
	/**
	 * Nettoyage du cache.
	 */
	void clear();
}
