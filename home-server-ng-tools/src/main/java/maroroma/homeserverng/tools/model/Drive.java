package maroroma.homeserverng.tools.model;

import lombok.Builder;
import lombok.Data;

/**
 * Descripteur pour un lecteur /  montage.
 * @author RLEVEXIE
 *
 */
@Data
@Builder
public class Drive {

	/**
	 * Nom du lecteur.
	 */
	private String name;
	/**
	 * Taille totale.
	 */
	private Long totalSpace;
	/**
	 * Taille disponible.
	 */
	private Long freeSpace;
	/**
	 * Taille utilisée.
	 */
	private Long usedSpace;
	
	/**
	 * Pourcentage de libre sur le lecteur.
	 */
	private float percentageUsed;
	
}
