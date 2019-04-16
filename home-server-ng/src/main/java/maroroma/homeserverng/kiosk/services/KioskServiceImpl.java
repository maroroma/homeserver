package maroroma.homeserverng.kiosk.services;

import maroroma.homeserverng.kiosk.model.CurrentReading;
import maroroma.homeserverng.kiosk.model.KioskDisplayOption;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du KioskService
 * @author rlevexie
 *
 */
@Service
public class KioskServiceImpl {

	/**
	 * displayWeather.
	 */
	@Property("homeserver.kiosk.displayWeather")
	private HomeServerPropertyHolder displayWeather;

	/**
	 * displayHour.
	 */
	@Property("homeserver.kiosk.displayHour")
	private HomeServerPropertyHolder displayHour;

	/**
	 * displayCurrentReading.
	 */
	@Property("homeserver.kiosk.displayCurrentReading")
	private HomeServerPropertyHolder displayCurrentReading;

	/**
	 * displayCurrentDownload.
	 */
	@Property("homeserver.kiosk.displayCurrentDownload")
	private HomeServerPropertyHolder displayCurrentDownload;

	/**
	 * Retourne les options d'affichage du kiosk.
	 * @return -
	 */
	public KioskDisplayOption getOptions() {

		// on retourne les options d'affichage à partir de l'ensemble des propriétés mappées sur le service.
		return KioskDisplayOption.builder()
				.displayCurrentDownload(this.displayCurrentDownload.asBoolean())
				.displayCurrentReading(this.displayCurrentReading.asBoolean())
				.displayHour(this.displayHour.asBoolean())
				.displayWeather(this.displayWeather.asBoolean())
				.build();
	}


	public List<CurrentReading> getCurrentReadings() {

		return null;
	}

}
