package maroroma.homeserverng.kiosk.controllers;

import maroroma.homeserverng.kiosk.KioskModuleDescriptor;
import maroroma.homeserverng.kiosk.model.KioskDisplayOption;
import maroroma.homeserverng.kiosk.services.KioskServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Rest controller pour le module Kiosk.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = KioskModuleDescriptor.class)
public class KioskController {
	
	/**
	 * SErvice sous jacent.
	 */
	@Autowired
	private KioskServiceImpl service;
	
	
	/**
	 * Retourne les options d'affichage du kiosk.
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/kiosk/options")
	public ResponseEntity<KioskDisplayOption> getOptions() {
		return ResponseEntity.ok(this.service.getOptions());
	}

}
