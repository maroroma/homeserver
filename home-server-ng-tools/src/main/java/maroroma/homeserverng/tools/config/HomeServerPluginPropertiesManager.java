package maroroma.homeserverng.tools.config;

import java.util.List;

import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Interface de définition d'un gestionnaire des propriétés dynamiques (des plugins) du serveur.
 * @author RLEVEXIE
 *
 */
public interface HomeServerPluginPropertiesManager {
	/**
	 * Permet de lister les properties des plugins.
	 * @return l'ensemble des propriétés des plugins.
	 * @throws HomeServerException -
	 */
	List<HomeServerPropertyHolder> getProperties() throws HomeServerException;

	/**
	 * Mise à jour d'un propriété du server.
	 * @param id identifiant de la propriété
	 * @param newValue propriété dans son nouvel état
	 */
	void updateProperty(String id, HomeServerPropertyHolder newValue);

	/**
	 * Retourne une propriété d'identifiant donné.
	 * @param propertyName nom de la propriété à récupérer
	 * @return -
	 */
	HomeServerPropertyHolder getPropertyHolder(final String propertyName);
	
}
