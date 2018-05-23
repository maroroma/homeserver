package maroroma.homeserverng.tools.transmission.model;

import lombok.Data;

import java.util.List;

/**
 * Réponse de transmission pour le listing des torrents.
 * @author RLEVEXIE
 *
 */
@Data
public class GetTorrentResponseArgument {
	/**
	 * Liste des torrents en cours.
	 */
	private List<Torrent> torrents;
}
