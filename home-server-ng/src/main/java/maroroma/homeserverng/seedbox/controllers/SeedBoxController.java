package maroroma.homeserverng.seedbox.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import maroroma.homeserverng.seedbox.SeedboxModuleDescriptor;
import maroroma.homeserverng.seedbox.model.NewTorrents;
import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.model.TorrentsToDelete;
import maroroma.homeserverng.seedbox.services.SeedBoxTodoServiceImpl;
import maroroma.homeserverng.seedbox.services.SeedboxRemoteControlServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.MoveRequest;
import maroroma.homeserverng.tools.files.MovedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.util.*;

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
	private SeedBoxTodoServiceImpl todoService;
	
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
	@RequestMapping("${homeserver.api.path:}/seedbox/todo/completedtorrents")
	public final ResponseEntity<List<TodoFile>> getTodoList() throws HomeServerException {
		return ResponseEntity.ok(
				todoService.getTodoList());
	}
	
	/**
	 * Permet de supprimer un fichier ou un répertoire de la todolist.
	 * @param fileId -
	 * @return -
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "${homeserver.api.path:}/seedbox/todo/completedtorrents/{id}", method = {RequestMethod.DELETE})
	public final ResponseEntity<List<TodoFile>> deleteTodoFile(@PathVariable("id") final String fileId) {
		return ResponseEntity.ok(todoService.deleteTodoFile(fileId));
	}

	/**
	 * retourne la liste des répertoires cible.
	 * @return -
	 */
	@RequestMapping("${homeserver.api.path:}/seedbox/todo/targets")
	public ResponseEntity<List<TargetDirectory>> getTargetList() {
		
		return ResponseEntity.ok(todoService.getTargetList());
	}

	/**
	 * Retourne le contenu d'un répertoire.
	 * @param fileId -
	 * @return -
	 */
	@RequestMapping(value = "${homeserver.api.path:}/seedbox/todo/targets/{id}/files", method = { RequestMethod.GET })
	public ResponseEntity<FileDirectoryDescriptor> getFolderContent(@PathVariable("id") final String fileId) {
		return ResponseEntity.ok(this.todoService.getDirectoryDetails(fileId));
	}

	/**
	 * Permet de lancer le déplacement des fichiers triés vers la target donnée.
	 * @param moveRequest info pour le déplacement.
	 * @return -
	 * @throws HomeServerException 
	 */
	@RequestMapping(value = "${homeserver.api.path:}/seedbox/todo/sortedfile", method = { RequestMethod.POST })
	public ResponseEntity<List<MovedFile>> moveFiles(@RequestBody final MoveRequest moveRequest) throws HomeServerException {
		
		return ResponseEntity.ok(todoService.moveFiles(moveRequest));
	}

	/**
	 * Retourne l'ensemble des torrents en cours.
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/seedbox/torrents")
	public ResponseEntity<List<RunningTorrent>> getRunningTorrents() throws HomeServerException {
		return ResponseEntity.ok(this.seedBoxRemoteService.getTorrents());
	}

	/**
	 * Rajout d'un nouveau torrent.
	 * @param newTorrent -
	 * @return -
	 */
	@PostMapping("${homeserver.api.path:}/seedbox/torrents")
	public ResponseEntity addTorrent(@RequestBody NewTorrents newTorrent) {
		this.seedBoxRemoteService.addTorrent(newTorrent);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("${homeserver.api.path:}/seedbox/torrents")
	public ResponseEntity removeTorrents(@RequestBody TorrentsToDelete torrentsToDelete) {
		this.seedBoxRemoteService.removeTorrents(torrentsToDelete);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("${homeserver.api.path:}/seedbox/torrents/{id}")
	public ResponseEntity<Boolean> deleteTorrent(@PathVariable("id") final Integer torrentId) {
		this.seedBoxRemoteService.removeTorrents(TorrentsToDelete.builder().idsToDelete(List.of(torrentId)).build());
		return ResponseEntity.ok(true);
	}
	
	

}
