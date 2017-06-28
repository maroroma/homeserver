package maroroma.homeserverng.tools.notifications;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Implémentation de {@link Notifyer} permettant d'aggréget un ensemble de {@link Notifyer}.
 * @author RLEVEXIE
 *
 */
@Component
@Log4j2
public class NotifyerContainer implements Notifyer {

	
	/**
	 * Liste de {@link Notifyer}.
	 */
	@Autowired(required = false)
	private List<Notifyer> notifiers;
	
	/**
	 * {@inheritDoc}
	 * <br /> Dans cette implémentation, permet d'émettre la notification vers l'ensemble des
	 * {@link Notifyer} trouvés dans le contexte.
	 */
	@Override
	public void notify(final NotificationEvent notification) throws HomeServerException {
		
		// validation de la notification
		NotificationValidator.validate(notification);
		
		// pour chacun des notifiers, si présents
		if (notifiers != null && !notifiers.isEmpty()) {
			notifiers.forEach(oneNotifyer -> {
				try {
					oneNotifyer.notify(notification);
				} catch (HomeServerException e) {
					log.warn("Erreur rencontrée lors de l'émission d'une notification", e);
				}
			});
		}
	}

}
