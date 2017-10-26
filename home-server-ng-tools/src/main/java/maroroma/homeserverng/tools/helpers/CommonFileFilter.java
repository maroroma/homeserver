package maroroma.homeserverng.tools.helpers;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;

/**
 * Classe utilitaire regroupant les filtres de fichiers de base.
 * @author RLEVEXIE
 *
 */
public final class CommonFileFilter {

	/**
	 * .
	 */
	private CommonFileFilter() {

	}

	/**
	 * Filtre de fichier pour les fichiers purs.
	 * @return -
	 */
	public static FileFilter pureFileFilter() { 
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.isFile();
			}
		};
	}

	/**
	 * Filtre de fichier pour les dossiers.
	 * @return -
	 */
	public static FileFilter pureDirectoryFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.isDirectory();
			}
		};
	}
	
	/**
	 * Filtre de fichier pour les dossiers comportant le nom directoryName.
	 * @param directoryName nom des répertoires à récupérer
	 * @return -
	 */
	public static FileFilter pureDirectoryFilter(final String directoryName) {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.isDirectory() && pathname.getName().contains(directoryName);
			}
		};
	}

	/**
	 * Filtre de fichiers par nom.
	 * @param fileName nom du fichier pour le filtre
	 * @return - 
	 */
	public static FileFilter fileNameFilter(final String fileName) {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.getName().toLowerCase().contains(fileName.toLowerCase());
			}
		};
	}
	
	/**
	 * Filtre de fichiers commencant par le préfixe donné.
	 * @param prefix -
	 * @return -
	 */
	public static FileFilter fileStartWithFilter(final String prefix) {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.getName().toLowerCase().startsWith(prefix.toLowerCase());
			}
		};
	}
	
	/**
	 * Retourne un filtre pour valider le nom de fichier.
	 * @param fileName - 
	 * @return -
	 */
	public static FileFilter fileNameWithoutExtensionIs(final String fileName) {
		// on vire l'extension avant de valider le nom du fichier (insensible à la casse
		return (file) -> file.getName()
				.replace("." + FilenameUtils.getExtension(file.getName()), "")
				.toLowerCase().equals(fileName.toLowerCase());
	}
	
	/**
	 * Détermine si le {@link File} est un fichier terminant par extension.
	 * @param extensions devant être au minimum contenue à la fin du fichier.
	 * @return -
	 */
	public static FileFilter fileExtensionFilter(final List<String> extensions) {
		Assert.notEmpty(extensions, "extensions can't be null or empty");
		return CommonFileFilter.fileExtensionFilter(extensions.toArray(new String[extensions.size()]));
	}
	
	/**
	 * Détermine si le {@link File} est un fichier terminant par extension.
	 * @param extensions devant être au minimum contenue à la fin du fichier.
	 * @return -
	 */
	public static FileFilter fileExtensionFilter(final String... extensions) {
		return new FileFilter() {
			
			@Override
			public boolean accept(final File pathname) {
				return pathname.exists() 
						&& pathname.isFile() 
						&& Arrays.asList(extensions).stream()
						.anyMatch(oneExtension -> pathname.getName().toLowerCase().endsWith(oneExtension.toLowerCase()));
			}
		};
	}
	
	/**
	 * Liste l'ensemble des répertoire porté par la propriété en entrée.
	 * @param toScan -
	 * @return -
	 */
	public static List<File> listDirectories(final HomeServerPropertyHolder toScan) {
		Assert.notNull(toScan, "toScan can't be null");
		return CommonFileFilter.listDirectories(toScan.asFile());
	}
	
	/**
	 * Liste l'ensemble des répertoire du fichier en entrée.
	 * @param toScan -
	 * @return -
	 */
	public static List<File> listDirectories(final File toScan) {
		Assert.isValidDirectory(toScan);
		return Arrays.asList(toScan.listFiles(CommonFileFilter.pureDirectoryFilter()));
	}
	
	/**
	 * Liste l'ensemble des fichiers du fichier en entrée.
	 * @param toScan -
	 * @return -
	 */
	public static List<File> listFiles(final File toScan) {
		Assert.isValidDirectory(toScan);
		return Arrays.asList(toScan.listFiles(CommonFileFilter.pureFileFilter()));
	}

}
