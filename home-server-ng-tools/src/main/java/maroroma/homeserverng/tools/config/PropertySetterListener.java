package maroroma.homeserverng.tools.config;

/**
 * Interface de définition d'un listener de modification de propriété de type {@link HomeServerPropertyHolder}
 * .
 * @author RLEVEXIE
 *
 */
@FunctionalInterface
public interface PropertySetterListener {

	/**
	 * Méthode appelée lorsqu'un {@link HomeServerPropertyHolder} voit sa valeur modifiée.
	 * @param homeServerPropertyHolder -
	 */
	void onSetProperty(HomeServerPropertyHolder homeServerPropertyHolder);
	
}
