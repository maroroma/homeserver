package maroroma.homeserverng.remote;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de gestion des composants IOT
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = RemoteModuleDescriptor.MODULE_REMOTE_ID,
		moduleDescription = "module permettant de gérer la réception de commandes distantes",
		displayName = "REMOTE",
		cssMenu = "glyphicon glyphicon-globe",
		hasServerSide = true,
		hasClientSide = false,
		isReadOnly = false, 
		propertiesFile = "remote.properties.json"
		)
public class RemoteModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module de remote.
	 */
	public static final String MODULE_REMOTE_ID = "remote";
}
