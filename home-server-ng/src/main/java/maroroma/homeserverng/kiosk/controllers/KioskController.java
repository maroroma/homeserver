package maroroma.homeserverng.kiosk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import maroroma.homeserverng.kiosk.KioskModuleDescriptor;
import maroroma.homeserverng.kiosk.model.KioskDisplayOption;
import maroroma.homeserverng.kiosk.services.KioskService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;


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
	private KioskService service;
	
	
	/**
	 * Retourne les options d'affichage du kiosk.
	 * @return -
	 */
	@GetMapping("/kiosk/options")
	public ResponseEntity<KioskDisplayOption> getOptions() {
		return ResponseEntity.ok(this.service.getOptions());
	}

}
