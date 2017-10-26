package maroroma.homeserverng.music.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Interface pour le service de gestion des tags mp3.
 * @author rlevexie
 *
 */
public interface MusicService {

	/**
	 * Préparation du répertoire de travail pour cet album.
	 * @param request -
	 * @return -
	 */
	AlbumDescriptor prepareWorkingDirectory(AlbumDescriptor request);
	
	/**
	 * Rajout d'une jaquette dans le répertoire.
	 * @param toUpdatePath -
	 * @param albumart -
	 * @return -
	 * @throws HomeServerException -
	 */
	AlbumDescriptor addAlbumArt(String toUpdatePath, MultipartFile albumart) throws HomeServerException;
	
	/**
	 * Retourne l'album art.
	 * @param albumPath -
	 * @param albumArtPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] getAlbumArt(String albumPath, String albumArtPath) throws HomeServerException;
	
	/**
	 * Ajoute un fichier mp3 dans le répertoire de travail.
	 * @param toUpdatePath -
	 * @param oneTrack -
	 * @return -
	 * @throws HomeServerException -
	 */
	TrackDescriptor addTrack(String toUpdatePath, MultipartFile oneTrack) throws HomeServerException;
	
	/**
	 * Retourne les tracks descriptors correspondants au mp3 correspondant l'album.
	 * @param toUpdatePath -
	 * @return -
	 */
	List<TrackDescriptor> getAllTracks(String toUpdatePath);

	/**
	 * Mise à jour des tags mp3 de l'album.
	 * @param td -
	 * @return -
	 * @throws HomeServerException -
	 */
	TrackDescriptor updateTrack(TrackDescriptor td) throws HomeServerException;
	
	/**
	 * Retourne un fichier mp3 de l'album.
	 * @param trackPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] getTrack(String trackPath) throws HomeServerException;

	/**
	 * Retourne l'album complet au format .tar .
	 * @param albumPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] downloadAllFiles(String albumPath) throws HomeServerException;
}
