package maroroma.homeserverng.music;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de gestion des photos.
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = MusicModuleDescriptor.MODULE_REDUCER_ID,
		moduleDescription = "module permettant de gérer musiques stockées sur le serveur",
		displayName = "Music",
		cssMenu = "glyphicon glyphicon-headphones",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false, 
		propertiesFile = "music.properties.json"
		)
public class MusicModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_REDUCER_ID = "music";
}
