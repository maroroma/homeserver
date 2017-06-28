package maroroma.homeserverng.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la gestion des requêtes en erreur.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {

	/**
	 * {@link HttpStatus} associé.
	 */
	private HttpStatus httpStatus;
	
	/**
	 * Message de l'exception.
	 */
	private String message;
	
	/**
	 * Constructeur.
	 * @param status -
	 * @param e -
	 */
	public ErrorInfo(final HttpStatus status, final Exception e) {
		this.httpStatus = status;
		this.message = e.getMessage();
	}
	
}
