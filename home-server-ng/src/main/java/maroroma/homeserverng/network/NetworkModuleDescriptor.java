package maroroma.homeserverng.network;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descripteur pour le module de scanner.
 * @author RLEVEXIE
 *
 */
@HomeServerModuleDescriptor(
		moduleId = NetworkModuleDescriptor.MODULE_NETWORK_ID,
		moduleDescription = "module permettant de gérer les informations du réseau local",
		displayName = "Network",
		cssMenu = "glyphicon glyphicon-cloud",
		hasServerSide = true,
		hasClientSide = false,
		isReadOnly = true,
		propertiesFile = "network.properties.json"
		)
public class NetworkModuleDescriptor extends AbstractModuleDescriptor {

	/**
	 * Identifiant du module network.
	 */
	public static final String MODULE_NETWORK_ID = "network";

}
