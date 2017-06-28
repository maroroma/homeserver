package maroroma.homeserverng.seedbox.model.remote.gettorrent;

import maroroma.homeserverng.seedbox.model.remote.AbstractTransmissionRequest;
import maroroma.homeserverng.tools.helpers.FluentList;

/**
 * Requete de récupération des torrents en cours.
 * @author RLEVEXIE
 *
 */
public final class GetTorrentRequest extends AbstractTransmissionRequest {

	/**
	 * Constructeur.
	 */
	private GetTorrentRequest() {
		super("torrent-get");
	}
	
	/**
	 * Retourne une requete de type récupération de torrent en cours.
	 * @return -
	 */
	public static GetTorrentRequest create() {
		GetTorrentRequest returnValue = new GetTorrentRequest();
		
		FluentList<String> parameters = FluentList.<String>create()
				.addAnd("id")
				.addAnd("name")
				.addAnd("totalSize")
				.addAnd("isFinished")
				.addAnd("leftUntilDone")
				.addAnd("percentDone")
				.addAnd("sizeWhenDone");
		
		returnValue.getArguments().add("fields", parameters);
		
		return returnValue;
	}

}
