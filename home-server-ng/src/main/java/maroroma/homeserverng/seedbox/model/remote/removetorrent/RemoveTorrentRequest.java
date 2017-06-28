package maroroma.homeserverng.seedbox.model.remote.removetorrent;

import java.util.Arrays;

import maroroma.homeserverng.seedbox.model.remote.AbstractTransmissionRequest;

public class RemoveTorrentRequest extends AbstractTransmissionRequest {

	private RemoveTorrentRequest() {
		super("torrent-remove");
	}
	
	public static RemoveTorrentRequest create(String... id) {
		RemoveTorrentRequest returnValue = new RemoveTorrentRequest();
		
		returnValue.getArguments().add("ids", Arrays.asList(id));
		
		return returnValue;
	}

}
