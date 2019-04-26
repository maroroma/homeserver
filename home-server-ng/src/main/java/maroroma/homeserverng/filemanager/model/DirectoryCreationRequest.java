package maroroma.homeserverng.filemanager.model;

import lombok.Data;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;

/**
 * REquete pour la création d'un répertoire.
 * @author rlevexie
 *
 */
@Data
public class DirectoryCreationRequest {
	/**
	 * Répertoire parent.
	 */
	private FileDirectoryDescriptor parentDirectory;
	
	/**
	 * Nom du répertoire à créer.
	 */
	private String directoryName;
}
