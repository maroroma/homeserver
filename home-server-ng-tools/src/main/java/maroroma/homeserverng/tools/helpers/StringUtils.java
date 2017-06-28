package maroroma.homeserverng.tools.helpers;


/**
 * Classe utilitaire pour la manipulation des {@link String}.
 * @author rlevexie
 *
 */
public abstract class StringUtils extends org.springframework.util.StringUtils {

	/**
	 * .
	 */
	private static final int RADIX = 10;

	/**
	 * Détermine si la chaine est un entier.
	 * @param s -
	 * @return -
	 */
	public static boolean isInteger(final String s) {
		return isInteger(s, RADIX);
	}

	/**
	 * Détermine si la chaine est un entier.
	 * @param s -
	 * @param radix -
	 * @return -
	 */
	public static boolean isInteger(final String s, final int radix) {
		if (s.isEmpty()) { 
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1) { 
					return false; 
				} else {
					continue;
				}
			}
			if (Character.digit(s.charAt(i), radix) < 0) {
				return false;
			}
		}
		return true;
	}
}
