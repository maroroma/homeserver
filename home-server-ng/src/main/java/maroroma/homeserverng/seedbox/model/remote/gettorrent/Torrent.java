package maroroma.homeserverng.seedbox.model.remote.gettorrent;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Description d'un torrent remonté par transmission.
 * @author RLEVEXIE
 *
 */
@Data
public class Torrent {
	/**
	 * Identifiant du torrent.
	 */
	private String id;
	/**
	 * Nom du torrent.
	 */
	private String name;
	/**
	 * Taille totale du torrent.
	 */
	private long totalSize;
	
	/**
	 * Le torrent est-il terminé ?
	 */
	@JsonProperty("isFinished")
	private boolean finished;
	
	/**
	 * Bytes restants à télécharger.
	 */
	private long leftUntilDone;
	
	/**
	 * Pourcentage fait.
	 */
	private double percentDone;
	
	/**
	 * Taille complète.
	 */
	private long sizeWhenDone;
}
