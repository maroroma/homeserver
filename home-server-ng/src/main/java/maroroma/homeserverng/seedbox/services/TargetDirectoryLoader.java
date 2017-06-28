package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.TargetDirectory;

/**
 * Interface de définition d'un chargeur de {@link TargetDirectory}.
 * @author rlevexie
 *
 */
public interface TargetDirectoryLoader {

	/**
	 * Retourne le {@link TargetDirectory} géré par l'implémentation.
	 * @return -
	 */
	TargetDirectory loadTargetDirectory();
	
}
