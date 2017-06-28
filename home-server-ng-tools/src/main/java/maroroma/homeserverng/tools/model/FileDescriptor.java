package maroroma.homeserverng.tools.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la description des fichiers présents
 *  dans le répertoire de tri de la seedbox.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
public class FileDescriptor {

	/**
	 * Nom du fichier.
	 */
	private String name;
	/**
	 * Chemin complet du fichier.
	 */
	private String fullName;

	/**
	 * Accessibilité du fichier en tant que ressource http.
	 */
	private String httpRessource;
	
	/**
	 * Encodage du nom de fichier pour facilité les échange http.
	 */
	private String base64FullName;

	/**
	 * Constructeur.
	 * @param fileName -
	 * @param fullFileName -
	 * @param httpRez -
	 */
	public FileDescriptor(final String fileName, final String fullFileName, final String httpRez) {
		this.name = fileName;
		this.fullName = fullFileName;
		this.httpRessource = httpRez;
		this.base64FullName = Base64Utils.encodeToString(this.fullName.getBytes());
	}

	/**
	 * Produit un {@link File} à partir du nom complet du fichier.
	 * @return -
	 */
	public File createFile() {
		return new File(this.getFullName());
	}

	/**
	 * Permert de supprimer le fichire donné.
	 * @return true si la suppression est ok.
	 */
	public boolean deleteFile() {
		File toDelete = this.createFile();
		Assert.isTrue(toDelete.exists(), "Le fichier " + toDelete.getAbsolutePath() + " n'existe pas");
		return toDelete.delete();
	}

	/**
	 * Constructeur.
	 * @param file -
	 */
	public FileDescriptor(final File file) {
		this(file.getName(), file.getAbsolutePath());
	}

	/**
	 * Constructeur.
	 * @param file -
	 * @param parentUri -
	 */
	public FileDescriptor(final File file, final String parentUri) {
		this(file.getName(), file.getAbsolutePath(), (parentUri.endsWith("/") ? parentUri : (parentUri + "/")) + file.getName());
	}

	/**
	 * Constructeur.
	 * @param fileName -
	 * @param absoluteFileName -
	 */
	public FileDescriptor(final String fileName, final String absoluteFileName) {
		this(fileName, absoluteFileName, null);
	}

	/**
	 * Constructeur de type clonage.
	 * @param source -
	 */
	protected FileDescriptor(final FileDescriptor source) {
		this.fullName = source.getFullName();
		this.httpRessource = source.getHttpRessource();
		this.base64FullName = source.getBase64FullName();
		this.name = source.getName();
	}

	/**
	 * Retourne une liste de {@link FileDescriptor} à partir d'une lise de {@link File}.
	 * @param array -
	 * @return -
	 */
	public static List<FileDescriptor> toList(final File[] array) {
		List<FileDescriptor> returnValue = new ArrayList<>();
		return addToList(returnValue, array);
	}
	
	/**
	 * Retourne une liste de {@link FileDescriptor} à partir d'une lise de {@link File}.
	 * @param array -
	 * @return -
	 */
	public static List<FileDescriptor> toList(final List<File> array) {
		List<FileDescriptor> returnValue = new ArrayList<>();
		return addToList(returnValue, array);
	}

	/**
	 * Retourne une liste de {@link FileDescriptor} à partir d'une lise de {@link File}.
	 * @param array -
	 * @param httpRootRes racine de la ressource http pour le fichier.
	 * @return -
	 */
	public static List<FileDescriptor> toList(final File[] array, final String httpRootRes) {
		List<FileDescriptor> returnValue = new ArrayList<>();
		return addToList(returnValue, array, httpRootRes);
	}

	/**
	 * Ajoute le tableau de {@link File} dans la liste de {@link FileDescriptor} donnée.
	 * @param listToPopulate -
	 * @param files -
	 * @return -
	 */
	public static List<FileDescriptor> addToList(final List<FileDescriptor> listToPopulate, final File[] files) {

		if (files != null) {

			for (File pureFile : files) {
				listToPopulate.add(new FileDescriptor(pureFile));
			}
		}

		return listToPopulate;
	}
	
	/**
	 * Ajoute le tableau de {@link File} dans la liste de {@link FileDescriptor} donnée.
	 * @param listToPopulate -
	 * @param files -
	 * @return -
	 */
	public static List<FileDescriptor> addToList(final List<FileDescriptor> listToPopulate, final List<File> files) {

		if (files != null) {
			files.forEach(pureFile -> listToPopulate.add(new FileDescriptor(pureFile)));
		}

		return listToPopulate;
	}

	/**
	 * Ajoute le tableau de {@link File} dans la liste de {@link FileDescriptor} donnée.
	 * @param listToPopulate -
	 * @param files -
	 * @param httpRootRes racine de la ressource http pour le fichier.
	 * @return -
	 */
	public static List<FileDescriptor> addToList(final List<FileDescriptor> listToPopulate, final File[] files, final String httpRootRes) {

		if (files != null) {
			for (File pureFile : files) {
				listToPopulate.add(new FileDescriptor(pureFile, httpRootRes));
			}
		}

		return listToPopulate;
	}





}
