package maroroma.homeserverng.notifyer.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.kodimanager.services.KodiManagerService;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link Notifyer} permettant de notifier des systèmes kodi.
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
public class KodiNotifyer extends AbstractDisableableNotifyer implements Notifyer {

	/**
	 * Service centralisant les accès KODI
	 */
	@Autowired
	KodiManagerService kodiManagerService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doNotify(final NotificationEvent notification) throws HomeServerException {
		this.kodiManagerService.sendNotifications(notification.getMessage(), notification.getTitle());
	}

}
