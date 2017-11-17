package maroroma.homeserverng.music.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.music.MusicModuleDescriptor;
import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.music.services.MusicService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

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
	private MusicService service;

	/**
	 * Création du répertoire de travail?
	 * @param request -
	 * @return -
	 * @throws HomeServerException - 
	 */
	@PostMapping("/music/workingdirectories")
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
	@PatchMapping("/music/workingdirectories/{id}/albumart")
	public ResponseEntity<AlbumDescriptor> addAlbumArt(@PathVariable("id") final String toUpdatePath,
			@RequestBody final MultipartFile albumart) throws HomeServerException {
		return ResponseEntity.ok(this.service.addAlbumArt(toUpdatePath, albumart));
	}

	
	/**
	 * Retourne l'album art.
	 * @param albumPath -
	 * @param albumArtPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping(value = "/music/workingdirectories/{albumpath}/albumart/{albumArtPath}", 
			produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public ResponseEntity<byte[]> getAlbumArt(@PathVariable("albumpath") final String albumPath,
			@PathVariable("albumArtPath") final String albumArtPath) throws HomeServerException {
		return ResponseEntity.ok(this.service.getAlbumArt(albumPath, albumArtPath));
	}
	
	/**
	 * Upload d'un fichier mp3 dans le répertoire de travail.
	 * @param toUpdatePath -
	 * @param track -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PostMapping("/music/workingdirectories/{id}/tracks")
	public ResponseEntity<TrackDescriptor> addTrack(@PathVariable("id") final String toUpdatePath,
			@RequestBody final MultipartFile track) throws HomeServerException {
		return ResponseEntity.ok(this.service.addTrack(toUpdatePath, track));
	}
	
	/**
	 * Mise à jour des tags mp3 pour un morceau donné.
	 * @param toUpdatePath -
	 * @param td -
	 * @return -
	 * @throws HomeServerException -
	 */
	@PatchMapping("/music/workingdirectories/{id}/tracks")
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
	@GetMapping("/music/workingdirectories/{id}/tracks")
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
	@GetMapping(path = "/music/workingdirectories/{id}/tracks/{idtrack}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
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
	@GetMapping(path = "/music/workingdirectories/{id}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<byte[]> getWorkingDir(@PathVariable("id") final String albumToDownload) throws HomeServerException {
		return ResponseEntity.ok(this.service.downloadAllFiles(albumToDownload));
	}
	
}
