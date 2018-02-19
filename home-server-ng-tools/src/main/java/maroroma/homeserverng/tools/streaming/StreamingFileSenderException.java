package maroroma.homeserverng.tools.streaming;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Exception dédiée à la gestion du streaming.
 * @author rlevexie
 *
 */
public class StreamingFileSenderException extends HomeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1884711691221559413L;
	
	/**
	 * STatus http à retourné si l'erreur est rencontrée.
	 */
	@Getter
	private HttpStatus httpStatus;
	
	/**
	 * Headers spécifiques à placer dans le retour http en cas d'erreur.
	 */
	@Getter
	private Tuple<String, String> specificHeader;
	
	/**
	 * Constructeur.
	 */
	public StreamingFileSenderException() {
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
	public StreamingFileSenderException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 */
	public StreamingFileSenderException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructeur.
	 * @param message -
	 */
	public StreamingFileSenderException(final String message) {
		super(message);
	}

	/**
	 * Constructeur.
	 * @param cause -
	 */
	public StreamingFileSenderException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 * @param status -
	 * @param headers -
	 */
	public StreamingFileSenderException(final String message, final Throwable cause, final HttpStatus status,
			final Tuple<String, String> headers) {
		this(message, cause);
		this.httpStatus = status;
		this.specificHeader = headers;
	}
	
	/**
	 * Constructeur.
	 * @param message -
	 * @param status -
	 * @param headders -
	 */
	public StreamingFileSenderException(final String message, final HttpStatus status, final Tuple<String, String> headders) {
		this(message);
		this.httpStatus = status;
		this.specificHeader = headders;
	}
	
	/**
	 * Constructeur.
	 * @param message -
	 * @param cause -
	 * @param status -
	 */
	public StreamingFileSenderException(final String message, final Throwable cause, final HttpStatus status) {
		this(message, cause);
		this.httpStatus = status;
	}
	
	/**
	 * Constructeur.
	 * @param message (
	 * @param status (
	 */
	public StreamingFileSenderException(final String message, final HttpStatus status) {
		this(message);
		this.httpStatus = status;
	}

}
