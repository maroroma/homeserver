package maroroma.homeserverng.tools.notifications;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * Event général de notification.
 * @author RLEVEXIE
 *
 */
@Data
@Builder
public class NotificationEvent {
	/**
	 * Titre de l'event.
	 */
	private String title;
	/**
	 * Message de l'event.
	 */
	private String message;
	/**
	 * Date de création.
	 */
	private Date creationDate;
}
