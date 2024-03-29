package maroroma.homeserverng.tools.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.helpers.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	 * Message complexe (html)
	 */
	private String complexMessage;

	/**
	 * Date de création.
	 */
	private Date creationDate;

	/**
	 * Permet de réaliser des traitement supplémentaires (identification pour template, etc)
	 */
	private String eventType;

	/**
	 * Permet de rendre modulaire les events (passage de params, etc)
	 */
	private Map<String, Object> properties = new HashMap<>();

	/**
	 * Détermine si un message complexe est renseigné.
	 * @return -
	 */
	@JsonIgnore
	public boolean hasComplexMessage() {
		return StringUtils.hasLength(this.complexMessage);
	}
}
