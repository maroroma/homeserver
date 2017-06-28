package maroroma.homeserverng.tools.helpers;

import java.io.File;

import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Extension de {@link org.springframework.util.Assert} avec quelques méthodes supplémentaires.
 * @author rlevexie
 *
 */
public abstract class Assert extends org.springframework.util.Assert {

	
	/**
	 * Détermine si le {@link FileDescriptor} passé en paramètre est un dossier existant.
	 * @param file -
	 */
	public static void isValidDirectory(final FileDescriptor file) {
		Assert.notNull(file, "le fichier ne peut être null");
		Assert.isValidDirectory(file.createFile());
	}
	
	/**
	 * Détermine si le fichier passé en paramètre est un dossier existant.
	 * @param file -
	 */
	public static void isValidDirectory(final File file) {
		Assert.notNull(file, "le fichier ne peut être null");
		Assert.isTrue(file.exists(), file.getAbsolutePath() + " n'existe pas");
		Assert.isTrue(file.isDirectory(), file.getAbsolutePath() + " n'est pas un répertoire");
	}
	
	/**
	 * Valide le contenu d'une {@link HomeServerPropertyHolder}.
	 * @param property à tester.
	 */
	public static void isValidProperty(final HomeServerPropertyHolder property) {
		Assert.notNull(property, "property can't be null");
		Assert.hasLength(property.getId(), "property.id can't be null or empty");
		Assert.hasLength(property.getValue(), "property.value can't be null or empty");
	}
	
	/**
	 * Valide qu'une {@link HomeServerPropertyHolder} représente bien un répertoire existant.
	 * @param property à tester.
	 */
	public static void isValidDirectory(final HomeServerPropertyHolder property) {
		Assert.isValidProperty(property);
		File toTest = property.asFile();
		Assert.isTrue(toTest.exists(), "Le répertoire " 
				+ toTest.getAbsolutePath() + " n'existe pas. Vérifier la propriété applicative [" + property.getId() + "]");
		
		Assert.isTrue(toTest.isDirectory(), "Le répertoire " 
				+ toTest.getAbsolutePath() + " n'est pas un répertoire. Vérifier la propriété applicative [" + property.getId() + "]");
	}
	
	/**
	 * Détermine si le fichier passé en paramètre ets un fichier existant.
	 * @param file -
	 */
	public static void isValidFile(final File file) {
		Assert.notNull(file, "le fichier ne peut être null");
		Assert.isTrue(file.exists(), file.getAbsolutePath() + " n'existe pas");
		Assert.isTrue(file.isFile(), file.getAbsolutePath() + " n'est pas un fichier");
	}
	
	/**
	 * Détermine si le fichier passé en paramètre ets un fichier existant.
	 * @param file -
	 */
	public static void isValidFileOrDirectory(final File file) {
		Assert.notNull(file, "le fichier ne peut être null");
		Assert.isTrue(file.exists(), file.getAbsolutePath() + " n'existe pas");
	}
	
	/**
	 * Détermine si le fichier passé en paramètre ets un fichier existant.
	 * @param file -
	 */
	public static void isValidFileOrDirectory(final FileDescriptor file) {
		Assert.notNull(file, "le fichier ne peut être null");
		Assert.isValidFileOrDirectory(file.createFile());
	}
	
}
