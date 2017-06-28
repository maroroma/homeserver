package maroroma.homeserverng.tools.sse;

import maroroma.homeserverng.tools.exceptions.SSEStreamableException;

/**
 * Interface de définition d'un composant portant une méthode utilisée
 * pour un appel récurent dont le résultat est à diffusé à travers des sse.
 * @author rlevexie
 *
 */
@FunctionalInterface
public interface SSEStreamable {

	/**
	 * Méthode appelée dont le résultat est utilisé pour le streaming.
	 * @return le résultat du traitement à streamer
	 * @throws SSEStreamableException -
	 */
	Object process() throws SSEStreamableException;
	
}
