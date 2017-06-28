package maroroma.homeserverng.tools.sse;

/**
 * Interface de définition d'un event handler pour la gestion des events depuis un {@link ManagedSseEmitter}.
 * @author RLEVEXIE
 *
 */
@FunctionalInterface
public interface ManagedSseEmitterEventHandler {
	/**
	 * Méthode appelée lorsqu'un event est déclenché par le {@link ManagedSseEmitter}.
	 * @param typeEvent type d'event
	 * @param emitter {@link ManagedSseEmitter} source.
	 */
	void onEvent(String typeEvent, ManagedSseEmitter emitter);
}
