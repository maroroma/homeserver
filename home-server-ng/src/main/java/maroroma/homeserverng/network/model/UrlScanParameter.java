package maroroma.homeserverng.network.model;

import lombok.Builder;
import lombok.Data;

/**
 * Permet de regrouper l'ensemble des paramètres utilisés pour un scan d'adresse IP.
 * @author rlevexie
 *
 */
@Data
@Builder
public class UrlScanParameter {
	/**
	 * Min value pour le scan.
	 */
	private int minValue;
	/**
	 * Max value pour le scan.
	 */
	private int maxValue;
	
	/**
	 * Adresse IP à incrémenter.
	 */
	private String ipFragment;
	
	/**
	 * Timeout pour les tests de communication.
	 */
	private int timeout;
	
	/**
	 * Calcule le temps d'attente max pour l'ensemble des appels.
	 * @return -
	 */
	public int getMaxAwaitTime() {
		return this.timeout * (this.maxValue - this.minValue);
	}
}
