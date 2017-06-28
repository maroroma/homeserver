package maroroma.homeserverng.administration.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;

/**
 * Représente la réponse d'un import de propriétés.
 * @author RLEVEXIE
 *
 */
@Data
public class UploadPropertiesResponse {
	
	/**
	 * Liste des propriétés mises à jour.
	 */
	private List<HomeServerPropertyHolder> updatedProperties;
	
	/**
	 * Liste des propriétés en lecture seule.
	 */
	private List<HomeServerPropertyHolder> readOnlyProperties;
	
	/**
	 * Liste des propriétés importées et non trouvées.
	 */
	private List<HomeServerPropertyHolder> notFoundProperties;
	
	/**
	 * Constructeur.
	 */
	public UploadPropertiesResponse() {
		this.updatedProperties = new ArrayList<>();
		this.readOnlyProperties = new ArrayList<>();
		this.notFoundProperties = new ArrayList<>();
	}
}
