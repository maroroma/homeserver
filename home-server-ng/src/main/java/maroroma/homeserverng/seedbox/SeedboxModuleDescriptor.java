package maroroma.homeserverng.seedbox;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;

/**
 * Descripteur pour le module de scanner.
 * @author RLEVEXIE
 *
 */
@HomeServerModuleDescriptor(
		moduleId = "seedbox",
		moduleDescription = "module permettant de gérer la liste des downloads terminés sur la seedbox",
		displayName = "Seedbox",
		cssMenu = "glyphicon glyphicon-download-alt",
		hasServerSide = true,
		hasClientSide = true,
		propertiesFile = "seedbox.properties.json"
		)
public class SeedboxModuleDescriptor {

}
