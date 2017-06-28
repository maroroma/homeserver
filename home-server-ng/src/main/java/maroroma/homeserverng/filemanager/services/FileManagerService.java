package maroroma.homeserverng.filemanager.services;

import java.util.List;

import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.FileDeletionResult;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;

/**
 * Service de gestion centralisé de la manipulation des fichiers.
 * @author rlevexie
 *
 */
public interface FileManagerService {

	/**
	 * Création d'un répertoir.
	 * @param creationRequest -
	 * @return -
	 * @throws HomeServerException -
	 */
	FileDescriptor createDirectory(DirectoryCreationRequest creationRequest) throws HomeServerException;
	
	/**
	 * Retourne la liste des répertoires gérables directement par le filemanager.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<FileDirectoryDescriptor> getRootDirectories() throws HomeServerException;
	
	FileDirectoryDescriptor getDirectoryDetail(String id) throws HomeServerException;

	FileDeletionResult deleteFile(String id);
	
}
