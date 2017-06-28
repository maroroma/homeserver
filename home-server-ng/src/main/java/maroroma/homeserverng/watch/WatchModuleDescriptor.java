package maroroma.homeserverng.watch;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;

/**
 * Descripteur pour le module de watch.
 * @author RLEVEXIE
 *
 */
@HomeServerModuleDescriptor(
		moduleId = "watch",
		moduleDescription = "module permettant de gérer les webcams de surveillance",
		displayName = "Watch",
		cssMenu = "glyphicon glyphicon-sunglasses",
		hasServerSide = true,
		hasClientSide = true,
		propertiesFile = "watch.properties.json",
		isReadOnly = false
		)
public class WatchModuleDescriptor {

}
