package maroroma.homeserverng.notifyer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.FluentMap;
import maroroma.homeserverng.tools.jsonrpc.KodiJsonRPCMethod;
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
	 * Nom de la méthode proposée par kodi pour faire de la notification.
	 */
	private static final String KODI_METHOD_NAME_GUI_SHOW_NOTIFICATION = "GUI.ShowNotification";

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

		// on valide que la liste d'url est bien présente

		// construction de la méthode json rpc.
		KodiJsonRPCMethod method = KodiJsonRPCMethod.builder()
				.id(1)
				.jsonrpc(KodiJsonRPCMethod.JSON_RPC_V_2_0)
				.method(KODI_METHOD_NAME_GUI_SHOW_NOTIFICATION)
				.id(1)
				.params(
						FluentMap.<String, Object>create()
						.add("title", notification.getTitle())
						.add("message", notification.getMessage())
						.add("displaytime", this.displayTime.asInt()))
				.build();

		// pour chacun des urls, émission asynchrone sur l'url donnée
		this.kodiUrls.asStringList().parallelStream().forEach(oneUrl -> {
			log.warn("notification vers " + oneUrl);
			RestTemplate rt = new RestTemplate();
			rt.postForObject(oneUrl, method, String.class);
		});

	}

}
