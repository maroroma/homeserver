package maroroma.homeserverng.tools.cache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import lombok.Getter;

/**
 * Substitution pour la clef de base spring.
 * Elle permet une sérialisation de la clef pour manipulation des caches à travers les apis.
 * @author rlevexie
 *
 */
public class OpenSimpleStringsKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9203005468779465219L;


	/**
	 * Instance par défaut.
	 */
	public static final OpenSimpleStringsKey EMPTY = new OpenSimpleStringsKey();

	/**
	 * Liste des paramètres de la clef.
	 */
	@Getter
	private final String[] params;
	
	/**
	 * .
	 */
	private final int hashCode;
	
	
	/**
	 * Constrecteur.
	 * @param elements -
	 */
	public OpenSimpleStringsKey(final Object... elements) {
		Assert.notNull(elements, "Elements must not be null");

		// conversion de tous les éléments en string.
		this.params = Arrays.asList(elements).stream()
			.map(element -> element.toString())
			.collect(Collectors.toList())
			.toArray(new String[elements.length]);
		

		this.hashCode = Arrays.deepHashCode(this.params);
	}
	
	/**
	 * Retourne les clefs en tant que liste.
	 * @return -
	 */
	public List<String> asList() {
		return Arrays.asList(this.params);
	}
	
	@Override
	public boolean equals(final Object obj) {
		return (this == obj || (obj instanceof OpenSimpleStringsKey
				&& Arrays.deepEquals(this.params, ((OpenSimpleStringsKey) obj).params)));
	}

	@Override
	public final int hashCode() {
		return this.hashCode;
	}


}
