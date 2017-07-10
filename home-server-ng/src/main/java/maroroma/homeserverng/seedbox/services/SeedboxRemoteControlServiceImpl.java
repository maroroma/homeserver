package maroroma.homeserverng.seedbox.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.tools.annotations.Streamed;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.sse.ServiceStreamer;

/**
 * Impléméntation du {@link SeedboxRemoteControlService}.
 * Permet de piloter un client transmission dont les api web json ont été exposées.
 * @author RLEVEXIE
 *
 */
@Service
public class SeedboxRemoteControlServiceImpl implements SeedboxRemoteControlService {

	/**
	 * Utilisation du {@link TransmissionClientDevImpl} en mode {@link Streamed}.
	 */
	@Autowired
	private ServiceStreamer<TransmissionClient> transmissionStreamer;
	
	/**
	 * {@inheritDoc}
	 * @throws HomeServerException 
	 */
	@Override
	public SseEmitter subscribeToTorrentStream(final String id) throws HomeServerException {
		return this.transmissionStreamer.subscribe(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTorrent(final MultipartFile torrentFile) throws HomeServerException {
		this.transmissionStreamer.getStreamableService().uploadTorrent(torrentFile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTorrent(final String torrentId) throws HomeServerException {
		this.transmissionStreamer.getStreamableService().deleteTorrent(torrentId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unsubscribeToTorrentStream(final String id) {
		this.transmissionStreamer.unsuscribe(id);
	}

	@Override
	public List<RunningTorrent> getTorrents() throws HomeServerException {
		return this.transmissionStreamer.getStreamableService().getRunningTorrents();
	}

}
