package maroroma.homeserverng.tools.helpers;


/**
 * Classe utilitaire pour le rajout de méthode mathématiques.
 * @author RLEVEXIE
 *
 */
public abstract class CustomMath {
	
	/**
	 * Calcul de pourcentage.
	 */
	private static final int PERCENT_PRODUCT = 100;

	/**
	 * Retourne l'équivalent en pourcent de la part par rapport au total.
	 * @param part -
	 * @param total -
	 * @return -
	 */
	public static float percent(final long part, final long total) {
		return percent((float) part / total);
	}
	
	/**
	 * Multiplie par cent.
	 * @param noPercent -
	 * @return -
	 */
	public static float percent(final float noPercent) {
		return noPercent * PERCENT_PRODUCT;
	}
}
