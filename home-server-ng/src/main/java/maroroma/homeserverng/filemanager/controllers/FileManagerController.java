package maroroma.homeserverng.filemanager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.filemanager.FileManagerModuleDescriptor;
import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.FileDeletionResult;
import maroroma.homeserverng.filemanager.services.FileManagerService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;


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
	private FileManagerService fileService;
	
	/**
	 * Création d'un répertoire.
	 * @param creationRequest -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PostMapping("/filemanager/directory")
	public ResponseEntity<FileDescriptor> createNewDirectory(final @RequestBody DirectoryCreationRequest creationRequest) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.createDirectory(creationRequest));
	}
	
	/**
	 * Retourne la liste des répertoires racines du filemanager.
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("/filemanager/rootdirectories")
	public ResponseEntity<List<FileDirectoryDescriptor>> getRootDirectories() throws HomeServerException {
		return ResponseEntity.ok(this.fileService.getRootDirectories());
	}
	
	/**
	 * Retourne le détail complet d'un répertoire donné.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("/filemanager/directories/{id}")
	public ResponseEntity<FileDirectoryDescriptor> getDirectoryDetails(final @PathVariable("id") String id) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.getDirectoryDetail(id));
	}
	
	/**
	 * Demande de suppression d'un fichier.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	@DeleteMapping("/filemanager/files/{id}")
	public ResponseEntity<FileDeletionResult> deleteFile(final @PathVariable("id") String id) throws HomeServerException {
		return ResponseEntity.ok(this.fileService.deleteFile(id));
	}
	
	
}
