package maroroma.homeserverng.tools.helpers.multicast;


/**
 * Interface de définition d'un agregator pour le multicast.
 * Spécialise l'interface {@link Consumer} pour un {@link MulticastResult}
 * @author RLEVEXIE
 *
 */
@FunctionalInterface
public interface MulticastAgregator {
	/**
	 * Méthode appelée à la fin de l'ensemble des traitements asynchrones.
	 * @param result -
	 */
	void accept(MulticastResult result);
}
