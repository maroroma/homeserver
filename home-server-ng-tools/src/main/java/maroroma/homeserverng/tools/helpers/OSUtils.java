package maroroma.homeserverng.tools.helpers;

import maroroma.homeserverng.tools.model.OSType;

/**
 * Classe utilitaire pour la gestion de la détection de l'os.
 * @author rlevexie
 *
 */
public abstract class OSUtils {

	/**
	 * Retourne le type d'OS déduit.
	 * @return -
	 */
	public static OSType getOs() {
		if (getRawOS().toLowerCase().contains("windows")) {
			return OSType.WINDOWS;
		} else {
			return OSType.UNIX;
		}
	}
	
	/**
	 * Retourne l'os tel que définit dans les propriétés JAVA.
	 * @return -
	 */
	public static String getRawOS() {
		return System.getProperty("os.name");
	}
	
}
