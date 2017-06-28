package maroroma.homeserverng.photo.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import maroroma.homeserverng.photo.needers.PhotoCacheNeeder;

/**
 * Implémente {@link PhotoCacheEvicter} par simple utilisation des annotation spring {@link CacheEvict}.
 * @author rlevexie
 *
 */
@Service
public class PhotoCacheEvicterImpl implements PhotoCacheEvicter {

	@CacheEvict(cacheNames = PhotoCacheNeeder.ALL_YEARS_CACHE)
	@Override
	public void evictYears() {

	}

	@CacheEvict(cacheNames = PhotoCacheNeeder.MONTH_CACHE)
	@Override
	public void evictDaysForMonth(final Integer year, final Integer monthToScan) {
		// TODO Auto-generated method stub

	}

	@CacheEvict(cacheNames = PhotoCacheNeeder.DAY_CACHE)
	@Override
	public void evictPhotosForDay(final Integer year, final Integer month, final Integer day) {
		// TODO Auto-generated method stub

	}

	@CacheEvict(cacheNames = PhotoCacheNeeder.YEAR_CACHE)
	@Override
	public void evictMonths(final Integer year) {
		// TODO Auto-generated method stub

	}

}
