package maroroma.homeserverng.administration;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;


/**
 * Descriptor du module d'administration.
 * @author RLEVEXIE
 *
 */
@HomeServerModuleDescriptor(
		moduleId = AdministrationModuleDescriptor.MODULE_ADMINISTRATION_ID,
		moduleDescription = "module permettant de réaliser des opérations d'administrations du serveur via les API REST",
		displayName = "Administration",
		cssMenu = "glyphicon glyphicon-cog",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = true
		)
public class AdministrationModuleDescriptor {

	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_ADMINISTRATION_ID = "administration";

}
