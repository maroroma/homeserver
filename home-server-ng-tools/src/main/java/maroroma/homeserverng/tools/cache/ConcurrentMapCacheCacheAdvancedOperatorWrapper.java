package maroroma.homeserverng.tools.cache;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.util.Assert;

import maroroma.homeserverng.tools.helpers.FluentList;


/**
 * Implémentation de {@link AbstractCacheAdvancedOperatorWrapper} spécialisée pour les {@link ConcurrentMapCache} de spring.
 * @author rlevexie
 *
 */
public class ConcurrentMapCacheCacheAdvancedOperatorWrapper 
	extends AbstractCacheAdvancedOperatorWrapper<ConcurrentMapCache> 
	implements CacheAdvancedOperator {

	/**
	 * Constructeur.
	 * @param cache -
	 */
	public ConcurrentMapCacheCacheAdvancedOperatorWrapper(final Cache cache) {
		super(cache);
	}

	/**
	 * Détermine si le {@link Cache} passé en paramètre est gérable par ce {@link ConcurrentMapCacheCacheAdvancedOperatorWrapper}.
	 * @param cache -
	 * @return -
	 */
	public static boolean isWrappable(final Cache cache) {
		return (cache instanceof ConcurrentMapCache);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ConcurrentMapCache validateAndCast(final Cache cache) {
		Assert.isInstanceOf(ConcurrentMapCache.class, cache, "le cache doit être une instance de ConcurrentMapCache");
		return (ConcurrentMapCache) cache;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CacheKeyDescriptor> getKeys() {
		return this.getWrapped().getNativeCache().keySet().stream()
				// conversion des clef en cacheKeyDescriptor.
				.map(key -> CacheKeyDescriptor.builder().value(getSimpleKeyValue(key)).type(CacheKeyDescriptorType.OPEN_SIMPLE_KEY).build())
				.collect(Collectors.toList());
	}

	/**
	 * Extraction des clefs pour leur exposution.
	 * @param rawKey -
	 * @return -
	 */
	private List<String> getSimpleKeyValue(final Object rawKey) {
		if (rawKey instanceof OpenSimpleStringsKey) {
			return ((OpenSimpleStringsKey) rawKey).asList();
		} else {
			return FluentList.<String>create().addAnd(rawKey.toString());
		}

	}
}
