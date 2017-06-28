package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Définition du service permettant de rédémarrer le server.
 * @author rlevexie
 *
 */
public interface RestarterService {

	/**
	 * Programme un redémarrage sur le temps par défaut.
	 * @throws HomeServerException -
	 */
	void scheduleStop() throws HomeServerException;

	/**
	 * Programme un redémarrage sur un temps donné.
	 * @param millisBeforeRestart - 
	 * @throws HomeServerException -
	 */
	void scheduleStop(int millisBeforeRestart) throws HomeServerException;

}