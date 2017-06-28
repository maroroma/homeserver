package maroroma.homeserverng.tools.sse;

import java.util.concurrent.CountDownLatch;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Retourné lors de la création de {@link ManagedSseEmitter} par le {@link ManagedSseEmitterCollection}.
 * <br /> Mis en place pour permettre une attente active lors de la recréation de {@link SseEmitter} sous jacents.
 * <br /> PErmet de s'assurer d'attendre la recréation d'un {@link SseEmitter} suite à la destruction du {@link SseEmitter}
 *  prééxistant dans la collection.
 * <br /> evite les conflits sur le cycle de vie de ce type d'objet.
 * @author rlevexie
 *
 */
public class ManagedSseEmiterCreationHandler {

	/**
	 * Attente active.
	 */
	private CountDownLatch innerCdl;
	
	/**
	 * {@link ManagedSseEmitter} créé.
	 */
	private ManagedSseEmitter toBeCreated;
	
	/**
	 * Constructeur.
	 */
	public ManagedSseEmiterCreationHandler() {
		this.innerCdl = new CountDownLatch(1);
	}
	
	/**
	 * Fin de l'attente par affectation du {@link ManagedSseEmitter} effectivement créé.
	 * @param createdEmitter -
	 */
	public void complete(final ManagedSseEmitter createdEmitter) {
		this.toBeCreated = createdEmitter;
		this.innerCdl.countDown();
	}
	
	/**
	 * Attente de la création du {@link ManagedSseEmitter}.
	 * @return -
	 * @throws InterruptedException -
	 */
	public ManagedSseEmitter await() throws InterruptedException {
		this.innerCdl.await();
		return this.toBeCreated;
	}

}
