package maroroma.homeserverng.iot;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de gestion des composants IOT
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = IotModuleDescriptor.MODULE_IOT_ID,
		moduleDescription = "module permettant de gérer les composants IOT",
		displayName = "IOT",
		cssMenu = "glyphicon glyphicon-globe",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false, 
		propertiesFile = "iot.properties.json"
		)
public class IotModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'iot.
	 */
	public static final String MODULE_IOT_ID = "iot";
}
