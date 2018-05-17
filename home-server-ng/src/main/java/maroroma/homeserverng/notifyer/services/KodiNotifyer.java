package maroroma.homeserverng.notifyer.services;

import maroroma.homeserverng.tools.kodi.methods.KodiClient;
import maroroma.homeserverng.tools.kodi.methods.KodiMethod;
import maroroma.homeserverng.tools.kodi.methods.ShowNotification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.kodi.requests.AbstractKodiJsonRPCMethod;
import maroroma.homeserverng.tools.kodi.requests.KodiMethods;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;

/**
 * {@link Notifyer} permettant de notifier des systèmes kodi.
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
public class KodiNotifyer extends AbstractDisableableNotifyer implements Notifyer {

	/**
	 * urls kodi exploitables.
	 */
	@Property("homeserver.notifyer.kodi.urls")
	private HomeServerPropertyHolder kodiUrls;

	/**
	 * Temps d'affichage de la notification.
	 */
	@Property("homeserver.notifyer.kodi.url.displaytime")
	private HomeServerPropertyHolder displayTime;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doNotify(final NotificationEvent notification) throws HomeServerException {

		KodiMethod<?, ?> method = ShowNotification.create()
				.displayTime(this.displayTime.asInt())
				.message(notification.getMessage())
				.title(notification.getTitle())
				.build();

		this.kodiUrls.asStringList().parallelStream()
				.map(KodiClient::new)
				.forEach(method::execute);

	}

}
