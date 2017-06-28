package maroroma.homeserverng.administration.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import maroroma.homeserverng.tools.cache.AbstractCacheAdvancedOperatorWrapper;
import maroroma.homeserverng.tools.cache.CacheAdvancedOperator;
import maroroma.homeserverng.tools.cache.CacheDescriptor;
import maroroma.homeserverng.tools.cache.CacheKeyDescriptor;
import maroroma.homeserverng.tools.cache.ConcurrentMapCacheCacheAdvancedOperatorWrapper;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;

/**
 * Implémentation de {@link CacheService}.
 * @author rlevexie
 *
 */
@Service
public class CacheServiceImpl implements CacheService {

	/**
	 * CAche manager natif.
	 */
	@Autowired
	private CacheManager cacheManager;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CacheDescriptor> getCaches() throws HomeServerException {
		return cacheManager.getCacheNames().stream()
		.map(cacheName -> resolveCacheAdvancedDescriptor(cacheName).buildCacheDescriptor())
		.collect(Collectors.toList());
	}
	
	@Override
	public List<CacheKeyDescriptor> getCacheKeys(final String cacheName) throws HomeServerException {
		Assert.hasLength(cacheName, "cacheName can't be null or empty");
		return this.resolveCacheAdvancedDescriptor(cacheName).getKeys();
	}
	
	@Override
	public List<CacheKeyDescriptor> clearCache(final String cacheName) throws HomeServerException {
		Assert.hasLength(cacheName, "cacheName can't be null or empty");
		Assert.notNull(this.cacheManager.getCache(cacheName), "Le cache " + cacheName + " n'existe pas");
		this.resolveCacheAdvancedDescriptor(cacheName).clear();
		return this.getCacheKeys(cacheName);
	}
	
	/**
	 * Permet de récupérer le {@link CacheAdvancedOperator} depuis le cache manager pour un nom de cache donné.
	 * @param cacheName -
	 * @return -
	 */
	private CacheAdvancedOperator resolveCacheAdvancedDescriptor(final String cacheName) {
		Assert.hasLength(cacheName, "cacheName can't be null or empty");
		Assert.notNull(this.cacheManager.getCache(cacheName), "Le cache " + cacheName + " n'existe pas");
		return this.resolveCacheAdvancedDescriptor(this.cacheManager.getCache(cacheName));
	}
	
	/**
	 * Retourne le {@link CacheAdvancedOperator} correspondant au cache en entrée.
	 * @param cache -
	 * @return -
	 */
	private CacheAdvancedOperator resolveCacheAdvancedDescriptor(final Cache cache) {
		
		// si instance déjà du bon type
		if (cache instanceof CacheAdvancedOperator) {
			return ((CacheAdvancedOperator) cache);
		} 
		
		// si concurrentMapCache
		if (ConcurrentMapCacheCacheAdvancedOperatorWrapper.isWrappable(cache)) {
			return new ConcurrentMapCacheCacheAdvancedOperatorWrapper(cache);
		}

		// sinon valeur par défaut, implémentation bidon.
		return new AbstractCacheAdvancedOperatorWrapper<Cache>(cache) {

			@Override
			public int size() {
				return -1;
			}

			@Override
			protected Cache validateAndCast(final Cache cache) {
				return cache;
			}

			@Override
			public List<CacheKeyDescriptor> getKeys() {
				return Collections.emptyList();
			}

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}
		};
	}

	
	
}
