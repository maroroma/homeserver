package maroroma.homeserverng.seedbox.tools;

/**
 * Classe utilitaire pour le stockage des constantes communes au module scanner.
 * @author rlevexie
 *
 */
public abstract class SeedboxModuleConstants {

	
	/**
	 * Nom du module : {@value}.
	 */
	public static final String HOME_MODULE_SEEDBOX = "home.moduleSeedbox";
	
	/**
	 * Nom du cache pour la liste des cibles de la seedbox : {@value}.
	 */
	public static final String SEEDBOX_TARGET_LIST_CACHE_NAME = "SEEDBOX_TARGET_LIST_CACHE_NAME";
	
	/**
	 * Clef de propriété pour l'accès au répertoire de torrents complétés.
	 */
	public static final String HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY = "homeserver.seedbox.todo.directory";

}
