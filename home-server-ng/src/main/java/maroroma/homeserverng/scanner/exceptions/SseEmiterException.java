package maroroma.homeserverng.scanner.exceptions;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Exception levée par le SSEEmitter.
 * @author rlevexie
 *
 */
public class SseEmiterException extends HomeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1821058816260728434L;

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 */
	public SseEmiterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	
	
}
