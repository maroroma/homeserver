package maroroma.homeserverng.music.tools;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.MimeTypeUtils;

import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;

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
	 * Permet de formatter le nom d'un albumart.
	 */
	public static final String ALBUM_ART_NAME_FORMAT =  ALBUM_ART_NAME_FILE_NAME + ".%s";
	
	/**
	 * Permet de formatter le nom d'un albumart pour le fichier folder.
	 */
	public static final String FOLDER_NAME_FORMAT = "folder.%s";
	
	/**
	 * PErmet de générer un {@link AlbumDescriptor} à partir d'un répertoire de travail.
	 * @param rawDirectory - répertoire de base à scanner
	 * @return -
	 */
	public static AlbumDescriptor createFromDirectory(final File rawDirectory) {
		Assert.notNull(rawDirectory, "rawDirectory can't be null or empty");
		Assert.isValidDirectory(rawDirectory);
		
		// récupération de l'artiste et de l'album en fonction du nom du répertoire
		String[] splitted = rawDirectory.getName().split(ALBUM_DIR_SEPARATOR);
		
		// création du builder
		AlbumDescriptor.AlbumDescriptorBuilder builder = AlbumDescriptor.builder()
		.albumName(splitted[1])
		.artistName(splitted[0])
		.directoryDescriptor(FileDirectoryDescriptor.createSimple(rawDirectory));
		
		// si album art présent, rajout
		File[] albumart = rawDirectory.listFiles(CommonFileFilter.fileNameWithoutExtensionIs(ALBUM_ART_NAME_FILE_NAME));
		
		if (albumart.length == 1) {
			builder.albumart(new FileDescriptor(albumart[0]));
		}
		
		
		return builder.build();
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
