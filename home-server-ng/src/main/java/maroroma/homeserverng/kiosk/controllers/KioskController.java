package maroroma.homeserverng.kiosk.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.kiosk.KioskModuleDescriptor;
import maroroma.homeserverng.kiosk.model.KioskDisplayOption;
import maroroma.homeserverng.kiosk.model.weather.AllForeCasts;
import maroroma.homeserverng.kiosk.services.KioskServiceImpl;
import maroroma.homeserverng.kiosk.services.WeatherService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Rest controller pour le module Kiosk.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = KioskModuleDescriptor.class)
@RequiredArgsConstructor
public class KioskController {
	
	/**
	 * SErvice sous jacent.
	 */
	private final KioskServiceImpl service;

	private final WeatherService weatherService;
	
	/**
	 * Retourne les options d'affichage du kiosk.
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/kiosk/options")
	public ResponseEntity<KioskDisplayOption> getOptions() {
		return ResponseEntity.ok(this.service.getOptions());
	}


	@GetMapping("${homeserver.api.path:}/kiosk/weather/allforecasts")
	public ResponseEntity<AllForeCasts> getAllForecasts() {
		return ResponseEntity.ok(this.weatherService.getAllForecasts());
	}
}
