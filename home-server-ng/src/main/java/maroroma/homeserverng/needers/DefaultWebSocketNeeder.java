package maroroma.homeserverng.needers;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.needers.WebsocketNeeder;

/**
 * Définition d'un websocket par défaut.
 * @author rlevexie
 *
 */
@Component
public class DefaultWebSocketNeeder implements WebsocketNeeder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTopic() {
		return "/homeserver/topic";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStompEndPoint() {
		return "/homeserver/broker";
	}

}
