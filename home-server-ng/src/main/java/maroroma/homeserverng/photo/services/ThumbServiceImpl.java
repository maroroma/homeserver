package maroroma.homeserverng.photo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import maroroma.homeserverng.photo.needers.PhotoCacheNeeder;
import maroroma.homeserverng.photo.tools.ThumbFileNameKey;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;


/**
 * Implémentation de {@link ThumbService}.
 * @author rlevexie
 *
 */
@Service
public class ThumbServiceImpl implements ThumbService {

	/**
	 * Permet d'accéder au cache sous jacent pour la gestion des miniatures.
	 */
	@Autowired
	private CacheManager cacheManager;

	/**
	 * Emplacement de la sauvegarde des miniatures.
	 */
	@Property("homeserver.photo.thumb.cache.directory")
	private HomeServerPropertyHolder cacheDirectory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getThumb(final int... fields) throws HomeServerException { 
		
		
		ValueWrapper fromCache = getCache().get(new ThumbFileNameKey(fields));
		
		if (fromCache != null && fromCache.get() != null) {
			return (byte[]) fromCache.get();
		} else {
			return this.getDefaultThumb();
		}
		
	}

	/**
	 * retourne le cache associé au miniatures.
	 * @return -
	 */
	private Cache getCache() {
		return this.cacheManager.getCache(PhotoCacheNeeder.THUMB_CACHE);
	}
	
	/**
	 * Construit une miniature par défaut si non générée.
	 * @return -
	 * @throws HomeServerException -
	 */
	private byte[] getDefaultThumb() throws HomeServerException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putThumb(final byte[] thumb, final int year, final int month, final int day) throws HomeServerException {

		ThumbFileNameKey key = new ThumbFileNameKey(year, month, day);
		
		if (this.getCache().get(key) == null) {
			getCache().put(key, thumb);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getThumbForDay(final int year, final int month, final int day) throws HomeServerException {
		return this.getThumb(year, month, day);
	}

}
