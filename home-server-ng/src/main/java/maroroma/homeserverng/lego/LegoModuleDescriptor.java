package maroroma.homeserverng.lego;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de gestion des legos
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = LegoModuleDescriptor.MODULE_LEGO_ID,
		moduleDescription = "module permettant de gérer le catalog de lego",
		displayName = "Lego",
		cssMenu = "glyphicon glyphicon-headphones",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false, 
		propertiesFile = "lego.properties.json"
		)
public class LegoModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_LEGO_ID = "lego";
}
