package maroroma.homeserverng.kodimanager;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module kodi.
 * Ne possède pas de vue IHM propre, mais reste paramétrable par le menu d'administration.
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = KodiManagerModuleDescriptor.MODULE_REDUCER_ID,
		moduleDescription = "module permettant de centraliser la gestion des instances kodi",
		displayName = "KodiManager",
		cssMenu = "glyphicon glyphicon-film",
		hasServerSide = true,
		hasClientSide = false,
		isReadOnly = true,
		propertiesFile = "kodimanager.properties.json"
		)
public class KodiManagerModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_REDUCER_ID = "kodimanager";
}
