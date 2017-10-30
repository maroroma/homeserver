package maroroma.homeserverng.tools.helpers;

import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;

/**
 * Classe utilitaire pour la gestion des extensions.
 * @author rlevexie
 *
 */
public abstract class FileExtensionHelper {

	/**
	 * {@value}.
	 */
	public static final  String PNG = "png";

	/**
	 * {@value}.
	 */
	public static final String JPG = "jpg";

	/**
	 * {@value}.
	 */
	public static final String JPEG = "jpeg";

	/**
	 * {@value}.
	 */
	public static final String MP3 = "mp3";

	/**
	 * Détermine si le nom de fichier dispose d'une extension présente dans la liste donnée.
	 * @param fileName -
	 * @param extensions -
	 * @return -
	 */
	public static boolean hasExtension(final String fileName, final String... extensions) {

		Assert.notEmpty(extensions, "extensions can't be null or empty");

		// si une liste d'extension est fournie, on controle qu'au moins l'une d'entre elle correspond à celle
		// du fichier
		return Arrays.stream(extensions)
				.anyMatch(oneExtension -> oneExtension.toLowerCase().equals(FilenameUtils.getExtension(fileName)));
	}

}
