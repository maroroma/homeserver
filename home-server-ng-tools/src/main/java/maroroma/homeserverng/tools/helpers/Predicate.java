package maroroma.homeserverng.tools.helpers;

/**
 * Interface fonctionnelle permet d'appliquer des critères de recherche ou de comparaison sur un objet.
 * @author rlevexie
 *
 * @param <T>
 */
@FunctionalInterface
public interface Predicate<T> {

	/**
	 * Retourne true si item répond au prédicat.
	 * @param item item à test.
	 * @return -
	 */
	boolean accept(T item);
	
}
