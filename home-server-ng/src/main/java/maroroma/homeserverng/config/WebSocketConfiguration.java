package maroroma.homeserverng.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.needers.WebsocketNeeder;

/**
 * Permet de configurer la websocket.
 * @author RLEVEXIE
 *
 */
@Configuration
@EnableWebSocketMessageBroker
@Log4j2
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * Listing des besoins en websockets.
	 */
	@Autowired(required = false)
	private List<WebsocketNeeder> websocketNeeders = new ArrayList<>();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configureMessageBroker(final MessageBrokerRegistry registry) {
		// definition des topic qui sera accessible via le websocket

		StringBuilder trace = new StringBuilder("[homeserver - websockets] - topic demandés : ");

		List<String> topicList = new ArrayList<>();
		for (WebsocketNeeder needer : this.websocketNeeders) {
			topicList.add(needer.getTopic());
			trace.append(needer.getTopic()).append(";");
		}

		//		registry.enableSimpleBroker("/seedbox/topic", "/scanner/topic");


		registry.enableSimpleBroker(topicList.toArray(new String[topicList.size()]));
		registry.setApplicationDestinationPrefixes("/app");
		
		
		log.info(trace);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStompEndpoints(final StompEndpointRegistry arg0) {

		StringBuilder trace = new StringBuilder("[homeserver - websockets] - endpoint demandés : ");

		// init d'un endpoitn pour la partie sockJS et son init
		for (WebsocketNeeder websocketNeeder : websocketNeeders) {
			arg0.addEndpoint(websocketNeeder.getStompEndPoint()).withSockJS();	
			trace.append(websocketNeeder.getStompEndPoint()).append(";");
		}

		log.info(trace);

		//		arg0.addEndpoint("/seedbox/broker").withSockJS();
		//		arg0.addEndpoint("/scanner/broker").withSockJS();

	}

}
