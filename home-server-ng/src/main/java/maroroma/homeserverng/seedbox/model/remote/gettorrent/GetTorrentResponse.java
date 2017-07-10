package maroroma.homeserverng.seedbox.model.remote.gettorrent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Réponse de transmission pour le listing des torrents.
 * @author RLEVEXIE
 *
 */
@Data
@JsonIgnoreProperties("result")
public class GetTorrentResponse {
	/**
	 * .
	 */
	private ResponseArgument arguments;
}
