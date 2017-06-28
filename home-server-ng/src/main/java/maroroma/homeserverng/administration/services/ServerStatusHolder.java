package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.HomeServerRunningStatus;

/**
 * Interface de définition du status du server.
 * @author rlevexie
 *
 */
public interface ServerStatusHolder {

	/**
	 * Retourne le status actuel du server.
	 * @return -
	 */
	HomeServerRunningStatus getStatus();
	
	/**
	 * Met à jour le status actuel du server.
	 * @param status -
	 */
	void setStatus(HomeServerRunningStatus status);
	
}
