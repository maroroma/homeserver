package maroroma.homeserverng.tools.helpers;

import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.streaming.input.UploadFile;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
	 * Détermine si le {@link FileDescriptor} passé en paramètre est ok
	 * @param fileDescriptor -
	 * @param extensions -
	 */
	@Deprecated
	public static void isValidFile(final FileDescriptor fileDescriptor, final String... extensions) {
		isValidFile(fileDescriptor.createFile(), extensions);
	}

	/**
	 * Détermine si le fichier passé en paramètre ets un fichier existant.
	 * @param file -
	 * @param extensions liste d'extension à valider si besoin;
	 */
	public static void isValidFile(final File file, final String... extensions) {
		Assert.notNull(file, "le fichier ne peut être null");
		Assert.isTrue(file.exists(), file.getAbsolutePath() + " n'existe pas");
		Assert.isTrue(file.isFile(), file.getAbsolutePath() + " n'est pas un fichier");

		// si une liste d'extension est fournie, on controle qu'au moins l'une d'entre elle correspond à celle
		// du fichier. Si pas de liste, pas de contrôles.
		if (extensions != null && extensions.length > 0) {
			Assert.hasValidExtension(file, extensions);
		}
	}

	/**
	 * Détermine si le nom de fichier dispose d'une des extensions en entrée.
	 * @param file -
	 * @param extensions -
	 */
	public static void hasValidExtension(final File file, final String... extensions) {
		Assert.hasValidExtension(file.getAbsolutePath(), extensions);
	}

	/**
	 * Détermine si le nom de fichier dispose d'une des extensions en entrée.
	 * @param file -
	 * @param extensions -
	 */
	public static void hasValidExtension(final UploadFile file, final String... extensions) {
		Assert.hasValidExtension(file.getFileName(), extensions);
	}

	/**
	 * Détermine si le nom de fichier dispose d'une des extensions en entrée.
	 * @param fileName -
	 * @param extensions -
	 */
	public static void hasValidExtension(final String fileName, final String... extensions) {
		Assert.isTrue(FileExtensionHelper.hasExtension(fileName, extensions), 
				"Le fichier  [" 
						+ fileName 
						+ "] ne correspond pas l'une des extensions suivantes :" 
						+ Arrays.stream(extensions).collect(Collectors.joining(";")));
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

	public static void isNotEmpty(final Collection<?> collection) {
		Assert.isTrue(!CollectionUtils.isEmpty(collection), "oollection can't be empty");
	}

}
