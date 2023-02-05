package maroroma.homeserverng.tools.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Permet de produire des {@link Collector} propre à l'application.
 * @author RLEVEXIE
 *
 */
public abstract class CustomCollectors {

	/**
	 * Permet de produire une liste unique à partir d'une collection de liste.
	 * @return -
	 * @param <T> type d'item porté par la collection produite par le collector.
	 */
	public static <T>
    Collector<List<T>, ?, List<T>> toAgregatedList() {
		return Collector.of(
				// création du type de retour (supplier de type List<T>)
				ArrayList::new,
				// méthode utilisée pour ajouter des éléments dans le type de retour (agregator , BiConsumer<List<T>, List<T>>)
				(container, elementToAdd) -> container.addAll(elementToAdd),
				// méthode utilisée pour cumuler deux container. (binary operator, BinaryOperator<List<T>, List<T>>)
                (left, right) -> { 
                	left.addAll(right); 
                	return left; 
                	});
    }
	
	public static <T> Collector<T, ?, Tuple<List<T>, List<T>>> toSplittedList(Function<T, Boolean> selector) {
		return Collector.of(
				() -> Tuple.from(new ArrayList<>(), new ArrayList<>()),
				(container, elementToAdd) -> {
					if (selector.apply(elementToAdd)) {
						container.getItem1().add(elementToAdd);
					} else {
						container.getItem2().add(elementToAdd);
					}
				},
				(left, right) -> {
					left.getItem1().addAll(right.getItem1());
					left.getItem2().addAll(right.getItem2());
					return left;
				});
	}

	public static <T> Collector<T, ?, FluentList<T>> toFluentList() {
		return Collector.of(
				// factory pour le type qu'on veut produire
				FluentList::new,
				ArrayList::add,
				FluentList::addAllAnd);
	}

}
