package maroroma.homeserverng.notifyer;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de notification.
 * Ne possède pas de vue IHM propre, mais reste paramétrable par le menu d'administration.
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = NotifyerModuleDescriptor.MODULE_REDUCER_ID,
		moduleDescription = "module permettant de réaliser des notifications lors de certains évenements de l'application.",
		displayName = "Notifyer",
		cssMenu = "glyphicon glyphicon-bullhorn",
		hasServerSide = true,
		hasClientSide = false,
		isReadOnly = false, 
		propertiesFile = "notifyer.properties.json"
		)
public class NotifyerModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_REDUCER_ID = "notifyer";
}
