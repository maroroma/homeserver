package maroroma.homeserverng.tools.transmission.model;

import lombok.Data;

/**
 * Réponse de transmission pour le listing des torrents.
 * @author RLEVEXIE
 *
 */
@Data
public class GetTorrentResponse extends BasicResponse {
	/**
	 * .
	 */
	private GetTorrentResponseArgument arguments;

}
