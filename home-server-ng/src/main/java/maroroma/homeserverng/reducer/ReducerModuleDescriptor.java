package maroroma.homeserverng.reducer;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de réduction d'image.
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = ReducerModuleDescriptor.MODULE_REDUCER_ID,
		moduleDescription = "module permettant de réaliser des réduction inline d'images.",
		displayName = "Image Reducer",
		cssMenu = "glyphicon glyphicon-resize-small",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false, 
		propertiesFile = "reducer.properties.json"
		)
public class ReducerModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_REDUCER_ID = "reducer";
}
