package maroroma.homeserverng.network.tools;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import maroroma.homeserverng.network.model.UrlScanParameter;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Classe utilitaire pour la génération d'un {@link UrlScanParameter}.
 * @author rlevexie
 *
 */
@Component
public class UrlScanParameterHLP {
	
	/**
	 * Intervalle max.
	 */
	private static final int MAX_SCAN_RANGE = 10;

	/**
	 * Nombre de fragment IP exploitable pour la construction de l'ip.
	 */
	private static final int IP_SUBNETWORK_FRAGMENTS = 3;

	/**
	 * Valeur max pour la variabilisation de l'ip.
	 */
	private static final int IP_MAX_VALUE = 255;

	/**
	 * Permet de connaitre l'adresse du réseau local.
	 */
	@Property("homeserver.network.localnetwork.address.min")
	private HomeServerPropertyHolder localNetworkAddressMin;

	/**
	 * Permet de connaitre l'adresse du réseau local.
	 */
	@Property("homeserver.network.localnetwork.address.max")
	private HomeServerPropertyHolder localNetworkAddressMax;

	/**
	 * Définit le timeout de scan des adresses du réseau local.
	 */
	@Property("homeserver.network.localnetwork.scan.timeout")
	private HomeServerPropertyHolder scanTimeout;
	
	/**
	 * Extrait le dernier quart de l'adresse ip au format entier pour construire une borne de la boucle d'itération.
	 * @param property -
	 * @return -
	 * @throws HomeServerException -
	 */
	private int extractValue(final HomeServerPropertyHolder property) throws HomeServerException {
		String[] splitted = splitIp(property);
		int returnValue = 0;
		
		try {
			returnValue = Integer.parseInt(splitted[splitted.length - 1]);
		} catch (NumberFormatException nfe) {
			throw new HomeServerException("Paramètrage incorrect pour le scan des adresses IP. (vérifier "
					+ property.getId() 
					+ "). Les valeurs ne peuvent être résolues à des entiers", nfe);
		}
		
		Assert.isTrue(returnValue < IP_MAX_VALUE, property.getId() + " dispose d'une valeur trop importante");
		
		
		return returnValue;
	}

	/**
	 * Permet de spliter une ip à partir des "." .
	 * @param property -
	 * @return -
	 */
	private String[] splitIp(final HomeServerPropertyHolder property) {
		return property.getValue().split("\\.");
	}
	
	/**
	 * Construit la chaine ip qui sera incrémentée.
	 * <br /> valide aussi que les ip matchent entre les deux bornes.
	 * @return -
	 */
	private String validateAndBuildIpFormat() {
		
		StringBuilder returnValue = new StringBuilder();
		
		String[] splittedMin = this.splitIp(localNetworkAddressMin);
		String[] splittedMax = this.splitIp(localNetworkAddressMax);
		
		for (int i = 0; i < IP_SUBNETWORK_FRAGMENTS; i++) {
			Assert.isTrue(splittedMin[i].equals(splittedMax[i]), "Les adresses IP du sous réseau ne matchent pas (vérifier "
					+ localNetworkAddressMin.getId() 
					+ " ou "
					+ localNetworkAddressMax.getId()
					+ " digit "
					+ i
					+ ")");

			returnValue.append(splittedMin[i]).append(".");
		}
		
		return returnValue.toString();
	}
	
	/**
	 * Construit le {@link UrlScanParameter}.
	 * @return -
	 * @throws HomeServerException -
	 */
	public UrlScanParameter buildParameter() throws HomeServerException {

		// récup valeur min
		int minValue = this.extractValue(localNetworkAddressMin);
		
		// récup valeur max
		int maxValue = this.extractValue(localNetworkAddressMax);
		
		
		// validations diverses
		valideMinMaxSupZero(minValue, maxValue);

		validateMaxRange(minValue, maxValue);
		
		// récup du fragment d'ip qui sera incrémenté.
		String ipFormat = validateAndBuildIpFormat();

		// construction finale du parametre
		return UrlScanParameter.builder()
				.ipFragment(ipFormat)
				.minValue(minValue)
				.maxValue(maxValue)
				.timeout(this.scanTimeout.asInt()).build();
		
	}

	/**
	 * le range est limité pour ne pas tombé en thread starvation.
	 * @param minValue -
	 * @param maxValue -
	 */
	private void validateMaxRange(final int minValue, final int maxValue) {
		Assert.isTrue(maxValue - minValue <= MAX_SCAN_RANGE, "Paramètrage incorrect pour le scan des adresses IP. (vérifier "
				+ localNetworkAddressMin.getId() 
				+ " ou "
				+ localNetworkAddressMax.getId()
				+ "). Les valeurs couvrent une plage trop importante");
	}

	/**
	 * Valide que les valeurs de bornes présentent bien un intervalle non null.
	 * @param minValue -
	 * @param maxValue -
	 */
	private void valideMinMaxSupZero(final int minValue, final int maxValue) {
		Assert.isTrue(maxValue - minValue > 0, "Paramètrage incorrect pour le scan des adresses IP. (vérifier "
				+ localNetworkAddressMin.getId() 
				+ " ou "
				+ localNetworkAddressMax.getId()
				+ "). Les valeurs couvrent une plage incorrecte");
	}
}
