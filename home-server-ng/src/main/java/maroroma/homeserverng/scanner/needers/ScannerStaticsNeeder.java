package maroroma.homeserverng.scanner.needers;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.needers.StaticsNeeder;


/**
 * Permet de demander la définition d'un path http pour le service des fichiers statiques associés au scanner.
 * @author RLEVEXIE
 *
 */
@Component
public class ScannerStaticsNeeder implements StaticsNeeder {

	
	/**
	 * Chemin d'exposition http des scans disponibles sur le serveur.
	 */
	public static final String SCANNER_DONESCANS_RESOURCE_HANDLER = "/scanner/availablescans/**";

	/**
	 * Emplacement des scans réalisés.
	 */
	@Property("homeserver.scanner.donescans.directory")
	private HomeServerPropertyHolder doneScansDirectory;
	
	
	@Override
	public String getHandler() {
		return SCANNER_DONESCANS_RESOURCE_HANDLER;
	}

	@Override
	public String getLocations() {
		return this.doneScansDirectory.asClassPathPath();
	}

}
