package maroroma.homeserverng.filemanager.controllers;

import maroroma.homeserverng.filemanager.FileManagerModuleDescriptor;
import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.RenameFileDescriptor;
import maroroma.homeserverng.filemanager.services.FileManagerServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.files.FileOperationResult;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Controller pour la manipulation générique de fichiers.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = FileManagerModuleDescriptor.class)
public class FileManagerController {

	/**
	 * SErvice sous jacent.
	 */
	@Autowired
	private FileManagerServiceImpl fileService;
	
	/**
	 * Création d'un répertoire.
	 * @param creationRequest -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PostMapping("${homeserver.api.path:}/filemanager/directory")
	public ResponseEntity<FileDescriptor> createNewDirectory(final @RequestBody DirectoryCreationRequest creationRequest) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.createDirectory(creationRequest));
	}
	
	/**
	 * Retourne la liste des répertoires racines du filemanager.
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/filemanager/rootdirectories")
	public ResponseEntity<List<FileDirectoryDescriptor>> getRootDirectories() throws HomeServerException {
		return ResponseEntity.ok(this.fileService.getRootDirectories());
	}
	
	/**
	 * Retourne le détail complet d'un répertoire donné.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/filemanager/directories/{id}")
	public ResponseEntity<FileDirectoryDescriptor> getDirectoryDetails(final @PathVariable("id") String id) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.getDirectoryDetail(id));
	}
	
	/**
	 * Permet de télécharger un fichier selon son encodage Bas64.
	 * @param base64FileName nom du fichier
	 * @param response -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/filemanager/files/{id}")
	public void downloadFile(@PathVariable("id") final String base64FileName, final HttpServletResponse response) throws HomeServerException {
		this.fileService.getFile(base64FileName, response);
	}

	/**
	 * Retourne le file descriptor correspondant à l'id de fichier.
	 * @param base64FileName -
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/filemanager/filedescriptors/{id}")
	public ResponseEntity<FileDescriptor> getFileDescriptor(@PathVariable("id") final String base64FileName) {
		return ResponseEntity.ok(this.fileService.getFileDescriptor(base64FileName));
	}

	/**
	 * Upload d'un fichier sur le serveur
	 * @param base64DirectoryName -
	 * @return -
	 */
	@PostMapping("${homeserver.api.path:}/filemanager/files/{id}")
	public ResponseEntity<List<FileDescriptor>> uploadFile(@PathVariable("id") final String base64DirectoryName, final HttpServletRequest request) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.uploadFiles(base64DirectoryName, request));
	}
	
	/**
	 * Permet de streamer un fichier multimédia.
	 * @param base64FileName identifiant du fichier.
	 * @param request -
	 * @param response -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/filemanager/files/{id}/streaming")
	public void streamFile(@PathVariable("id") final String base64FileName, 
			HttpServletRequest request, HttpServletResponse response) throws HomeServerException {
		this.fileService.streamFile(base64FileName, request, response);
	}
	
	/**
	 * Demande de suppression d'un fichier.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	@DeleteMapping("${homeserver.api.path:}/filemanager/files/{id}")
	public ResponseEntity<FileOperationResult> deleteFile(final @PathVariable("id") String id) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.deleteFile(id));
	}
	
	/**
	 * Demande de renommage de fichier.
	 * @param rfd -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PatchMapping("${homeserver.api.path:}/filemanager/files")
	public ResponseEntity<FileOperationResult> renameFile(final @RequestBody RenameFileDescriptor rfd) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.renameFile(rfd));
	} 
	
	
}
