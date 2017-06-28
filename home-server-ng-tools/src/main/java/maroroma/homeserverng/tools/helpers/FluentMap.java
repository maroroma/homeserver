package maroroma.homeserverng.tools.helpers;

import java.util.HashMap;
import java.util.Properties;

/**
 * Extension de {@link HashMap} pour permettre une construction fluent.
 * <br /> Offre aussi une méthode utilitaire pour générer une instance de {@link Properties}.
 * @author RLEVEXIE
 *
 * @param <T> clef
 * @param <U> valeur
 */
public class FluentMap<T, U> extends HashMap<T, U> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4108445361607048807L;

	/**
	 * Constructeur.
	 */
	public FluentMap() {
		super();
	}
	
	/**
	 * Instancie la {@link FluentMap}.
	 * @param <T> clef
	 * @param <U> valeur
	 * @return -
	 */
	public static <T, U> FluentMap<T, U> create() {
		return new FluentMap<>();
	}
	
	/**
	 * Ajoute un élément à la {@link FluentMap}.
	 * @param key -
	 * @param value -
	 * @return -
	 */
	public FluentMap<T, U> add(final T key, final U value) {
		this.put(key, value);
		return this;
	}
	
	/**
	 * Génère un {@link Properties} à partir de cette map.
	 * @return -
	 */
	public Properties buildProperties() {
		Properties returnValue = new Properties();
		
		this.keySet().forEach(key -> returnValue.put(key, this.get(key)));
		
		return returnValue;
	}
	
	
}
