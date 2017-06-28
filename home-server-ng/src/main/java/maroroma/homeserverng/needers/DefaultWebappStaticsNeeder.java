package maroroma.homeserverng.needers;

import org.springframework.beans.factory.annotation.Value;

import maroroma.homeserverng.tools.needers.StaticsNeeder;

/**
 * Permet de demander la définition d'un path http pour le service des fichiers statiques associés au pages de l'application web.
 * 
 * @author RLEVEXIE
 *
 */
//@Component
@Deprecated
public class DefaultWebappStaticsNeeder implements StaticsNeeder {

	/**
	 * Chemin d'exposition http pour les pages statiques.
	 */
	private static final String ROOT_RESOURCE_HANDLER = "/**";

	/**
	 * Emplacement des fichiers statiques.
	 */
	@Value("file:${homeserver.staticpath}")
	private String staticPath;
	
	@Override
	public String getHandler() {
		return ROOT_RESOURCE_HANDLER;
	}

	@Override
	public String getLocations() {
		return this.staticPath;
	}

}
