package maroroma.homeserverng.seedbox.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import maroroma.homeserverng.seedbox.SeedboxModuleDescriptor;
import maroroma.homeserverng.seedbox.model.NewTorrents;
import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.services.SeedBoxTodoService;
import maroroma.homeserverng.seedbox.services.SeedboxRemoteControlServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.model.MoveRequest;
import maroroma.homeserverng.tools.model.MovedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Controller pour la gestion de la seedbox.
 * @author RLEVEXIE
 *
 */
@HomeServerRestController(moduleDescriptor = SeedboxModuleDescriptor.class)
public class SeedBoxController {

	/**
	 * SErvice pour la gestion des TODO.
	 */
	@Autowired
	private SeedBoxTodoService todoService;
	
	/**
	 * Service pour la gestion des torrents.
	 */
	@Autowired
	private SeedboxRemoteControlServiceImpl seedBoxRemoteService;

	/**
	 * retourne la liste des fichiers à trier.
	 * @return -
	 * @throws HomeServerException 
	 */
	@RequestMapping("/seedbox/todo/completedtorrents")
	public final ResponseEntity<List<TodoFile>> getTodoList() throws HomeServerException {
		return ResponseEntity.ok(
				todoService.getTodoList());
	}
	
	/**
	 * Permet de supprimer un fichier ou un répertoire de la todolist.
	 * @param toDelete -
	 * @return -
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/seedbox/todo/completedtorrent", method = {RequestMethod.DELETE})
	public final ResponseEntity<List<TodoFile>> deleteTodoFile(@RequestBody final TodoFile toDelete) {
		return ResponseEntity.ok(todoService.deleteTodoFile(toDelete));
	}
	
	/**
	 * Permet de supprimer un fichier ou un répertoire de la todolist.
	 * @param fileId -
	 * @return -
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/seedbox/todo/completedtorrents/{id}", method = {RequestMethod.DELETE})
	public final ResponseEntity<List<TodoFile>> deleteTodoFile(@PathVariable("id") final String fileId) {
		return ResponseEntity.ok(todoService.deleteTodoFile(fileId));
	}

	/**
	 * retourne la liste des répertoires cible.
	 * @return -
	 */
	@RequestMapping("/seedbox/todo/targets")
	public ResponseEntity<List<TargetDirectory>> getTargetList() {
		
		return ResponseEntity.ok(todoService.getTargetList());
	}
	
		
	/**
	 * Retourne le contenu d'un répertoire.
	 * @param directoryToParse -
	 * @return -
	 */
	@RequestMapping(value = "/seedbox/todo/target/detail", method = { RequestMethod.POST })
	public ResponseEntity<List<FileDescriptor>> getFolderContent(@RequestBody final FileDescriptor directoryToParse) {
		return ResponseEntity.ok(this.todoService.getDirectoryDetails(directoryToParse));
	}
	
	/**
	 * Retourne le contenu d'un répertoire.
	 * @param fileId -
	 * @return -
	 */
	@RequestMapping(value = "/seedbox/todo/targets/{id}/files", method = { RequestMethod.GET })
	public ResponseEntity<FileDirectoryDescriptor> getFolderContent(@PathVariable("id") final String fileId) {
		return ResponseEntity.ok(this.todoService.getDirectoryDetails(fileId));
	}

	/**
	 * Permet de lancer le déplacement des fichiers triés vers la target donnée.
	 * @param moveRequest info pour le déplacement.
	 * @return -
	 * @throws HomeServerException 
	 */
	@RequestMapping(value = "/seedbox/todo/sortedfile", method = { RequestMethod.POST })
	public ResponseEntity<List<MovedFile>> moveFiles(@RequestBody final MoveRequest moveRequest) throws HomeServerException {
		
		return ResponseEntity.ok(todoService.moveFiles(moveRequest));
	}

	/**
	 * Retourne l'ensemble des torrents en cours.
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("/seedbox/torrents")
	public ResponseEntity<List<RunningTorrent>> getRunningTorrents() throws HomeServerException {
		return ResponseEntity.ok(this.seedBoxRemoteService.getTorrents());
	}

	/**
	 * Rajout d'un nouveau torrent.
	 * @param newTorrent -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PostMapping("/seedbox/torrents")
	public ResponseEntity addTorrent(@RequestBody NewTorrents newTorrent) throws HomeServerException {
		this.seedBoxRemoteService.addTorrent(newTorrent);
		return ResponseEntity.ok().build();
	}
	
	

}
