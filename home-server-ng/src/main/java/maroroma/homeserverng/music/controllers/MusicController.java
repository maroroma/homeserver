package maroroma.homeserverng.music.controllers;

import maroroma.homeserverng.music.MusicModuleDescriptor;
import maroroma.homeserverng.music.model.AddTracksFromExistingSourceRequest;
import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.music.services.MusicServiceImpl;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller pour la gestion des fichiers musicaux.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = MusicModuleDescriptor.class)
public class MusicController {

	/**
	 * Service sous jacent.
	 */
	@Autowired
	private MusicServiceImpl service;

	/**
	 * Création du répertoire de travail?
	 * @param request -
	 * @return -
	 * @throws HomeServerException - 
	 */
	@PostMapping("${homeserver.api.path:}/music/workingdirectories")
	public ResponseEntity<AlbumDescriptor> prepareWorkingDirectory(@RequestBody final AlbumDescriptor request) throws HomeServerException {
		return ResponseEntity.ok(this.service.prepareWorkingDirectory(request));
	}

	/**
	 * Mise à jour du répertoire par upload d'un albumart.
	 * @param toUpdatePath -
	 * @param albumart -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PatchMapping("${homeserver.api.path:}/music/workingdirectories/{id}/albumart")
	public ResponseEntity<AlbumDescriptor> addAlbumArt(@PathVariable("id") final String toUpdatePath,
													   final HttpServletRequest request) throws HomeServerException {
		return ResponseEntity.ok(this.service.addAlbumArt(toUpdatePath, request));
	}

	
	/**
	 * Retourne l'album art.
	 * @param albumPath -
	 * @param albumArtPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping(value = "${homeserver.api.path:}/music/workingdirectories/{albumId}/albumart",
			produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public void getAlbumArt(
			@PathVariable("albumId") final String albumPath,
			final HttpServletResponse response) throws HomeServerException {
		this.service.getAlbumArt(albumPath, response);
	}
	
	/**
	 * Upload d'un fichier mp3 dans le répertoire de travail.
	 * @param toUpdatePath -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PostMapping("${homeserver.api.path:}/music/workingdirectories/{id}/tracks")
	public ResponseEntity<TrackDescriptor> addTrack(@PathVariable("id") final String toUpdatePath, final HttpServletRequest request) throws HomeServerException {
		return ResponseEntity.ok(this.service.addUploadedTrack(toUpdatePath, request));
	}

	/**
	 * Retourne la liste des fichiers disponibles depuis le serveur
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/music/availablefiles")
	public ResponseEntity<List<TodoFile>> availableFilesToAddFromServer() {
		return ResponseEntity.ok(this.service.listExistingTodoFiles());
	}

	/**
	 * Ajout des fichiers séletionnés à l'album. Les fichiers sont issus directement du serveur.
	 * @param albumId -
	 * @param addTracksFromExistingSourceRequest -
	 * @return -
	 * @throws HomeServerException
	 */
	@PostMapping("${homeserver.api.path:}/music/workingdirectories/{id}/existingtracks")
	public ResponseEntity<List<TrackDescriptor>> addTrackFromExistingFiles(@PathVariable("id") final String albumId, @RequestBody AddTracksFromExistingSourceRequest addTracksFromExistingSourceRequest) throws HomeServerException {
		return ResponseEntity.ok(this.service.addExistingTracks(albumId, addTracksFromExistingSourceRequest));
	}
	
	/**
	 * Mise à jour des tags mp3 pour un morceau donné.
	 * @param toUpdatePath -
	 * @param td -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PatchMapping("${homeserver.api.path:}/music/workingdirectories/{id}/tracks")
	public ResponseEntity<TrackDescriptor> updateTracks(@PathVariable("id") final String toUpdatePath, 
			@RequestBody final TrackDescriptor td) throws HomeServerException {
		return ResponseEntity.ok(this.service.updateTrack(td));
	}
	
	/**
	 * Retourne la liste des descriptor des morceaux d'un album.
	 * @param toUpdatePath -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/music/workingdirectories/{id}/tracks")
	public ResponseEntity<List<TrackDescriptor>> getAllTracks(@PathVariable("id") final String toUpdatePath) throws HomeServerException {
		return ResponseEntity.ok(this.service.getAllTracks(toUpdatePath));
	}
	
	/**
	 * Retourne un fichier mp3 sélectionné.
	 * @param toUpdatePath -
	 * @param trackPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping(path = "${homeserver.api.path:}/music/workingdirectories/{id}/tracks/{idtrack}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<byte[]> getOneTrack(@PathVariable("id") final String toUpdatePath, 
			@PathVariable("idtrack") final String trackPath) throws HomeServerException {
		return ResponseEntity.ok(this.service.getTrack(trackPath));
	}
	
	/**
	 * Retourne le répertoire de travail en tant que fichier tar.
	 * @param albumToDownload -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping(path = "${homeserver.api.path:}/music/workingdirectories/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<byte[]> getWorkingDir(@PathVariable("id") final String albumToDownload) throws HomeServerException {
		return ResponseEntity.ok(this.service.downloadAllFiles(albumToDownload));
	}

	/**
	 * Permet de compléter l'album en cours de traitement
	 * @param albumToComplete identifiant de l'album à compléter
	 * @return -
	 * @throws HomeServerException
	 */
	@PatchMapping(path = "${homeserver.api.path:}/music/workingdirectories/{id}/complete")
	public ResponseEntity<AlbumDescriptor> complete(@PathVariable("id") final String albumToComplete) throws HomeServerException {
		return ResponseEntity.ok(this.service.completeAlbumDescriptor(albumToComplete));
	}

	/**
	 * transfert d'un album vers le répertoire final
	 * @param albumToComplete
	 * @return
	 * @throws HomeServerException
	 */
	@PostMapping(path = "${homeserver.api.path:}/music/workingdirectories/{id}/archive")
	public ResponseEntity<AlbumDescriptor> archive(@PathVariable("id") final String albumToComplete) throws HomeServerException {
		return ResponseEntity.ok(this.service.archive(albumToComplete));
	}

	/**
	 * Retourne la liste des albums complétés
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping(path = "${homeserver.api.path:}/music/workingdirectories")
	public ResponseEntity<List<AlbumDescriptor>> getCompletedAlbums() throws HomeServerException {
		return ResponseEntity.ok(this.service.getCompletedAlbumDescriptors());
	}
	
}
