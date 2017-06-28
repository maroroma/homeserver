package maroroma.homeserverng.administration.services;

import java.util.List;

import maroroma.homeserverng.tools.cache.CacheDescriptor;
import maroroma.homeserverng.tools.cache.CacheKeyDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Définition du service de manipulation des caches.
 * @author rlevexie
 *
 */
public interface CacheService {
	/**
	 * Retourne l'ensemble des caches portés par l'application.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<CacheDescriptor> getCaches() throws HomeServerException;

	/**
	 * Retourne l'ensemble des clefs pour un cache donné.
	 * @param cacheName -
	 * @return -
	 * @throws HomeServerException -
	 */
	List<CacheKeyDescriptor> getCacheKeys(String cacheName) throws HomeServerException;
	
	/**
	 * Supprime l'ensemble des clef du cache donné.
	 * @param cacheName no du cache
	 * @return -
	 * @throws HomeServerException -
	 */
	List<CacheKeyDescriptor> clearCache(String cacheName) throws HomeServerException;
}
