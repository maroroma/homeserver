package maroroma.homeserverng.scanner.services;

import java.util.List;

import maroroma.homeserverng.scanner.model.ScanRequest;
import maroroma.homeserverng.scanner.model.ScannerColorMode;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * 
 * Interface de définition d'un service de scan.
 * @author RLEVEXIE
 *
 */
public interface ScannerService {
	
	/**
	 * Lancement d'un scan.
	 * @param request paramétrage du scan demandé.
	 * @throws HomeServerException -
	 */
	void scanWithOptions(ScanRequest request)  throws HomeServerException;
	
	/**
	 * Listing des scans disponibles sur le serveur.
	 * @return - 
	 */
	List<FileDescriptor> listDoneScans();
	
	/**
	 * Suppression d'un scan disponible sur le serveur.
	 * @param fileName -
	 */
	void deleteScan(String fileName);
	
	/**
	 * Listing des options disponibles pour le scan.
	 * @return -
	 */
	List<ScannerColorMode> listColorModes();

	/**
	 * Permet de valider la requête de scan.
	 * @param request -
	 */
	void validateScanRequest(ScanRequest request);
	
	
}
