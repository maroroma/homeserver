package maroroma.homeserverng.tools.sse;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Encaspulation d'un SSEEmitter pour la gestion de handlers multiples.
 * @author RLEVEXIE
 *
 */
@Slf4j
@ToString
public class ManagedSseEmitter {
	
	/**
	 * Détermine si le client est connecté. 
	 */
	@Getter
	private boolean connected;
	
	/**
	 * Identifiant du client.
	 */
	@Getter
	private String id;

	/**
	 * Permet de gérer les events en interne.
	 */
	@Getter
	private SseEmitter innerEmitter;
	
	/**
	 * Liste des handlers disponibles pour la completion.
	 */
	private List<ManagedSseEmitterEventHandler> onCompletionHandlers;
	
	/**
	 * Constructeur.
	 * @param identifier identifiant unique du handler.
	 */
	public ManagedSseEmitter(final String identifier) {
		
		// création du ssemitter natif
		this.innerEmitter = new SseEmitter();
		
		// mise en place de l'identifiant
		this.id = identifier;
		
		// liste des handlers
		this.onCompletionHandlers = new ArrayList<>();
		
		// js style
		final ManagedSseEmitter that = this;
		
		// rajout par du handler natif pour l'introduction des handlers multiples.
		this.innerEmitter.onCompletion(new Runnable() {
			@Override
			public void run() {
				log.info("ManagedSseEmitter::onCompletion - gestion de [" + onCompletionHandlers.size() + "] handlers pour l'emitter " + that);
				
				// sur complétion, appel de tous les handlers.
				for (ManagedSseEmitterEventHandler managedSseEmitterEventHandler : onCompletionHandlers) {
					managedSseEmitterEventHandler.onEvent("onCompletion", that);
				}
			}
		});
	}
	
	/**
	 * Rajout d'un event handler pour la completion.
	 * @param handler nouvel event handler.
	 * @return - this
	 */
	public ManagedSseEmitter onCompletion(final ManagedSseEmitterEventHandler handler) {
		Assert.notNull(handler, "handler must not be null");
		this.onCompletionHandlers.add(handler);
		return this;
	}
	
	/**
	 * Entraine la completion du {@link SseEmitter}, en plaçant un {@link ManagedSseEmitterEventHandler} spécifique.
	 * @param completeCallBack -
	 */
	public void complete(final ManagedSseEmitterEventHandler completeCallBack) {
		Assert.notNull(completeCallBack, "completeCallBack must not be null");
		this.onCompletion(completeCallBack).complete();
	}
	
	/**
	 * Entraine la completion du {@link SseEmitter}.
	 */
	public void complete() {
		this.innerEmitter.complete();
	}

	/**
	 * Emission d'un event à travers le {@link SseEmitter} natif.
	 * @param sseEventBuilder builder pour le message.
	 * @throws IOException -
	 */
	public void send(final SseEventBuilder sseEventBuilder) throws IOException {
		Assert.notNull(sseEventBuilder, "sseEventBuilder can't be null");
		this.innerEmitter.send(sseEventBuilder);		
	}
	
	
}
