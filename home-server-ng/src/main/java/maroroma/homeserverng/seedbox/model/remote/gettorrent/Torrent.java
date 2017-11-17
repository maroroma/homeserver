package maroroma.homeserverng.seedbox.model.remote.gettorrent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Description d'un torrent remonté par transmission.
 * @author RLEVEXIE
 *
 */
@Data
/*
 * "addedDate" : 1479760552,
				"error" : 0,
				"errorString" : "",
				"eta" : 109641,
				"id" : 11,
				"isFinished" : false,
				"isStalled" : false,
				"leftUntilDone" : 548208640,
				"metadataPercentComplete" : 1,
				"name" : "test.The.Walking.Dead.S07E05.VOSTFR.HDTV.XviD-ZT.www.torrent9.biz.avi",
				"peersConnected" : 9,
				"peersGettingFromUs" : 3,
				"peersSendingToUs" : 4,
				"percentDone" : 0.0336,
				"queuePosition" : 0,
				"rateDownload" : 5000,
				"rateUpload" : 3000,
				"recheckProgress" : 0,
				"seedRatioLimit" : 2,
				"seedRatioMode" : 0,
				"sizeWhenDone" : 567274430,
				"status" : 4,
				"totalSize" : 567274430,
				"trackers" : [{
 */
@JsonIgnoreProperties({"addedDate", "error", "errorString", "eta", "isStalled", "metadataPercentComplete", "peersConnected",
		"peersGettingFromUs", "peersSendingToUs", "queuePosition", "rateDownload", "rateUpload", "recheckProgress",
		"seedRatioLimit", "seedRatioMode",
		"status", "trackers", "uploadRatio", "uploadedEver" })
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
