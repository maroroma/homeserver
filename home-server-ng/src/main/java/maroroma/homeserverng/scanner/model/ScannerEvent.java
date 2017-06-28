package maroroma.homeserverng.scanner.model;

import lombok.Builder;
import lombok.Data;

/**
 * Dto pour l'émission des events liés à un scan en cours.
 * @author RLEVEXIE
 *
 */
@Data
@Builder
public class ScannerEvent {

	/**
	 * Message.
	 */
	private String message;
	
	/**
	 * Identifiant du message.
	 */
	private String id;
	
	/**
	 * Type de l'event.
	 */
	private ScannerEventType eventType;
	
}
