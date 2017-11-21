package maroroma.homeserverng.reducer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.reducer.ReducerModuleDescriptor;
import maroroma.homeserverng.reducer.model.ReducedImageInput;
import maroroma.homeserverng.reducer.model.SendMailRequest;
import maroroma.homeserverng.reducer.services.ReducerService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.mail.ContactDescriptor;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Rest controller pour la sauvegard des miniatures sur le serveur.
 * @author RLEVEXIE
 *
 */
@HomeServerRestController(moduleDescriptor = ReducerModuleDescriptor.class)
public class ReducerController {

	/**
	 * Service sous jacent.
	 */
	@Autowired
	private ReducerService service;
	
	/**
	 * Sauvegarde d'une image réduite.
	 * @param base64ReducedImage -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/reducer/reducedImage", method = { RequestMethod.POST })
	public void uploadReducedImage(@RequestBody final ReducedImageInput base64ReducedImage) throws HomeServerException {
			this.service.uploadReducedImage(base64ReducedImage);
	}
	
	/**
	 * Lance la réduction d'une image sur le serveur.
	 * @param imageToReduce -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/reducer/fullSizeImage", method = { RequestMethod.POST })
	public void uploadImageToReduce(@RequestBody final MultipartFile imageToReduce) throws HomeServerException {
		this.service.reduceImage(imageToReduce);
	}
	
	/**
	 * Emission de mails avec une list d'image réduites.
	 * @param mailRequest -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/reducer/mail", method = { RequestMethod.POST })
	public void uploadReducedImage(@RequestBody final SendMailRequest mailRequest) throws HomeServerException {
			this.service.sendMail(mailRequest);
	}
	
	/**
	 * Permet de trouver une liste de contact correspondant au pattern donné.
	 * @param pattern -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/reducer/mail/contacts/{pattern}")
	public ResponseEntity<List<ContactDescriptor>> findContacts(@PathVariable("pattern") final String pattern) throws HomeServerException {
		return ResponseEntity.ok(this.service.findContacts(pattern));
	}
	
	/**
	 * Suppression d'une image réduite.
	 * @param base64ReducedImage -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/reducer/reducedImage", method = { RequestMethod.DELETE })
	public void deleteReducedImage(@RequestBody final FileDescriptor base64ReducedImage) throws HomeServerException {
			this.service.deleteReducedImage(base64ReducedImage);
	}
	
	/**
	 * Suppression d'une image réduite.
	 * @param fileID -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/reducer/reducedImage/{id}", method = { RequestMethod.DELETE })
	public void deleteReducedImage(@PathVariable("id") final String fileID) throws HomeServerException {
			this.service.deleteReducedImage(fileID);
	}
	
	/**
	 * Récupération de l'ensemble des images réduites présentes sur le serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/reducer/reducedImages")
	public ResponseEntity<List<FileDescriptor>> getReducedImages() throws HomeServerException {
		return ResponseEntity.ok(this.service.getReducedImages());
	}
	
	/**
	 * Récupération d'une image sur le serveur.
	 * @param fileName -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/reducer/reducedImages/{base64FileName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public ResponseEntity<byte[]> getReducedImage(@PathVariable("base64FileName") final String fileName) throws HomeServerException {
		return ResponseEntity.ok(this.service.getReducedImage(fileName));
	}
	
	
	
	
	
}
