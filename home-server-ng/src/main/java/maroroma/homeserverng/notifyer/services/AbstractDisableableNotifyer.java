package maroroma.homeserverng.notifyer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import maroroma.homeserverng.notifyer.NotifyerModuleDescriptor;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;

/**
 * {@link Notifyer} de base permettant de controller si les notifications sont activées
 * par rapport au module de notification.
 * @author rlevexie
 *
 */
public abstract class AbstractDisableableNotifyer implements Notifyer {

	/**
	 * {@link HomeServerModuleDescriptor} dédié à la notification.
	 */
	@Autowired
	private NotifyerModuleDescriptor moduleDescriptor;
	
	/**
	 * Détermine si le module de notification est activé.
	 * @return -
	 */
	protected boolean isNotificationEnabled() {
		return this.moduleDescriptor.isModuleEnabled();
	}

	/**
	 * {@inheritDoc}
	 * <br /> La notification n'est déclenchée que si le module de notification est activé.
	 * <br /> La notification est asynchrone.
	 */
	@Async
	@Override
	public void notify(final NotificationEvent notification) throws HomeServerException {
		if (this.isNotificationEnabled()) {
			this.doNotify(notification);
		}
	}

	/**
	 * Implémentation de la notification.
	 * @param notification -
	 * @throws HomeServerException -
	 */
	protected abstract void doNotify(NotificationEvent notification) throws HomeServerException;
	
	
}
