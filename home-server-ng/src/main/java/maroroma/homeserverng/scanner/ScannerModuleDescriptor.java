package maroroma.homeserverng.scanner;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;

/**
 * Descripteur pour le module de scanner.
 * @author RLEVEXIE
 *
 */
@HomeServerModuleDescriptor(
		moduleId = "scanner",
		moduleDescription = "module permettant de réaliser un scan via les apis rest",
		displayName = "Scanner",
		cssMenu = "glyphicon glyphicon-inbox",
		hasServerSide = true,
		hasClientSide = true,
		propertiesFile = "scanner.properties.json"
		)
public class ScannerModuleDescriptor {

}
