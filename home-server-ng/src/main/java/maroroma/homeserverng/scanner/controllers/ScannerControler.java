package maroroma.homeserverng.scanner.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.scanner.ScannerModuleDescriptor;
import maroroma.homeserverng.scanner.model.ScanRequest;
import maroroma.homeserverng.scanner.model.ScannerColorMode;
import maroroma.homeserverng.scanner.services.ScannerEventEmitter;
import maroroma.homeserverng.scanner.services.ScannerService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Controller pour la gestion des scans.
 * @author RLEVEXIE
 *
 */
@HomeServerRestController(moduleDescriptor = ScannerModuleDescriptor.class)
public class ScannerControler {

	
	/**
	 * Service pour la gestion du scanner.
	 */
	@Autowired
	private ScannerService scannerService;
	
	/**
	 * Permet de remonter des events aux clients abonnés.
	 */
	@Autowired
	private ScannerEventEmitter scannerEventEmitter;
	
	/**
	 * Listing des scans déjà présents.
	 * @return -
	 */
	@RequestMapping("/scanner/scans")
	public ResponseEntity<List<FileDescriptor>> listDoneScans() {
		return ResponseEntity.ok(this.scannerService.listDoneScans());
	}
	
	/**
	 * Suppression d'un scan.
	 * @param fileName identifiant du fichier à supprimer
	 * @return -
	 */
	@RequestMapping(value = "/scanner/scan/{id}", method = {RequestMethod.DELETE})
	public ResponseEntity<List<FileDescriptor>> deleteScan(@PathVariable("id") final String fileName) {
		this.scannerService.deleteScan(fileName);
		return ResponseEntity.ok(this.scannerService.listDoneScans());
	}
	
	
	/**
	 * demande de scan.
	 * @param scanRequest -
	 * @return -
	 * @throws HomeServerException  -
	 */
	@RequestMapping(value = "/scanner/scan", method = { RequestMethod.POST })
	public ResponseEntity<Boolean> scan(@RequestBody final ScanRequest scanRequest) throws HomeServerException {
		this.scannerService.validateScanRequest(scanRequest);
		this.scannerService.scanWithOptions(scanRequest);
		return ResponseEntity.ok(true);
	}
	
	/**
	 * Permet de s'abonner aux events émis lors d'un scan.
	 * @param id identifiant du nouvel abonné.
	 * @return {@link SseEmitter}
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/scanner/events/{id}")
	public SseEmitter subscribeScannerEvents(@PathVariable("id") final String id) throws HomeServerException {
		return this.scannerEventEmitter.createEmitter(id);
	}
	
	/**
	 * Retourne la liste des options pour les couleurs lors du scan.
	 * @return -
	 */
	@RequestMapping("/scanner/options/colormodes")
	public ResponseEntity<List<ScannerColorMode>> listColorModes() {
		return ResponseEntity.ok(this.scannerService.listColorModes());
	}
	
		
}
