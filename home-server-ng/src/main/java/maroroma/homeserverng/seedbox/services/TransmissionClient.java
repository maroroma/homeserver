package maroroma.homeserverng.seedbox.services;

import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.sse.SSEStreamable;

/**
 * Interface de définition d'un client vers le service web proposé par le client torrent transmission.
 * @author RLEVEXIE
 *
 */
public interface TransmissionClient extends SSEStreamable {
	void uploadTorrent(MultipartFile torrentToUpload) throws HomeServerException;
	void deleteTorrent(String torrentId) throws HomeServerException;
}
