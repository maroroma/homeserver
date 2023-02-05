package maroroma.homeserverng.book;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.AbstractModuleDescriptor;

/**
 * Descriptor pour le module de gestion des books
 * @author rlevexie
 *
 */
@HomeServerModuleDescriptor(
		moduleId = BookModuleDescriptor.MODULE_BOOK_ID,
		moduleDescription = "module permettant de gérer la bibliotheques papier",
		displayName = "Lego",
		cssMenu = "glyphicon glyphicon-headphones",
		hasServerSide = true,
		hasClientSide = true,
		isReadOnly = false, 
		propertiesFile = "books.properties.json"
		)
public class BookModuleDescriptor extends AbstractModuleDescriptor {
	/**
	 * Identifiant du module d'administration.
	 */
	public static final String MODULE_BOOK_ID = "books";
}
