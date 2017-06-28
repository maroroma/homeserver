package maroroma.homeserverng.photo.model;

import maroroma.homeserverng.photo.services.PhotoScanService;

/**
 * Permet de lister l'ensemble des résolutions retournable par le service {@link PhotoScanService}.
 * @author RLEVEXIE
 *
 */
public enum PhotoResolution {
	/**
	 * Taille pleine de la photo.
	 */
	FULL,
	/**
	 * Taille intermédaire de la photo.
	 */
	MEDIUM,
	/**
	 * Taille miniature de la photo.
	 */
	THUMB
}
