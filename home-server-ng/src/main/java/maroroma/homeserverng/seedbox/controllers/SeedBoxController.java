package maroroma.homeserverng.seedbox.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import maroroma.homeserverng.seedbox.SeedboxModuleDescriptor;
import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.services.SeedBoxTodoService;
import maroroma.homeserverng.seedbox.services.SeedboxRemoteControlService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.model.MoveRequest;
import maroroma.homeserverng.tools.model.MovedFile;

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
	private SeedboxRemoteControlService seedBoxRemoteService;

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
	 * Souscription à la mise à jour de l'état des torrents.
	 * @param idSubscriber -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/seedbox/torrents/subscribe/{idSubscriber}")
	public SseEmitter subscribeToTorrentsUpdate(final @PathVariable("idSubscriber") String idSubscriber) throws HomeServerException {
		return this.seedBoxRemoteService.subscribeToTorrentStream(idSubscriber);
	}
	
	/**
	 * Désabonnement à la mise à jour de l'état des torrents.
	 * @param idSubscriber -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "/seedbox/torrents/subscribe/{idSubscriber}", method = {RequestMethod.DELETE})
	public void unsubscribeToTorrentsUpdate(final @PathVariable("idSubscriber") String idSubscriber) throws HomeServerException {
		this.seedBoxRemoteService.unsubscribeToTorrentStream(idSubscriber);
	}
	
	/**
	 * Upload de torrents à lancer.
	 * @param file -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "/seedbox/torrent", method = {RequestMethod.POST})
	public void uploadTorrent(@RequestBody final MultipartFile file) throws HomeServerException {
		this.seedBoxRemoteService.addTorrent(file);
	}
	
	/**
	 * Suppression d'un torrent.
	 * @param id -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "/seedbox/torrent/{id}", method = {RequestMethod.DELETE})
	public void deleteTorrent(@PathVariable final String id) throws HomeServerException {
		this.seedBoxRemoteService.removeTorrent(id);
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
	
	

}
