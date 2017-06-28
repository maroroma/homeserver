package maroroma.homeserverng.tools.needers;

/**
 * Interface de définition pour définir un besoin en définition de chemin http statique.
 * @author RLEVEXIE
 *
 */
public interface StaticsNeeder {

	/**
	 * Retourne le path http par lequel les locations retournés par {@link StaticsNeeder#getLocations()} seront exposées.
	 * @return -
	 */
	String getHandler();
	
	/**
	 * Emplacement physique des resources par lequel {@link StaticsNeeder#getHandler()} permet d'exposer en tant que resources http.
	 * @return -
	 */
	String getLocations();
	
}
