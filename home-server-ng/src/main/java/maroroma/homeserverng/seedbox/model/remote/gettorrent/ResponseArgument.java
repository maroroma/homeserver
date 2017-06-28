package maroroma.homeserverng.seedbox.model.remote.gettorrent;

import java.util.List;

import lombok.Data;

/**
 * Réponse de transmission pour le listing des torrents.
 * @author RLEVEXIE
 *
 */
@Data
public class ResponseArgument {
	/**
	 * Liste des torrents en cours.
	 */
	private List<Torrent> torrents;
}
