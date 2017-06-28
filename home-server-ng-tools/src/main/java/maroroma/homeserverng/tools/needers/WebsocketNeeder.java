package maroroma.homeserverng.tools.needers;

/**
 * Interface de définition pour définir le besoin de mise en place d'un nouveau channel sur le websocket.
 * @author rlevexie
 *
 */
public interface WebsocketNeeder {

	/**
	 * Retourne le nom du topic à exposer.
	 * @return -
	 */
	String getTopic();
	
	/**
	 * Retourne le nom du endpoint à exposer.
	 * @return -
	 */
	String getStompEndPoint();
	
}
