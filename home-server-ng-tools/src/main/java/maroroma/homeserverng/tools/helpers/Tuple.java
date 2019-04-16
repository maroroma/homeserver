package maroroma.homeserverng.tools.helpers;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
public class Tuple<T, U> {
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
	protected Tuple(final T i1, final U i2) {
		this.item1 = i1;
		this.item2 = i2;
	}

	public <V,W> Tuple<V,W> map(Function<T, V> item1Mapper, Function<U, W> item2Mapper) {
		return Tuple.from(item1Mapper.apply(this.getItem1()), item2Mapper.apply(this.getItem2()));
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
	
	/**
	 * Retourne une liste de {@link Tuple} à partir de deux listes.
	 * @param list1 -
	 * @param list2 -
	 * @return -
	 * @param <T> premier type de la liste
	 * @param <U> deuxième type de la liste
	 */
	public static <T, U> List<Tuple<T, U>> fromLists(final List<T> list1, final List<U> list2) {
		Assert.notEmpty(list1, "list1 ne peut être vide");
		Assert.notEmpty(list2, "list2 ne peut être vide");
		Assert.isTrue(list1.size() == list2.size(), "Les deux listes doivent faire la même taille");
		List<Tuple<T, U>> returnValue = new ArrayList<>();
		for (int i = 0; i < list1.size(); i++) {
			returnValue.add(Tuple.from(list1.get(i), list2.get(i)));
		}
		return returnValue;
	}
}
