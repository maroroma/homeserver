package maroroma.homeserverng.tools.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

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
				() -> new ArrayList<T>(),
				// méthode utilisée pour ajouter des éléments dans le type de retour (agregator , BiConsumer<List<T>, List<T>>)
				(container, elementToAdd) -> container.addAll(elementToAdd),
				// méthode utilisée pour cumuler deux container. (binary operator, BinaryOperator<List<T>, List<T>>)
                (left, right) -> { 
                	left.addAll(right); 
                	return left; 
                	});
    }
	
	/**
	 * Collecte l'ensemble des données sous la forme d'un {@link String} concaténé.
	 * @return -
	 */
	public static 
    Collector<String, ?, StringBuilder> toConcatenatedString() {
		return Collector.of(
				// création du type de retour (supplier de type StringBuilder)
				() -> new StringBuilder(),
				// méthode utilisée pour ajouter des éléments dans le type de retour (agregator , BiConsumer<List<T>, List<T>>)
				(container, elementToAdd) -> container.append(elementToAdd),
				// méthode utilisée pour cumuler deux container. (binary operator, BinaryOperator<List<T>, List<T>>)
                (left, right) -> { 
                	left.append(right.toString()); 
                	return left; 
                	});
    }
	
	/**
	 * Retourne l'ensemble des données sous la forme d'une chaine concaténée avec un séparateur donné.
	 * @param separator -
	 * @return -
	 */
	public static 
    Collector<String, ?, StringBuilder> toConcatenatedString(final String separator) {
		return Collector.of(
				// création du type de retour (supplier de type StringBuilder)
				() -> new StringBuilder(),
				// méthode utilisée pour ajouter des éléments dans le type de retour (agregator , BiConsumer<List<T>, List<T>>)
				(container, elementToAdd) -> container.append(elementToAdd).append(separator),
				// méthode utilisée pour cumuler deux container. (binary operator, BinaryOperator<List<T>, List<T>>)
                (left, right) -> { 
                	left.append(right.toString()); 
                	return left; 
                	});
    }
	
}
