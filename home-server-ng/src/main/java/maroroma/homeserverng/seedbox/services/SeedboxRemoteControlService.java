package maroroma.homeserverng.seedbox.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Interface de définition du service permettant de piloter un client torrent tierce, non inclu dans le homeserver.
 * @author rlevexie
 *
 */
public interface SeedboxRemoteControlService {

	/**
	 * Souscription à la mise à jour des torrents.
	 * @param id -
	 * @return -
	 * @throws HomeServerException 
	 */
	SseEmitter subscribeToTorrentStream(String id) throws HomeServerException;
	
	/**
	 * Désouscription à la mise à jour de l'état des torrents.
	 * @param id -
	 */
	void unsubscribeToTorrentStream(String id);
	
	/**
	 * Ajout d'un torrent. Déclence le téléchargement du torrent donné.
	 * @param torrentFile -
	 * @throws HomeServerException -
	 */
	void addTorrent(MultipartFile torrentFile) throws HomeServerException;
	
	/**
	 * Suppression d'un torrent. Arrête le téléchargement du torrent donné.
	 * @param torrentId -
	 * @throws HomeServerException -
	 */
	void removeTorrent(String torrentId) throws HomeServerException;
	
	/**
	 * Retourne la liste des torrents en cours.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<RunningTorrent> getTorrents() throws HomeServerException;
}
