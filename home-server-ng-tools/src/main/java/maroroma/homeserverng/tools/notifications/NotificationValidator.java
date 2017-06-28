package maroroma.homeserverng.tools.notifications;

import org.springframework.util.Assert;

/**
 * Classe utilitaire pour la validation d'events.
 * @author RLEVEXIE
 *
 */
public abstract class NotificationValidator {

	/**
	 * Validation d'un {@link NotificationEvent}.
	 * @param notification -
	 */
	public static void validate(final NotificationEvent notification) {
		Assert.notNull(notification, "notification can't be null");
		Assert.hasLength(notification.getTitle(), "notification.title can't be null or empty");
		Assert.hasLength(notification.getMessage(), "notification.message can't be null or empty");
		Assert.notNull(notification.getCreationDate(), "notification.creationDate can't be null");
	}
	
}
