package maroroma.homeserverng.photo;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de gestion des photos.
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = PhotoModuleDescriptor.MODULE_REDUCER_ID,
		moduleDescription = "module permettant de gérer les photos backupées sur le serveur",
		displayName = "Photo",
		cssMenu = "glyphicon glyphicon-camera",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false, 
		propertiesFile = "photo.properties.json"
		)
public class PhotoModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_REDUCER_ID = "photo";
}
