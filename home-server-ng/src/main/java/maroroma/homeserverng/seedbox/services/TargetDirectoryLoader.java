package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.tools.model.FileDescriptor;

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

	/**
	 * Retourne le nom du répertoire dans les alias kodi. cette information doit permettre le scan du répertoire.
	 * @return -
	 */
	String[] getKodiAliases();

	/**
	 * Détermine si cette cible contient le répertoire en entrée.
	 * @param fileDescriptor -
	 * @return
	 */
	default boolean includes(FileDescriptor fileDescriptor) {
		return fileDescriptor.createFile()
				.getParentFile()
				.getAbsolutePath()
				.startsWith(this.loadTargetDirectory().getFullName());
	}

	/**
	 * Lance un scan du répertore tel qu'il est identifié sur une instance kodi distante.
	 */
	void executeScanOnKodiInstances();
	
}
