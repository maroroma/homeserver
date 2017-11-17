package maroroma.homeserverng.tools.config;

/**
 * Interface utilisée par un {@link HomeServerPropertyHolder} pour résoudre dynamiquement ses variables.
 * @author rlevexie
 *
 */
@FunctionalInterface
public interface PropertyValueResolver {

	/**
	 * Retourne la chaine résolue.
	 * @param rawValue 
	 * @return -
	 */
	String resolve(String rawValue);
	
}
