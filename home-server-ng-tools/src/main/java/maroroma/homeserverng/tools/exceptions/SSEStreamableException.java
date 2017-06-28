package maroroma.homeserverng.tools.exceptions;

import maroroma.homeserverng.tools.sse.SSEStreamable;

/**
 * Exception levée lors de l'execution d'une méthode d'un {@link SSEStreamable}.
 * @author rlevexie
 *
 */
public class SSEStreamableException extends HomeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3018799602289575696L;

	/**
	 * Constructeur.
	 */
	public SSEStreamableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 * @param enableSuppression -
	 * @param writableStackTrace -
	 */
	public SSEStreamableException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 */
	public SSEStreamableException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructeur.
	 * @param message -
	 */
	public SSEStreamableException(final String message) {
		super(message);
	}

	/**
	 * Constructeur.
	 * @param cause -
	 */
	public SSEStreamableException(final Throwable cause) {
		super(cause);
	}
	
	

}
