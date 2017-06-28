package maroroma.homeserverng.photo.services;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Permet de gérer les miniatures et leur rattachement à leur répertoires parents.
 * Prend la forme d'un cache de fichier hyper spécialisé.
 * @author rlevexie
 *
 */
public interface ThumbService {

	/**
	 * Retourne une miniature en fonction d'une liste de champ constituant une date.
	 * @param fields -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] getThumb(final int... fields) throws HomeServerException;
	
	/**
	 * Retourne une miniature pour un jour donné.
	 * @param year -
	 * @param month -
	 * @param day -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] getThumbForDay(final int year, final int month, final int day) throws HomeServerException;
	
	/**
	 * Rajoute une miniature dans le cache spécifique de miniature.
	 * @param thumb -
	 * @param year -
	 * @param month -
	 * @param day -
	 * @throws HomeServerException -
	 */
	void putThumb(byte[] thumb, int year, int month, int day) throws HomeServerException;
	
}
