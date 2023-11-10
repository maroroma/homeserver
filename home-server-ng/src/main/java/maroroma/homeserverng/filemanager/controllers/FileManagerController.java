package maroroma.homeserverng.filemanager.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.filemanager.FileManagerModuleDescriptor;
import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.RenameFileDescriptor;
import maroroma.homeserverng.filemanager.model.RootDirectoryConfiguration;
import maroroma.homeserverng.filemanager.model.RootDirectoryConfigurationCreationRequest;
import maroroma.homeserverng.filemanager.services.FileManagerConfigurationService;
import maroroma.homeserverng.filemanager.services.FileManagerServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.FileOperationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controller pour la manipulation générique de fichiers.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = FileManagerModuleDescriptor.class)
@RequiredArgsConstructor
public class FileManagerController {

	/**
	 * SErvice sous jacent.
	 */
	private final FileManagerServiceImpl fileService;

	private final FileManagerConfigurationService fileManagerConfigurationService;
	
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

	@GetMapping("${homeserver.api.path:}/filemanager/configuration/rootDirectories")
	public ResponseEntity<List<RootDirectoryConfiguration>> getRootDirectoriesConfiguration() {
		return ResponseEntity.ok(this.fileManagerConfigurationService.getRawRootDirectoriesConfiguration());
	}

	@PostMapping("${homeserver.api.path:}/filemanager/configuration/rootDirectories")
	public ResponseEntity<List<RootDirectoryConfiguration>> addRootDirectory(@RequestBody RootDirectoryConfigurationCreationRequest rootDirectoryConfigurationCreationRequest) {
		return ResponseEntity.ok(this.fileManagerConfigurationService.addRootDirectoryConfiguration(rootDirectoryConfigurationCreationRequest));
	}

	@PatchMapping("${homeserver.api.path:}/filemanager/configuration/rootDirectories/{id}")
	public ResponseEntity<List<RootDirectoryConfiguration>> addRootDirectory(@PathVariable("id") String id,  @RequestBody RootDirectoryConfiguration rootDirectoryConfiguration) {
		return ResponseEntity.ok(this.fileManagerConfigurationService.updateRootDirectoryConfiguration(id, rootDirectoryConfiguration));
	}

	@DeleteMapping("${homeserver.api.path:}/filemanager/configuration/rootDirectories/{id}")
	public ResponseEntity<List<RootDirectoryConfiguration>> addRootDirectory(@PathVariable("id") String id) {
		return ResponseEntity.ok(this.fileManagerConfigurationService.deleteRootDirectoryConfiguration(id));
	}
	
	
}
