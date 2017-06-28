package maroroma.homeserverng.tools.directorymonitoring;

/**
 * {@link FunctionalInterface} permettant de définir des events listener lors de la levée
 * de {@link DirectoryEvent}.
 * @author rlevexie
 *
 */
@FunctionalInterface
public interface DirectoryEventListener {

	/**
	 * Appelée lorsqu'un {@link DirectoryEvent} est levé.
	 * @param event -
	 */
	void onEvent(DirectoryEvent event);
}
