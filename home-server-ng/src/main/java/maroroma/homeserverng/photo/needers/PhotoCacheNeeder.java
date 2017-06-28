package maroroma.homeserverng.photo.needers;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.helpers.FluentList;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.needers.CacheNeed;
import maroroma.homeserverng.tools.needers.CacheNeeder;

/**
 * Implémentation de {@link CacheNeeder} permettant de retourner l'ensemble des caches utilisés par le composant Photo.
 * @author rlevexie
 *
 */
@Component
public class PhotoCacheNeeder implements CacheNeeder {

	/**
	 * {@value}.
	 */
	public static final String ALL_YEARS_CACHE = "PHOTO_ALL_YEARS_CACHE";
	
	/**
	 * {@value}.
	 */
	public static final String YEAR_CACHE = "PHOTO_YEAR_CACHE";
	
	/**
	 * {@value}.
	 */
	public static final String MONTH_CACHE = "PHOTO_MONTH_CACHE";
	
	/**
	 * {@value}.
	 */
	public static final String DAY_CACHE = "PHOTO_DAY_CACHE";
	
	/**
	 * {@value}.
	 */
	public static final String FULL_SIZE_CACHE = "PHOTO_FULL_SIZE_CACHE";
	
	/**
	 * {@value}.
	 */
	public static final String THUMB_CACHE = "PHOTO_THUMB_CACHE";

	/**
	 * retourn la liste des caches simples.
	 * @return -
	 */
	private List<String> getSimpleCacheNeeded() {
		return Arrays.asList(MONTH_CACHE, YEAR_CACHE, ALL_YEARS_CACHE, DAY_CACHE);
	}

	/**
	 * retourn la liste des caches à deux niveaux.
	 * @return -
	 */
	private List<Tuple<String, String>> getFileCacheNeeded() {
		return new FluentList<Tuple<String, String>>()
				.addAnd(Tuple.from(FULL_SIZE_CACHE, "homeserver.photo.cache.directory"));
	}

	@Override
	public CacheNeed getCacheNeeded() {
		return CacheNeed.builder()
				.simpleCaches(this.getSimpleCacheNeeded())
				.twoLevelFileCaches(this.getFileCacheNeeded())
				.fileCaches(new FluentList<Tuple<String, String>>()
				.addAnd(Tuple.from(THUMB_CACHE, "homeserver.photo.thumb.cache.directory"))).build();
	}


}
