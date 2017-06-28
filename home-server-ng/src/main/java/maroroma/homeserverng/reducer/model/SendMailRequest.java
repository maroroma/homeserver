package maroroma.homeserverng.reducer.model;

import java.util.List;

import lombok.Data;

/**
 * Requête pour l'émission d'un  mail avec des images réduites.
 * @author RLEVEXIE
 *
 */
@Data
public class SendMailRequest {

	/**
	 * Liste des images réduites.
	 */
	private List<ReducedImageInput> imagesToSend;
	
	/**
	 * Liste des emails cibles (,).
	 */
	private List<String> emailList;
	
	/**
	 * Sujet du mail.
	 */
	private String subject;
	
	/**
	 * Message pour le mail.
	 */
	private String message;
}
