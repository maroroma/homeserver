package maroroma.homeserverng.seedbox.model.remote;

import org.springframework.util.Assert;

import lombok.Data;
import maroroma.homeserverng.tools.helpers.FluentMap;

/**
 * Requête de base pour les api transmissions.
 * @author rlevexie
 *
 */
@Data
public abstract class AbstractTransmissionRequest {

	/**
	 * Nom de la méthode.
	 */
	private String method;
	
	/**
	 * Liste des arguements.
	 */
	private FluentMap<String, Object> arguments;
	
	/**
	 * Constructeur.
	 * @param methodName -
	 */
	public AbstractTransmissionRequest(final String methodName) {
		Assert.hasLength(methodName, "methodName can't be null or empty");
		this.method = methodName;
		arguments = FluentMap.<String, Object>create();
	}
	
	
}
