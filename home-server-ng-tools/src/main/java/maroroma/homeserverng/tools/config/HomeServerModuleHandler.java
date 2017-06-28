package maroroma.homeserverng.tools.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;

/**
 * DTO utilisé pour le listing des modules.
 * @author RLEVEXIE
 *
 */
@Data
public class HomeServerModuleHandler {

	/**
	 * Identifiant du module.
	 */
	private String moduleId;
	
	/**
	 * Le module est-il activé ?
	 */
	private boolean enabled;
	
	/**
	 * Description du module.
	 */
	private String moduleDescription;
	
	/**
	 * Nom affiché pour le module.
	 */
	private String displayName;
	
	/**
	 * Css appliquée pour menu.
	 */
	private String cssMenu;
	
	/**
	 * Code présent coté serveur pour le module.
	 */
	private boolean hasServerSide;
	
	/**
	 * Code présent coté ihm pour le module.
	 */
	private boolean hasClientSide;
	
	/**
	 * fichier de properties correspondant.
	 */
	private String propertiesFile;
	
	/**
	 * Liste des endpoints exposés par le module.
	 */
	private List<String> endpoints;
	
	/**
	 * Détermine si le module peut être modifié.
	 */
	private boolean isReadOnly = false;
	
	/**
	 * Constructeur.
	 * @param input annotation dont on recopie les valeurs.
	 */
	public HomeServerModuleHandler(final HomeServerModuleDescriptor input) {
		this();
		this.moduleId = input.moduleId();
		this.moduleDescription = input.moduleDescription();
		this.cssMenu = input.cssMenu();
		this.displayName = input.displayName();
		this.hasClientSide = input.hasClientSide();
		this.hasServerSide = input.hasServerSide();
		this.propertiesFile = input.propertiesFile();
		this.isReadOnly = input.isReadOnly();
		// si en lecture, activé par défaut
		this.enabled = this.isReadOnly;
	}
	
	/**
	 * Constructeur.
	 */
	public HomeServerModuleHandler()  {
		this.endpoints = new ArrayList<>();
	}
	
	/**
	 * Permet d'ajouter un endpoint à la liste des endpoints du module.
	 * @param endpoint -
	 * @return -
	 */
	public HomeServerModuleHandler addEndpoint(final String endpoint) {
		if (this.endpoints == null) {
			this.endpoints = new ArrayList<>();
		}
		if (!this.endpoints.contains(endpoint)) {
			this.endpoints.add(endpoint);
		}
		return this;
	}
	
	/**
	 * Permet d'ajouter un endpoint à la liste des endpoints du module.
	 * @param endpointss -
	 * @return -
	 */
	public HomeServerModuleHandler addEndpoint(final String[] endpointss) {
		for (String string : endpointss) {
			this.addEndpoint(string);
		}
		return this;
	}
	
	/**
	 * Permet de nettoyer la liste des endpoints.
	 */
	public void clearEndpoints() {
		this.endpoints.clear();
	}
	
}
