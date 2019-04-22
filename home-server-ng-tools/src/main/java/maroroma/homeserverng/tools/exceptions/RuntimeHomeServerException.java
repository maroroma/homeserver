package maroroma.homeserverng.tools.exceptions;

import java.util.Optional;

/**
 * Permet de transporter une {@link HomeServerException} en tant que {@link RuntimeException}.
 * <br /> mise en place pour pouvoir gérer les consumers des nouvelles opérations lambda de java 8.
 * <br /> Cela permet de lever une exception qui claque le foreach et ainsi être récupérée en sortie du traitement.
 * @author RLEVEXIE
 *
 */
public class RuntimeHomeServerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4308673215355546038L;
	
	/**
	 * Exception interne.
	 */
	private Optional<HomeServerException> innerException;

	/**
	 * Constructeur.
	 * @param exception -
	 */
	public RuntimeHomeServerException(final HomeServerException exception) {
		super(exception);
		this.innerException = Optional.of(exception);
	}

	public RuntimeHomeServerException(final String message) {
		super(message);
		this.innerException = Optional.empty();
	}
	
	/**
	 * Constructeur.
	 * @param exception -
	 */
	public RuntimeHomeServerException(final Throwable exception) {
		super(exception);
		this.innerException = Optional.empty();
	}

	public RuntimeHomeServerException(final Throwable exception, final String message) {
		super(message, exception);
		this.innerException = Optional.empty();
	}
	
	/**
	 * Retourne l'exception de type {@link HomeServerException} qui n'a pu être gérée de manière standard.
	 * @return -
	 */
	public HomeServerException getInnerHomeServerException() {
		return this.innerException.orElse(new HomeServerException("Erreur inconnue", this.getCause()));
	}


	
}
