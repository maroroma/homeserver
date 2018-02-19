package maroroma.homeserverng.tools.helpers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Extension de {@link ArrayList} permettant de proposer des méthodes d'ajout de type fluent
 * via la méthode {@link FluentList#addAnd(Object)}.
 * @author rlevexie
 *
 * @param <T> -
 */
public class FluentList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -688588696567947653L;

	/**
	 * Constructeur.
	 */
	public FluentList() {
		super();
	}

	/**
	 * Création d'une {@link FluentList}.
	 * @param <T> -
	 * @return -
	 */
	public static <T> FluentList<T> create() {
		return new FluentList<>();
	}

	/**
	 * Ajoute un item et retourne la liste mise à jour.
	 * @param item -
	 * @return -
	 */
	public FluentList<T> addAnd(final T item) {
		this.add(item);
		return this;
	}

	/**
	 * Ajoute une liste d'item et retourne la liste mise à jour.
	 * @param item -
	 * @return -
	 */
	public FluentList<T> addAllAnd(final Collection<? extends T> item) {
		this.addAll(item);
		return this;
	}

	/**
	 * Ajoute un tableau d'item et retourne la liste mise à jour.
	 * @param array -
	 * @return -
	 */
	public FluentList<T> addAllAnd(final T[] array) {
		if (array != null && array.length > 0) {
			for (T item : array) {
				this.add(item);
			}
		}
		return this;
	}

}
