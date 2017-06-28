package maroroma.homeserverng.tools.notifications;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Interace de définition d'un notificateur.
 * @author rlevexie
 *
 */
public interface Notifyer {

	/**
	 * Demande l'émission d'une {@link NotificationEvent}.
	 * @param notification -
	 * @throws HomeServerException -
	 */
	void notify(NotificationEvent notification) throws HomeServerException;
	
}
