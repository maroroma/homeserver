package maroroma.homeserverng.filemanager.model;


import lombok.Data;
import maroroma.homeserverng.tools.files.FileDescriptor;

/**
 * Correspond à une requête de renommage.
 * @author rlevexie
 *
 */
@Data
public class RenameFileDescriptor {
	/**
	 * Nouveau nom demandé.
	 */
	private String newName;
	
	/**
	 * Fichier original.
	 */
	private FileDescriptor originalFile;
}
