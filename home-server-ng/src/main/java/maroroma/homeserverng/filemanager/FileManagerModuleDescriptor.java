package maroroma.homeserverng.filemanager;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de notification.
 * Ne possède pas de vue IHM propre, mais reste paramétrable par le menu d'administration.
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = FileManagerModuleDescriptor.MODULE_REDUCER_ID,
		moduleDescription = "module permettant de réaliser des modifications sur le système de fichier.",
		displayName = "FileManager",
		cssMenu = "glyphicon glyphicon-tasks",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false,
		propertiesFile = "filemanager.properties.json"
		)
public class FileManagerModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_REDUCER_ID = "filemanager";
}
