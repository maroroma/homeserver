package maroroma.homeserverng.tools.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Permet de gérer un sous ensemble de {@link Cache}.
 * @param <T> type de cache à gérer
 * @author rlevexie
 *
 */
public class FileCacheManager<T extends Cache> implements CacheManager {

	/**
	 * Liste de caches.
	 */
	private Map<String, T> cachesMap;
	
	/**
	 * Construit un cache manager intégrant une liste de {@link Cache} construite à partir de {@link Tuple} de nom de cahc
	 * et de {@link HomeServerPropertyHolder} correspondant au répertoire de cache de fichier.
	 * @param cacheNames -
	 * @param cacheCreator méthode permettant de générer le cache.
	 */
	public FileCacheManager(final List<Tuple<String, HomeServerPropertyHolder>> cacheNames, 
			final Function<Tuple<String, HomeServerPropertyHolder>, T> cacheCreator) {
		this.initCaches(cacheNames, cacheCreator);
	}
	
	/**
	 * Initialisation de la liste de cache.
	 * @param cacheNames -
	 * @param cacheCreator méthode permettant de générer le cache.
	 */
	private void initCaches(final List<Tuple<String, HomeServerPropertyHolder>> cacheNames,
			final Function<Tuple<String, HomeServerPropertyHolder>, T> cacheCreator) {
		this.cachesMap = new ConcurrentHashMap<>();
		cacheNames.forEach(tuple -> cachesMap.put(tuple.getItem1(), cacheCreator.apply(tuple)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cache getCache(final String name) {
		if (this.cachesMap.containsKey(name)) {
			return this.cachesMap.get(name);
		} else {
			return null;
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getCacheNames() {
		return this.cachesMap.keySet();
	}

}
