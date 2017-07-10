package maroroma.homeserverng.seedbox.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.sse.SSEStreamable;

/**
 * Interface de définition d'un client vers le service web proposé par le client torrent transmission.
 * @author RLEVEXIE
 *
 */
public interface TransmissionClient extends SSEStreamable {
	/**
	 * Upload d'un torrent.
	 * @param torrentToUpload fichier / torrent à lancer.
	 * @throws HomeServerException -
	 */
	void uploadTorrent(MultipartFile torrentToUpload) throws HomeServerException;
	
	/**
	 * Suppression du torrent donné.
	 * @param torrentId -
	 * @throws HomeServerException -
	 */
	void deleteTorrent(String torrentId) throws HomeServerException;
	
	/**
	 * Retourne l'ensemble des torrents en cours.
	 * @return - 
	 * @throws HomeServerException -
	 */
	List<RunningTorrent> getRunningTorrents() throws HomeServerException;
}
