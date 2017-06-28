package maroroma.homeserverng.scanner.needers;

import maroroma.homeserverng.tools.needers.WebsocketNeeder;

/**
 * Définition des besoins en websocket pour le scanner.
 * Deprecated
 * Est remplacé par les SSE.
 * @author rlevexie
 *
 */
//@Component
@Deprecated
public class ScannerWebSocketNeeder implements WebsocketNeeder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTopic() {
		return "/scanner/topic";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStompEndPoint() {
		return "/scanner/broker";
	}

}
