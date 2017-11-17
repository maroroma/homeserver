package maroroma.homeserverng.music.tools;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.MimeTypeUtils;

import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Regroupe les constantes utilisées pour la normalisation du nommage pour la gestion des tags et répertoire mp3.
 * <br /> fournit aussi des méthodes utilitaires pour la gestion des différents éléments du modèle utilisé.
 * @author rlevexie
 *
 */
public abstract class MusicTools {

	/**
	 * Extension pour le fichiers pngs.
	 */
	private static final String PNG_EXTENSION = "png";

	/**
	 * Séparateur artiste / album.
	 */
	public static final String ALBUM_DIR_SEPARATOR = " - ";

	/**
	 * Permet de formatter le nom d'un répertoire contenant un album ({@value}).
	 */
	public static final String ALBUM_DIR_NAME_FORMAT = "%s" + ALBUM_DIR_SEPARATOR + "%s";

	/**
	 * Nom sans extension d'un fichier albumart.
	 */
	public static final String ALBUM_ART_NAME_FILE_NAME = "albumart";

	/**
	 * Nom sans extension d'un fichier folder.
	 */
	public static final String FOLDER_FILE_NAME = "folder";

	/**
	 * Permet de formatter le nom d'un albumart.
	 */
	public static final String ALBUM_ART_NAME_FORMAT =  ALBUM_ART_NAME_FILE_NAME + ".%s";

	/**
	 * Permet de formatter le nom d'un albumart pour le fichier folder.
	 */
	public static final String FOLDER_NAME_FORMAT = FOLDER_FILE_NAME + ".%s";

	/**
	 * Supprime les fichiers d'albumart si déjà présents.
	 * @param rawDirectory -
	 */
	public static void removeExistingAlbumArt(final File rawDirectory) {
		Assert.notNull(rawDirectory, "rawDirectory can't be null or empty");
		Assert.isValidDirectory(rawDirectory);

		Arrays.stream(rawDirectory.listFiles(CommonFileFilter.fileNameWithoutExtensionIs(ALBUM_ART_NAME_FILE_NAME)))
		.forEach(oneFile -> oneFile.delete());

		Arrays.stream(rawDirectory.listFiles(CommonFileFilter.fileNameWithoutExtensionIs(FOLDER_FILE_NAME)))
		.forEach(oneFile -> oneFile.delete());
	}

	/**
	 * REtourne si existant le {@link FileDescriptor} correspondant à l'albumart.
	 * @param ad -
	 * @return -
	 */
	public static FileDescriptor extractAlbumArt(final AlbumDescriptor ad) {
		Assert.notNull(ad, "ad can't be null");
		Assert.isValidDirectory(ad.getDirectoryDescriptor());
		
		// si album art présent, rajout
		File[] albumart = ad.getDirectoryDescriptor().createFile().listFiles(CommonFileFilter.fileNameWithoutExtensionIs(ALBUM_ART_NAME_FILE_NAME));

		if (albumart.length == 1) {
			return new FileDescriptor(albumart[0]);
		}

		return null;
	}


	/**
	 * Récupération du type mime en fonction de l'extension du fichier pour insertion à terme dans les tags MP3.
	 * @param albumDescriptor -
	 * @return -
	 */
	public static String getMimeFromAlbumArt(final AlbumDescriptor albumDescriptor) {
		if (FilenameUtils.getExtension(albumDescriptor.getAlbumart().getName()).toLowerCase().contains(PNG_EXTENSION)) {
			return MimeTypeUtils.IMAGE_PNG_VALUE;
		} else {
			return MimeTypeUtils.IMAGE_JPEG_VALUE;
		}
	}
}
