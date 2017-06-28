package maroroma.homeserverng.photo.services;

/**
 * Service permettant de forcer la mise à jour des caches photos.
 * @author rlevexie
 *
 */
public interface PhotoCacheEvicter {
	
	/**
	 * Nettoye le cache des années.
	 */
	void evictYears();
	
	/**
	 * Nettoye le cache des jours du mois.
	 * @param year -
	 * @param monthToScan -
	 */
	void evictDaysForMonth(Integer year, Integer monthToScan);
	
	/**
	 * Nettoye le cache des photos du jour.
	 * @param year -
	 * @param month -
	 * @param day -
	 */
	void evictPhotosForDay(Integer year, Integer month, Integer day);

	/**
	 * Nettoye le cache des mois de l'année.
	 * @param year -
	 */
	void evictMonths(Integer year);
}
