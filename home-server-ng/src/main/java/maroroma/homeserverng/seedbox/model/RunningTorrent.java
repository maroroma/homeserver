package maroroma.homeserverng.seedbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Décrit un torrent en cours.
 * Porte les informations strictement nécessaires à un affichage simplifié du suivi d'un téléchargement de torrent.
 * @author rlevexie
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunningTorrent {
	
	/**
	 * Identifiant technique du torrent.
	 */
	private String id;
	
	/**
	 * Nom du torrent.
	 */
	private String name;
	/**
	 * données téléchargées.
	 */
	private long done;
	/**
	 * Données restantes à télécharger.
	 */
	private long remaining;
	/**
	 * Total des données à télécharger.
	 */
	private long total;
	
	/**
	 * Temps restant estimé.
	 */
	private String estimatedTime;
	
	/**
	 * Pourcentage accompli.
	 */
	private int percentDone;
	
	/**
	 * Détermine si le torrent est terminé.
	 */
	private boolean completed;
	
	
}
