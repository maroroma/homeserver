package maroroma.homeserverng.tools.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	/**
	 * Retourne toutes les occurences d'un patterne donné.
	 * @param toScan -
	 * @param toFind -
	 * @return -
	 */
	public static List<Integer> findAllOccurencesIndex(final String toScan, final String toFind) {
		Assert.hasLength(toScan);
		Assert.hasLength(toFind);
		List<Integer> returnValue = new ArrayList<>();
		
		int startIndex = toScan.indexOf(toFind);
		while (startIndex != -1) {
			returnValue.add(startIndex);
			if (startIndex + 1 < toScan.length()) {
				startIndex = toScan.indexOf(toFind, startIndex + 1);
			} else {
				startIndex = -1;
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Retourne toutes les occurences répondant à un pattern donné exemple. Le caractère * doit être utilisé pour résoudre
	 * les patterns.
	 * @param toScan -
	 * @param pattern -
	 * @param includePattern -
	 * @return -
	 */
	public static List<String> findAllPatterns(final String toScan, final String pattern, final boolean includePattern) {
		
		String[] patterns = pattern.split("\\*");
		
		List<Integer> startIndex = findAllOccurencesIndex(toScan, patterns[0]);
		List<Integer> endIndex = findAllOccurencesIndex(toScan, patterns[1]);
		
		if (startIndex.isEmpty() || endIndex.isEmpty()) {
			return new ArrayList<>();
		}
		List<Tuple<Integer, Integer>> matchIndex = Tuple.fromLists(startIndex, endIndex);
		
		return matchIndex.stream().map(oneTuple -> {
			return toScan.substring(oneTuple.getItem1() + patterns[0].length(), oneTuple.getItem2());
		}).collect(Collectors.toList());
		
	}
}
