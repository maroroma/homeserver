package maroroma.homeserverng.network.model;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Construit la liste d'adresses à tester en fonction du fragment d'ip et des valeus min et max retenue.
	 * @return -
	 */
	public List<String> getAddressesToTest() {
		
		List<String> returnValue = new ArrayList<>();
		for (int i = this.getMinValue(); i < this.getMaxValue(); i++) {
			returnValue.add(this.getIpFragment() + i);
		}
		
		return returnValue;
	}
}
