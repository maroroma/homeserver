package maroroma.homeserverng.kiosk;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;

/**
 * Descriptor pour le module kiosk.
 * @author RLEVEXIE
 *
 */
@HomeServerModuleDescriptor(
		moduleId = KioskModuleDescriptor.MODULE_KIOSK_ID,
		moduleDescription = "module pour présenter une page en mode kiosk sur le serveur",
		displayName = "Kiosk",
		cssMenu = "glyphicon glyphicon-blackboard",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false,
		propertiesFile = "kiosk.properties.json"
		)
public class KioskModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_KIOSK_ID = "kiosk";
}
