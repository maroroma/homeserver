package maroroma.homeserverng.tools.helpers;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe utilitaire pour la gestion d'un tuple.
 * @author RLEVEXIE
 *
 * @param <T> type du premier item
 * @param <U> type du deuxième item
 */
@Data
@Builder
@NoArgsConstructor
public final class Tuple<T, U> {
	/**
	 * Item 1.
	 */
	private T item1;
	
	/**
	 * Item 2.
	 */
	private U item2;
	
	/**
	 * Constructeur.
	 * @param i1 -
	 * @param i2 -
	 */
	private Tuple(final T i1, final U i2) {
		this.item1 = i1;
		this.item2 = i2;
	}
	
	/**
	 * Génère un tuple pour les valeurs données.
	 * @param i1 -
	 * @param i2 -
	 * @param <T> -
	 * @param <U> -
	 * @return -
	 */
	public static <T, U> Tuple<T, U> from(final T i1, final U i2) {
		return new Tuple<T, U>(i1, i2);
	}
}
