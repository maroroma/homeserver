package maroroma.homeserverng.scanner.services;

import java.io.File;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.scanner.exceptions.SseEmiterException;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Interface de gestion des {@link SseEmitter}.
 * @author RLEVEXIE
 *
 */
public interface ScannerEventEmitter {

	/**
	 * Permet de créer un {@link SseEmitter}, et de le rajouter à la liste courante.
	 * @param id identifiant pour l'abonnement.
	 * @return -
	 * @throws HomeServerException - 
	 */
	SseEmitter createEmitter(String id) throws HomeServerException;

	/**
	 * Permet d'émettre un event correspondant à une demande de scan.
	 * @throws SseEmiterException -
	 */
	void emitScanRequested() throws SseEmiterException;

	/**
	 * Permet d'émettre un event correspondant à scan en cours.
	 * @throws SseEmiterException -
	 */
	void emitScanRunning() throws SseEmiterException;

	/**
	 * Permet d'émettre un event correspondant à fichier raw prêt.
	 * @throws SseEmiterException -
	 */
	void emitNPMReady() throws SseEmiterException;

	/**
	 * Permet d'émettre un event correspondant à une conversion du fichier raw en jpg.
	 * @throws SseEmiterException -
	 */
	void emitConversionRunning() throws SseEmiterException;

	/**
	 * Permet d'émettre un event correspondant à une fin de scan.
	 * @param jpgFile fichier final
	 * @throws SseEmiterException -
	 */
	void emitScanCompleted(File jpgFile) throws SseEmiterException;

	/**
	 * Permet d'émettre un event correspondant à une erreur rencontrée lors du scan.
	 * @param e exception
	 * @throws SseEmiterException -
	 */
	void emitError(Exception e) throws SseEmiterException;

}
