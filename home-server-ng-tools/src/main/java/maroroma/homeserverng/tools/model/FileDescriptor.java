package maroroma.homeserverng.tools.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * DTO pour la description des fichiers présents
 *  dans le répertoire de tri de la seedbox.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
@Log4j2
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
	 * Taille estimée du fichier.
	 */
	private long size;

	/**
	 * Constructeur.
	 * @param fileName -
	 * @param fullFileName -
	 */
	public FileDescriptor(final String fileName, final String fullFileName) {
		this.name = fileName;
		this.fullName = fullFileName;
		this.base64FullName = Base64Utils.encodeToString(this.fullName.getBytes());
	}
	
	/**
	 * Constructeur.
	 * @param file -
	 */
	public FileDescriptor(final File file) {
		this(file.getName(), file.getAbsolutePath());
		try {
			this.size = Files.size(file.toPath());
		} catch (IOException e) {
			log.warn("Problème rencontré lors de la récupération de la taille du fichier {}", file.getAbsolutePath());
		}
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
		this.size = source.getSize();
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
	 * Permet de renommer le fichier donné.
	 * @param newName nouveau nom
	 * @return true si le renommage est ok.
	 */
	public boolean renameFile(final String newName) {
		File toRename = this.createFile();
		Assert.isTrue(toRename.exists(), "Le fichier " + toRename.getAbsolutePath() + " n'existe pas");
		File renameTo = new File(toRename.getParentFile(), newName);
		return toRename.renameTo(renameTo);
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

}
