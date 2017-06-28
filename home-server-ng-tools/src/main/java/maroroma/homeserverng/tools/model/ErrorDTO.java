package maroroma.homeserverng.tools.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Descriptor pour une erreur.
 * @author rlevexie
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {

	/**
	 * Code de l'erreur.
	 */
	private String errorCode;
	
	/**
	 * Message pour l'erreur.
	 */
	private String errorMessage;
	
}
