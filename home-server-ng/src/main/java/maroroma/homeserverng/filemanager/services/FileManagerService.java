package maroroma.homeserverng.filemanager.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.FileOperationResult;
import maroroma.homeserverng.filemanager.model.RenameFileDescriptor;
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
	
	/**
	 * Retourne le détail d'un répertoire.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	FileDirectoryDescriptor getDirectoryDetail(String id) throws HomeServerException;

	/**
	 * Supprime un fichier.
	 * @param id -
	 * @return -
	 */
	FileOperationResult deleteFile(String id);
	
	/**
	 * Renomme un fichier.
	 * @param rfd -
	 * @return -
	 */
	FileOperationResult renameFile(RenameFileDescriptor rfd);

	/**
	 * Permet de télécharger un fichier en écrivant directement dans le flux de retour.
	 * @param base64FileName -
	 * @param response port le flux à modifier.
	 * @throws HomeServerException -
	 */
	void getFile(String base64FileName, HttpServletResponse response) throws HomeServerException;

	/**
	 * Permet de gérer le streaming d'un fichier multimédia (mp3 ou video).
	 * @param base64FileName -
	 * @param request -
	 * @param response -
	 * @throws HomeServerException -
	 */
	void streamFile(String base64FileName, HttpServletRequest request, HttpServletResponse response) throws HomeServerException;
	
}
