package maroroma.homeserverng.photo.tools;

import java.io.File;
import java.time.LocalDate;

import org.springframework.util.Assert;

/**
 * Classe utilitaire pour le parsing des nom de fichier en date correspondante.
 * @author rlevexie
 *
 */
public abstract class PhotoNamesDateHLP {

	/**
	 * Message d'erreur standard pour le controle des entrée.
	 */
	private static final String FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG = "fileToParse can't be null";


	/**
	 * Préfixe pour les noms de miniatures.
	 */
	private static final String PREFIX_THUMB = "THUMB";
	
	
	/**
	 * Sépararteur utilisé par défaut dans les noms de dossier.
	 */
	private static final String SEPARATOR_DASH = "-";

	/**
	 * Permet de parser un nom de fichier pour construire une date limitée à une année.
	 * @param fileToParse -
	 * @return -
	 */
	public static LocalDate parseFileNameToLocalDateForYear(final File fileToParse) {
		Assert.notNull(fileToParse, FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG);
		return LocalDate.of(Integer.parseInt(fileToParse.getName()), 1, 1);
	}
	
	/**
	 * Permet de parser un nom de fichier pour construire une date limitée à une année et un mois.
	 * @param fileToParse -
	 * @return -
	 */
	public static LocalDate parseFileNameToLocalDateForMonth(final File fileToParse) {
		Assert.notNull(fileToParse, FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG);
		String[] splitted = splitFileName(fileToParse);
		return LocalDate.of(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), 1);
	}
	
	/**
	 * Permet de parser un nom de fichier pour construire une date limitée à une année, un mois et un jour.
	 * @param fileToParse -
	 * @return -
	 */
	public static LocalDate parseFileNameToLocalDateForDay(final File fileToParse) {
		Assert.notNull(fileToParse, FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG);
		String[] splitted = splitFileName(fileToParse);
		return LocalDate.of(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
	}

	/**
	 * Permet de séparer les noms de fichiers en fonction des {@link PhotoNamesDateHLP#SEPARATOR_DASH}.
	 * @param fileToParse -
	 * @return -
	 */
	private static String[] splitFileName(final File fileToParse) {
		return fileToParse.getName().split(SEPARATOR_DASH);
	}
	
	/**
	 * Extrait le numéro de mois à partir du nom d'un répertoire de type mois (YYYY-MM).
	 * @param fileToParse -
	 * @return -
	 */
	public static int getMonth(final File fileToParse) {
		Assert.notNull(fileToParse, FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG);
		String[] splitted = splitFileName(fileToParse);
		return Integer.parseInt(splitted[1]);
	}
	
	/**
	 * Extrait le numéro de jour à partir du nom d'un répertoire de type mois (YYYY-MM-dd).
	 * @param fileToParse -
	 * @return -
	 */
	public static int getDay(final File fileToParse) {
		Assert.notNull(fileToParse, FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG);
		String[] splitted = splitFileName(fileToParse);
		return Integer.parseInt(splitted[2]);
	}
	
	/**
	 * Extrait le numéro de l'année à partir du nom d'un répertoire de type mois (YYYY-MM-dd).
	 * @param fileToParse -
	 * @return -
	 */
	public static int getYear(final File fileToParse) {
		Assert.notNull(fileToParse, FILE_TO_PARSE_CAN_T_BE_NULL_ASSERT_MSG);
		String[] splitted = splitFileName(fileToParse);
		return Integer.parseInt(splitted[0]);
	}

	/**
	 * Permet de générer un nom de miniature en fonction du nombre de champs.
	 * @param fields -
	 * @return -
	 */
	public static String generateThumbName(final int... fields) {
		StringBuilder returnValue = new StringBuilder(PREFIX_THUMB)
				.append(SEPARATOR_DASH);

		for (int oneField : fields) {
			returnValue.append(oneField).append(SEPARATOR_DASH);
		}
		
		return returnValue.toString();
		
	}
	
}
