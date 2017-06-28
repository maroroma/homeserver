package maroroma.homeserverng.watch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.webcam.Webcam;
import maroroma.homeserverng.watch.WatchModuleDescriptor;
import maroroma.homeserverng.watch.services.WatchService;

/**
 * Controller pour la gestion des webcams.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = WatchModuleDescriptor.class)
public class WatchController {

	/**
	 * Service sous jacent.
	 */
	@Autowired
	private WatchService service;
	
	/**
	 * Récupération de toute les webcams.
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/watch/webcams")
	ResponseEntity<List<Webcam>> getAllWebcams() throws HomeServerException {
		return ResponseEntity.ok(service.getAllWebcams());
	}
	
	/**
	 * Enregistrement d'une webcam.
	 * @param newWebCam -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/watch/webcam", method = {RequestMethod.POST })
	public ResponseEntity<List<Webcam>> savewebcam(@RequestBody final Webcam newWebCam) throws HomeServerException {
		return ResponseEntity.ok(this.service.saveWebCam(newWebCam));
	}
	
	/**
	 * Mise à jour d'une webcam.
	 * @param id identifiant de la webcam à mettre à jour
	 * @param newWebCam nouvelle webcam
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/watch/webcam/{id}", method = {RequestMethod.PATCH })
	public ResponseEntity<List<Webcam>> updateWebcam(@PathVariable("id") final String id,
			@RequestBody final Webcam newWebCam) throws HomeServerException {
		return ResponseEntity.ok(this.service.updateWebcam(newWebCam));
	}
	
	@RequestMapping(value = "/watch/webcam/{id}", method = {RequestMethod.DELETE })
	public ResponseEntity<List<Webcam>> deleteWebcam(@PathVariable("id") final String id) throws HomeServerException {
		return ResponseEntity.ok(this.service.deleteWebCam(id));
	}
	
	@RequestMapping(value = "/watch/webcam/{id}/capture", method = {RequestMethod.POST }, produces = {MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getCapture(@PathVariable("id") final String id) throws HomeServerException {
		return ResponseEntity.ok(this.service.getCaptureFromWebcam(id));
	}
	
	@RequestMapping("/watch/availablewebcams")
	public ResponseEntity<List<Webcam>> getAllAvailableWebcams() throws HomeServerException {
		return ResponseEntity.ok(this.service.getAllAvailableWebcams());
	}
	
	@RequestMapping("/watch/webcam/{idWebcam}/subscribe/{idSubscriber}")
	public SseEmitter subscribeToWebCam(@PathVariable("idSubscriber") String idSubscriber, @PathVariable("idWebcam") String idWebcam) throws HomeServerException {
		return this.service.subscribeToStream(idSubscriber, idWebcam);
	}
	
	@RequestMapping(value = "/watch/webcam/{idWebcam}/subscribe/{idSubscriber}", method = {RequestMethod.DELETE })
	public ResponseEntity<Boolean> unsubscribeToWebCam(@PathVariable("idSubscriber") String idSubscriber, @PathVariable("idWebcam") String idWebcam) throws HomeServerException {
		return ResponseEntity.ok(this.service.unsubscribeToStream(idSubscriber, idWebcam));
	}
	
	
}
