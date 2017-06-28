package maroroma.homeserverng.tools.config;

import java.util.List;

/**
 * Interface de définition d'un gestionnaire de modules.
 * @author rlevexie
 *
 */
public interface HomeServerModuleManager {
	/**
	 * Liste des modules activés pour l'ihm.
	 * @return -
	 */
	List<HomeServerModuleHandler> getEnabledHomeServerModules();

	/**
	 * Détermine si le module est activé.
	 * @param moduleId -
	 * @return -
	 */
	boolean isModuleEnabled(String moduleId);
	
	/**
	 * Liste de l'ensemble des plugins.
	 * @return -
	 */
	List<HomeServerModuleHandler> getAllHomeServerModules();
}
