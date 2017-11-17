package maroroma.homeserverng.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.config.PropertySetterListener;
import maroroma.homeserverng.tools.config.PropertyValueResolver;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;

/**
 * Définition d'une source de propriété customisée, permettant de charger les {@link HomeServerPropertyHolder}, 
 * propriétés modifiables à chaud au niveau du homeserver.
 * @author RLEVEXIE
 *
 */
@Log4j2
public class HomeServerPropertiesSource implements PropertySetterListener {

	/**
	 * Listing des propriétés, associées à leur identifiants.
	 */
	private Map<String, HomeServerPropertyHolder> propertiesMap;
	
	/**
	 * Détermine si la propriété d'identifiant donné est présente dans la liste de propriété.
	 * @param propertyName nom de la propriété à tester.
	 * @return -
	 */
	public boolean containsProperty(final String propertyName) {
		return this.propertiesMap.containsKey(propertyName) || this.propertiesMap.containsKey(escapePropertyName(propertyName));
	}
	
	/**
	 * Retourn le nom de la propriété au format standard.
	 * <br /> remplace les tirets par des points.
	 * @param propertyName propriété à échapper
	 * @return -
	 */
	protected String escapePropertyName(final String propertyName) {
		return propertyName.replace("-", ".");
	}
	
	/**
	 * Retourne le {@link HomeServerPropertyHolder} d'identifiant donné.
	 * @param propertyName - 
	 * @return -
	 */
	public HomeServerPropertyHolder getProperty(final String propertyName) {
		HomeServerPropertyHolder returnValue = this.propertiesMap.get(propertyName);
		if (returnValue == null) {
			returnValue = this.propertiesMap.get(escapePropertyName(propertyName));
		}
		return returnValue;
	}
	
	/**
	 * Retourne la liste des propriétés de la source.
	 * @return -
	 */
	@JsonIgnore
	public List<HomeServerPropertyHolder> getProperties() {
		return new ArrayList<>(this.propertiesMap.values());
	}
	
	
	/**
	 * Nom du fichier à charger.
	 */
	private String fileName;
	
	/**
	 * Nom du module associé à cette source de propriété.
	 */
	private String moduleName;
	
	/**
	 * Constructeur.
	 * @param filePath nom du fichier de properties à charger.
	 * @param module nom du module associé à cette source.
	 */
	public HomeServerPropertiesSource(final String filePath, final String module) {
		this.fileName = filePath;
		this.moduleName = module;
	}

	/**
	 * Lance le chargement du fichier pour extraire les {@link HomeServerPropertyHolder}.
	 * @throws HomeServerException -
	 */
	public void loadPropertiesSource() throws HomeServerException {
		File configFile = new File(this.fileName);
		Assert.isValidFile(configFile);
		
		ObjectMapper deserializer = new ObjectMapper();
		
		List<HomeServerPropertyHolder> properties = null;
		try {
			properties = deserializer.readValue(
					configFile,
					deserializer.getTypeFactory().constructCollectionType(List.class, HomeServerPropertyHolder.class));
		} catch (IOException e) {
			throw new HomeServerException("Une erreur est survenue lors du chargement du fichier ", e);
		}
		
		this.propertiesMap = new HashMap<String, HomeServerPropertyHolder>();
		
		// abonnement aux modification de la propriété pour forcer une sauvegarde sur chaque modification
		for (HomeServerPropertyHolder homeServerPropertyHolder : properties) {
			homeServerPropertyHolder.addSetEventHandler(this);
			homeServerPropertyHolder.setModuleId(this.moduleName);
			this.propertiesMap.put(homeServerPropertyHolder.getId(), homeServerPropertyHolder);
			
			// on en profite pour tracer les properties
			log.info("[homeserverconfig] - " + homeServerPropertyHolder.getId() + " : [" + homeServerPropertyHolder.getValue() + "]");			
		}
		
	}

	/**
	 * Sauvegarde de la liste dans le fichier initial.
	 */
	public void save() {
		ObjectMapper serializer = new ObjectMapper();
		File configFile = new File(this.fileName);
		Assert.isValidFile(configFile);
		
		try {
			serializer.writerWithDefaultPrettyPrinter().writeValue(configFile, this.propertiesMap.values());
		} catch (IOException e) {
			log.warn("Erreur rencontrée lors de la sauvegarde du fichier [" + this.fileName + "]", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSetProperty(final HomeServerPropertyHolder homeServerPropertyHolder) {
		// sur une modification de la propriété, enregistrement du fichier
		this.save();
	}
	
	/**
	 * Application d'un {@link PropertyValueResolver} à l'ensemble des property holder.
	 * @param resolver -
	 */
	public void applyResolver(final PropertyValueResolver resolver) {
		Assert.notNull(resolver, "resolver can't be null");
		this.propertiesMap.values().forEach(oneProp -> oneProp.setResolver(resolver));
	}
	
}
