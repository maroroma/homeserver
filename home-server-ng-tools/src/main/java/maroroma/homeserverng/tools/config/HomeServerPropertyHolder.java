package maroroma.homeserverng.tools.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Holder pour une propriété applicative modifiable.
 * @author RLEVEXIE
 *
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"listeners", "resolvedValue", "resolver"})
public class HomeServerPropertyHolder {

	/**
	 * Taille attendue pour liste permettant de construire une dimension.
	 */
	private static final int DIMENSION_EXPECTED_SIZE = 2;

	/**
	 * Séparateur pour la conversion en dimension.
	 */
	private static final String DIMENSION_SEPARATOR = "x";

	/**
	 * Valeur par défault pour le split de chaine.
	 */
	private static final String DEFAULT_SPLIT_CHAR = ",";

	/**
	 * Identifiant de la propriété.
	 */
	private String id;
	
	/**
	 * Valeur de la propriété.
	 */
	private String value;
	
	/**
	 * Description de la propriété.
	 */
	private String description;
	
	/**
	 * Détermine si la propriété est en lecture seule. Par extension, va correspondre à une propriété
	 * dont la modificatio nécessite un redémmarage du serveur.
	 */
	private boolean readOnly;
	
	/**
	 * Identifiant du module associé à cette propriété.
	 */
	private String moduleId;
	
	/**
	 * Liste des {@link PropertySetterListener} à notifier en cas de modification des propriétés.
	 */
	private List<PropertySetterListener> listeners = new ArrayList<>();
	
	/**
	 * Permet de résoudre une valeur si elle dépend d'autres propriétés.
	 */
	private PropertyValueResolver resolver;
		
	/**
	 * Constructeur.
	 */
	@JsonCreator
	public HomeServerPropertyHolder() {
		this.listeners = new ArrayList<>();
	}
	
	/**
	 * Constructeur.
	 * @param idProp -
	 * @param valueProp -
	 */
	public HomeServerPropertyHolder(final String idProp, final String valueProp) {
		this();
		this.id = idProp;
		this.value = valueProp;
	}
	
	/**
	 * Valeur de la propriété.
	 * Si modification, appel des listeners de type {@link PropertySetterListener}.
	 * @param newValue -
	 */
	public void setValue(final String newValue) {
		this.value = newValue;
		if (this.listeners != null && !this.listeners.isEmpty()) {
			for (PropertySetterListener propertySetterListener : listeners) {
				propertySetterListener.onSetProperty(this);
			}
		}
	}

	
	/**
	 * Ajout d'un listener de type {@link PropertySetterListener}.
	 * @param listener -
	 */
	@JsonIgnore
	public void addSetEventHandler(final PropertySetterListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<>();
		}
		this.listeners.add(listener);
	}

	/**
	 * Retourne la propriété en tant que {@link File}.
	 * @return -
	 */
	public File asFile() {
		return new File(this.getResolvedValue());
	}
	
	/**
	 * Retourne la propriété en tant que {@link List} de {@link File}.
	 * @return -
	 */
	public List<File> asFileList() {
		List<File>  returnValue = new ArrayList<>();
		
		this.asStringList().forEach(oneFileName -> returnValue.add(new File(oneFileName)));
		
		return returnValue;
		
	}
	
	/**
	 * Retourne un chemin en tant que chemin du classpath.
	 * @return  -
	 */
	public String asClassPathPath() {
		return "file:" + this.getResolvedValue();
	}
	
	/**
	 * Retourne la valeur de la propriété en tant que {@link Integer}.
	 * @return -
	 */
	public int asInt() {
		return Integer.parseInt(this.getResolvedValue());
	}
	
	/**
	 * Split la valeur selon le caractère donné.
	 * @param splitChar -
	 * @return -
	 */
	public String[] asStringArray(final String splitChar) {
		return this.getResolvedValue().split(splitChar);
	}
	
	/**
	 * Split la valeur en se basant par défaut sur la ",".
	 * @return -
	 */
	public String[] asStringArray() {
		return asStringArray(DEFAULT_SPLIT_CHAR);
	}
	
	/**
	 * Split la valeur selon le caractère donné.
	 * @param splitChar -
	 * @return -
	 */
	public List<String> asStringList(final String splitChar) {
		return Arrays.asList(asStringArray(splitChar));
	}
	
	/**
	 * Split la valeur en se basant par défaut sur la ",".
	 * @return -
	 */
	public List<String> asStringList() {
		return asStringList(DEFAULT_SPLIT_CHAR);
	}
	
	/**
	 * Retourne la valeur en tant que booleen.
	 * @return -
	 */
	public boolean asBoolean() {
		return Boolean.parseBoolean(this.getResolvedValue());
	}
	
	/**
	 * Convertit la propriété en tuple d'entiers.
	 * @return -
	 */
	public Tuple<Integer, Integer> asDimension() {
		List<String> sizes = this.asStringList(DIMENSION_SEPARATOR);
		Assert.notEmpty(sizes, "La liste pour la conversion dans une dimension ne peut être vide");
		Assert.isTrue(sizes.size() == DIMENSION_EXPECTED_SIZE,
				"La liste pour la conversion dans une dimension doit avoir strictement 2 éléments");
		return Tuple.from(Integer.parseInt(sizes.get(0)), Integer.parseInt(sizes.get(1)));
	}
	
	/**
	 * Retourne la valeur interprée par rapport à un resolver.
	 * @return -
	 */
	public String getResolvedValue() {
		if (this.resolver != null) {
			return this.resolver.resolve(this.value);
		} else {
			return this.getValue();
		}
	}
	
}
