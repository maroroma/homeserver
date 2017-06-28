package maroroma.homeserverng.photo.model;

/**
 * PErmet de réaliser une distinction entre les différentes implémentation de {@link AbstractPhotoDirectoryDescriptor}.
 * @author RLEVEXIE
 *
 */
public enum PhotoDirectoryType {
	/**
	 * Container pour une année, portant des mois.
	 */
	YEAR,
	/**
	 * Container pour un mois, portant des jours.
	 */
	MONTH,
	/**
	 * Container pour un jour, contenant des photos.
	 */
	DAY
}
