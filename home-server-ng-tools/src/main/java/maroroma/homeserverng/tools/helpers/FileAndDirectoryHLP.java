package maroroma.homeserverng.tools.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Classe utilitaire pour la manipulation de répertoires et de fichiers.
 * @author rlevexie
 *
 */
@Log4j2
public abstract class FileAndDirectoryHLP {

	/**
	 * Suppression recursive d'un ensemble de {@link File}. Si le {@link File} est un répertoire, le nettoye de manière récursive.
	 * @param genericFilesToDelete -
	 */
	public static void deleteGenericFile(final File... genericFilesToDelete) {

		List<File> files = Arrays.asList(genericFilesToDelete);

		// tentative d'accélaration via du // stream
		files.parallelStream().forEach(fileToDelete -> {
			if (fileToDelete.isDirectory()) {
				File[] subFiles = fileToDelete.listFiles();
				if (subFiles != null && subFiles.length > 0) {
					deleteGenericFile(subFiles);
				}
				fileToDelete.delete();
			} else {
				fileToDelete.delete();
			}
		});

	}

	/**
	 * Permet de supprimer un ensemble de fichier de manières récursives.
	 * @param genericFilesToDelete liste des fichiers à supprimer
	 * @return mapping nom de fichier / status de suppression
	 */
	public static Map<File, Boolean> deleteGenericFileWithStatus(final File... genericFilesToDelete) {

		List<File> files = Arrays.asList(genericFilesToDelete);
		
		// liste des status à retourner.
		Map<File, Boolean> returnValue = new ConcurrentHashMap<File, Boolean>();

		// tentative d'accélaration via du // stream
		files.parallelStream().forEach(fileToDelete -> {
			if (fileToDelete.isDirectory()) {
				File[] subFiles = fileToDelete.listFiles();
				if (subFiles != null && subFiles.length > 0) {
					// rajout de la sous liste dans la liste globale
					returnValue.putAll(deleteGenericFileWithStatus(subFiles));
				}
			}
			
			// rajout du fichier, que ce soit un répertoire ou un fichier simple.
			returnValue.put(fileToDelete, fileToDelete.delete());

		});
		
		return returnValue;

	}

	/**
	 * Convertit le fichier donné en tableau de byte.
	 * @param toConvert -
	 * @return -
	 * @throws HomeServerException -
	 */
	public static byte[] convertFileToByteArray(final File toConvert) throws HomeServerException {
		Assert.isValidFile(toConvert);
		InputStream is = null;
		byte[] returnValue = null;

		try {
			is = Files.newInputStream(toConvert.toPath());
			returnValue = StreamUtils.copyToByteArray(is);

		} catch (IOException e) {
			String msg = "Erreur rencontrée lors de la lecture de l'image " + toConvert.getAbsolutePath();
			log.error(msg, e);
			throw new HomeServerException(msg, e);
		} finally {
			StreamHLP.safeClose(is);
		}

		return returnValue;

	}


	/**
	 * Permet de recopier un {@link MultipartFile} dans un fichier local.
	 * @param input -
	 * @param output -
	 * @throws HomeServerException -
	 */
	public static void convertByteArrayToFile(final MultipartFile input, final File output) throws HomeServerException {

		Assert.notNull(input, "bytes can't be null");
		Assert.notNull(output, "output can't be null");

		try {
			FileAndDirectoryHLP.convertByteArrayToFile(input.getBytes(), output);
		} catch (IOException e) {
			String msg = "Erreur lors du remplacement du fichier " + output.getAbsolutePath();
			log.warn(msg, e);
			throw new HomeServerException(msg, e);
		}
	}


	/**
	 * Permet de convertir un tableau de byte en fichier.
	 * @param bytes -
	 * @param output -
	 * @throws HomeServerException -
	 */
	public static void convertByteArrayToFile(final byte[] bytes, final File output) throws HomeServerException {

		Assert.notNull(bytes, "bytes can't be null");
		Assert.notNull(output, "output can't be null");

		try {
			FileCopyUtils.copy(bytes, output);
		} catch (IOException e) {
			String msg = "Erreur lors du remplacement du fichier " + output.getAbsolutePath();
			log.warn(msg, e);
			throw new HomeServerException(msg, e);
		}
	}

	/**
	 * Retourne le chemin complet d'un fichier en fonction de son hash.
	 * @param fileId -
	 * @return -
	 */
	public static String decodeFileName(final String fileId) {
		Assert.hasLength(fileId, "fileId can't be null or empty");
		return new String(Base64Utils.decodeFromString(fileId));
	}

	/**
	 * Retourne le {@link File} correspondant au hash de fichier.
	 * @param fileId -
	 * @return -
	 */
	public static File decodeFile(final String fileId) {
		return new File(decodeFileName(fileId));
	}

	/**
	 * Retourne le {@link FileDescriptor} correspondant au hash de fichier.
	 * @param fileId -
	 * @return -
	 */
	public static FileDescriptor decodeFileDescriptor(final String fileId) {
		return new FileDescriptor(decodeFile(fileId));
	}

	/**
	 * Détermine si child est un répertoire fils de parent.
	 * @param parent -
	 * @param child -
	 * @return -
	 */
	public static boolean isParentOf(final File parent, final File child) {
		Assert.notNull(parent, "parent can't be null or empty");
		Assert.notNull(child, "parent can't be null or empty");
		return child.getAbsolutePath().startsWith(parent.getAbsolutePath());
	}

	/**
	 * Détermine si child est un répertoire fils de parent.
	 * @param parent -
	 * @param child -
	 * @return -
	 */
	public static boolean isParentOf(final String parent, final File child) {
		Assert.hasLength(parent, "parent can't be null or empty");
		Assert.notNull(child, "parent can't be null or empty");
		return FileAndDirectoryHLP.isParentOf(new File(parent), child);
	}

	/**
	 * Détermine si child est un répertoire fils de parent.
	 * @param parent -
	 * @param child -
	 * @return -
	 */
	public static boolean isParentOf(final String parent, final String child) {
		Assert.hasLength(parent, "parent can't be null or empty");
		Assert.hasLength(child, "parent can't be null or empty");
		return FileAndDirectoryHLP.isParentOf(new File(parent), new File(child));
	}

	/**
	 * Détermine si child est un répertoire fils de parent.
	 * @param parent -
	 * @param child -
	 * @return -
	 */
	public static boolean isParentOf(final File parent, final String child) {
		Assert.notNull(parent, "parent can't be null or empty");
		Assert.hasLength(child, "parent can't be null or empty");
		return FileAndDirectoryHLP.isParentOf(parent, new File(child));
	}

	/**
	 * Détermine si child est un répertoire fils de parent.
	 * @param parent -
	 * @param child -
	 * @return -
	 */
	public static boolean isParentOf(final FileDescriptor parent, final FileDescriptor child) {
		Assert.notNull(parent, "parent can't be null or empty");
		Assert.notNull(child, "parent can't be null or empty");
		return FileAndDirectoryHLP.isParentOf(parent.createFile(), child.createFile());
	}

}
