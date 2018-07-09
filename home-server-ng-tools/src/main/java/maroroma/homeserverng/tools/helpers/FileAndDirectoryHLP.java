package maroroma.homeserverng.tools.helpers;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarOutputStream;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe utilitaire pour la manipulation de répertoires et de fichiers.
 * @author rlevexie
 *
 */
@Log4j2
public abstract class FileAndDirectoryHLP {

	/**
	 * Taille du buffer par défaut.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * Taille du buffer utilisé pour la lecture des fichiers à tarer.
	 */
	private static final int TAR_DEFAULT_BUFFER_SIZE = 2048;

	/**
	 * Extension pour un fichier tar.
	 */
	private static final String TAR_EXTENSION = ".tar";

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
	 * Retourne le fichier de chemin base64 donné en tableau de byte.
	 * @param base64Path -
	 * @return -
	 * @throws HomeServerException -
	 */
	public static byte[] convertFileToByteArray(final String base64Path) throws HomeServerException {
		return FileAndDirectoryHLP.convertFileToByteArray(FileAndDirectoryHLP.decodeFile(base64Path));
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

	/**
	 * Convertit en fichier tar le répertoire en entrée.
	 * @param srcDirectory  répertoire à tarer
	 * @return descriptor du fichier final.
	 * @throws HomeServerException -
	 */
	public static File tarDirectory(final File srcDirectory) throws HomeServerException {
		return FileAndDirectoryHLP.tarDirectory(srcDirectory, null);
	}

	/**
	 * Convertit en fichier tar le répertoire en entrée.
	 * @param srcDirectory répertoire à tarer
	 * @param targetFile fichier final
	 * @return descriptor du fichier final.
	 * @throws HomeServerException -
	 */
	public static File tarDirectory(final File srcDirectory, final File targetFile) throws HomeServerException {

		// validation du répertoire
		Assert.isValidDirectory(srcDirectory);

		File tarFile = targetFile;

		// fichier de sortie
		if (tarFile == null) {
			tarFile = new File(srcDirectory.getParentFile(), srcDirectory.getName() + TAR_EXTENSION);
		}

		try (
				// fichier de sortie
				FileOutputStream dest = new FileOutputStream(tarFile); 
				// tar de sortie
				TarOutputStream out = new TarOutputStream(new BufferedOutputStream(dest));) {

			Arrays.stream(srcDirectory.listFiles())
			.forEach(oneFile -> {
				// pour chaque fichier, lecture du contenu pour recopie dans le tar
				try (BufferedInputStream origin = new BufferedInputStream(new FileInputStream(oneFile));) {
					out.putNextEntry(new TarEntry(oneFile, oneFile.getName()));
					int count;
					byte[] data = new byte[TAR_DEFAULT_BUFFER_SIZE];

					while ((count = origin.read(data)) != -1) {
						out.write(data, 0, count);
					}

					out.flush();
				} catch (IOException e) {
					throw new RuntimeHomeServerException(e);
				}
			});
		} catch (RuntimeHomeServerException | IOException e) {
			throw new HomeServerException("Une erreur est survenue lors de la création du fichier [" + tarFile.getAbsolutePath() + "]", e);
		}

		return tarFile;
	}

	/**
	 * Recopie d'un fichier dans un ouputstream, sans passage temporaire en mémoire.
	 * @param toDownload -
	 * @param output -
	 * @throws HomeServerException -
	 */
	public static void copyFileToOuputStream(final File toDownload, final OutputStream output) throws HomeServerException {

		Assert.notNull(output, "output can't be null");
		Assert.isValidFile(toDownload);
		
		try (InputStream is = new FileInputStream(toDownload)) {
			// le flux de sortie est celui de la réponse HTTP.
			int read = 0;

			byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];

			// ecriture au fil de l'eau
			while ((read = is.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}

		} catch (IOException e) {
			throw new HomeServerException("Une erreur est survenue lors de l'écriture de [" 
					+ toDownload.getAbsolutePath() + "] dans le stream de sortie", e);
		} finally {
			StreamHLP.safeFlush(output);
			StreamHLP.safeClose(output);
		}
	}
	
	/**
	 * Recopie d'un fichier dans un ouputstream, sans passage temporaire en mémoire.
	 * @param toDownload -
	 * @param response -
	 * @throws HomeServerException -
	 */
	public static void copyFileToOuputStream(final File toDownload, final HttpServletResponse response) throws HomeServerException {
		Assert.notNull(response, "response can't be null");
		try {
			copyFileToOuputStream(toDownload, response.getOutputStream());
		} catch (IOException e) {
			throw new HomeServerException("Erreur rencontrée lors de la récupération du flux de sortie", e);
		}
	}
/**
	 * Recopie d'un fichier dans un ouputstream, sans passage temporaire en mémoire.
	 * @param toDownload -
	 * @param response -
	 * @throws HomeServerException -
	 */
	public static void copyFileToOuputStream(final FileDescriptor toDownload, final HttpServletResponse response) throws HomeServerException {
		Assert.notNull(response, "response can't be null");
		Assert.isValidFile(toDownload);
		response.setHeader("Content-Length", ""+toDownload.getSize());
		copyFileToOuputStream(toDownload.createFile(), response);
	}

}
