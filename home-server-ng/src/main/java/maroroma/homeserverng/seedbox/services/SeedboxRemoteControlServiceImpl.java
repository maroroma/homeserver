package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.NewTorrents;
import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.seedbox.model.TorrentsToDelete;
import maroroma.homeserverng.seedbox.tools.SeedboxModuleConstants;
import maroroma.homeserverng.seedbox.tools.TransmissionConverter;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.transmission.methods.AddTorrent;
import maroroma.homeserverng.tools.transmission.methods.GetTorrent;
import maroroma.homeserverng.tools.transmission.methods.RemoveTorrent;
import maroroma.homeserverng.tools.transmission.methods.TransmissionClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Permet de piloter un client transmission dont les api web json ont été exposées.
 * @author RLEVEXIE
 *
 */
@Service
public class SeedboxRemoteControlServiceImpl {

	/**
	 * Url d'accès au api web transmission.
	 */
	@Property("homeserver.seedbox.transmission.url")
	private HomeServerPropertyHolder transmissionUrl;

	/**
	 * Emplacement todo
	 */
	@Property(SeedboxModuleConstants.HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY)
	private HomeServerPropertyHolder todoDirectory;

	/**
	 * Client transmission (il est de la portée du bean)
	 */
	private Optional<TransmissionClient> transmissionClient = Optional.empty();

	/**
	 * Retourne la liste des torrents en cours.
	 * @return -
	 * @throws HomeServerException -
	 */
	public List<RunningTorrent> getTorrents() throws HomeServerException {
		return GetTorrent
				.create()
				.build()
				// exec
				.execute(this.getClient())
				// récup réponse
				.getArguments()
				.getTorrents()
				.stream()
				// conversion et renvoi
				.map(TransmissionConverter::convert)
				.collect(Collectors.toList());
	}

	/**
	 * Rajout d'un nouveau torrent sur le serveur transmission
	 * @param newTorrent
	 * @throws HomeServerException
	 */
	public void addTorrent(final NewTorrents newTorrent) {
		// préparation des requêtes pour chacun des liens remontés
		newTorrent.getMagnetLinks()
				.stream()
				.map(oneMag -> AddTorrent
						.create()
						.magnetLink(oneMag)
						.downloadDir(this.todoDirectory.getResolvedValue())
						.build())
				// execution des requêtes
				.forEach(request -> request.execute(this.getClient()));
	}


	/**
	 * Conversion de la liste d'identifiants en requete transmission de suppression.
	 * @param torrentsToDelete -
	 */
	public void removeTorrents(TorrentsToDelete torrentsToDelete) {
		RemoveTorrent.create()
				.remove(torrentsToDelete.getIdsToDelete())
				.build()
				.execute(this.getClient());
	}

	/**
	 * Récupère le client.
	 * @return -
	 */
	private TransmissionClient getClient() {
		if(!this.transmissionClient.isPresent()) {
			this.initTransmissionClient();
		}
		return this.transmissionClient.get();
	}

	/**
	 * Init du client transmission. Sert aussi en cas d'update de la propriété.
	 */
	@PropertyRefreshHandlers("homeserver.seedbox.transmission.url")
	private void initTransmissionClient() {
		this.transmissionClient = Optional
				.of(new TransmissionClient(this.transmissionUrl.getResolvedValue()));
	}


}
