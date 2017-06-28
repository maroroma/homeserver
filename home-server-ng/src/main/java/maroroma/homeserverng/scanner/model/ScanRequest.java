package maroroma.homeserverng.scanner.model;

import lombok.Data;

/**
 * Requete pour une demande de scanner.
 * @author rlevexie
 *
 */
@Data
public class ScanRequest {
	/**
	 * Nom du fichier pour le scan.
	 */
	private String fileName;
	
	/**
	 * Option du mode de couleur.
	 */
	private ScannerColorMode colorMode;
}
