package maroroma.homeserverng.reducer.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.reducer.model.ReducedImageInput;
import maroroma.homeserverng.reducer.model.SendMailRequest;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.mail.ContactDescriptor;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Interface de définition du service de réduction.
 * @author RLEVEXIE
 *
 */
public interface ReducerService {

	/**
	 * Upload d'une image réduite sur le serveur.
	 * @param imageInput -
	 * @throws HomeServerException -
	 */
	void uploadReducedImage(ReducedImageInput imageInput) throws HomeServerException;
	
	/**
	 * Listing des images présentes sur le serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<FileDescriptor> getReducedImages() throws HomeServerException;
	
	/**
	 * Récupération du contenu d'une image du serveur.
	 * @param fileName -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] getReducedImage(String fileName) throws HomeServerException;
	
	/**
	 * Suppression d'une image sur le serveur.
	 * @param base64ReducedImage -
	 * @throws HomeServerException -
	 */
	void deleteReducedImage(FileDescriptor base64ReducedImage) throws HomeServerException;

	/**
	 * Permet d'émettre un email avec le listing des images donné.
	 * @param mailRequest -
	 * @throws HomeServerException -
	 */
	void sendMail(SendMailRequest mailRequest) throws HomeServerException;

	/**
	 * Permet de rechercher un contact par son email.
	 * @param pattern chaine à rechercher.
	 * @return - 
	 * @throws HomeServerException -
	 */
	List<ContactDescriptor> findContacts(String pattern) throws HomeServerException;

	/**
	 * Suppression d'une image sur le serveur.
	 * @param fileID -
	 * @throws HomeServerException -
	 */
	void deleteReducedImage(String fileID) throws HomeServerException;
	
	/**
	 * Permet de réduire une image envoyée par un client et de la rajouter dans la liste des images réduites sur le serveur.
	 * @param imageToReduce image à réduire
	 * @throws HomeServerException -
	 */
	void reduceImage(MultipartFile imageToReduce) throws HomeServerException;
	
}
