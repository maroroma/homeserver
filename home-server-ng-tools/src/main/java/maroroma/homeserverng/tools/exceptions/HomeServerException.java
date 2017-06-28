package maroroma.homeserverng.tools.exceptions;


/**
 * Exception custom HomeServer.
 * @author rlevexie
 *
 */
public class HomeServerException extends Exception {

	/**
	 * -.
	 */
	private static final long serialVersionUID = -8624722917824855246L;

	/**
	 * Constructeur.
	 */
	public HomeServerException() {
		super();
	}

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 * @param enableSuppression -
	 * @param writableStackTrace -
	 */
	public HomeServerException(final String message, final  Throwable cause, final  boolean enableSuppression, final  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 */
	public HomeServerException(final String message, final  Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructeur.
	 * @param message -
	 */
	public HomeServerException(final String message) {
		super(message);
	}

	/**
	 * Constructeur.
	 * @param cause -
	 */
	public HomeServerException(final Throwable cause) {
		super(cause);
	}

}
