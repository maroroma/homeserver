package maroroma.homeserverng.seedbox.tools;

import maroroma.homeserverng.seedbox.model.RunningTorrent;
import maroroma.homeserverng.tools.transmission.model.GetTorrentResponse;
import maroroma.homeserverng.tools.transmission.model.Torrent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour la conversion des retours transmission vers le client web.
 * @author rlevexie
 *
 */
public abstract class TransmissionConverter {
	
	/** */
	private static final int PERCENT_PRODUCT = 100;

	
	/**
	 * Convertit un torrent retourné par transmission en DTO du homeserver.
	 * @param oneTorrent -
	 * @return -
	 */
	public static RunningTorrent convert(final Torrent oneTorrent) {
		return RunningTorrent.builder()
		.id(oneTorrent.getId())
		.done(oneTorrent.getSizeWhenDone() - oneTorrent.getLeftUntilDone())
		.name(oneTorrent.getName())
		.percentDone((int) (oneTorrent.getPercentDone() * PERCENT_PRODUCT))
		.total(oneTorrent.getTotalSize())
		// attention ici isFinished est pas forcément renseigné comme attendu.
		// on double le controle avec la taille restante
		.completed(oneTorrent.isFinished() || oneTorrent.getLeftUntilDone() == 0)
		.remaining(oneTorrent.getLeftUntilDone())
	.build();
	}
	
	/**
	 * Convertir une liste de torrent réceptionnée de puis transmission en liste retournable par le homeserver.
	 * @param response -
	 * @return -
	 */
	public static List<RunningTorrent> convert(final GetTorrentResponse response) {
		
		return response.getArguments().getTorrents().stream()
			.map(torrent -> TransmissionConverter.convert(torrent))
			.collect(Collectors.toList());
	}
	
}
