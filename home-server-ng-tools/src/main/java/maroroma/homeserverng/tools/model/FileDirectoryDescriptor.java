package maroroma.homeserverng.tools.model;

import java.io.File;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;

/**
 * Implémentation non récursive de la description d'un répertoire.
 * @author RLEVEXIE
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileDirectoryDescriptor extends FileDescriptor {
	
	/**
	 * Liste des fichiers du répertoire.
	 */
	private List<FileDescriptor> files;
	
	/**
	 * Liste des sous répertoire du répertoire.
	 */
	private List<FileDescriptor> directories;
	
	/**
	 * Constructeur.
	 */
	public FileDirectoryDescriptor() {
		
	}

	/**
	 * Constructeur.
	 * @param file -
	 * @param parseFiles -
	 * @param parseDirectories -
	 */
	private FileDirectoryDescriptor(final File file, final boolean parseFiles, final boolean parseDirectories) {
		super(file);
		if (parseDirectories) {
			this.directories = FileDescriptor.toList(CommonFileFilter.listDirectories(file));
		}
		if (parseFiles) {
			this.files = FileDescriptor.toList(CommonFileFilter.listFiles(file));
		}
	}
	
	/**
	 * Créer un {@link FileDirectoryDescriptor} à partir d'un {@link File}. Les sous fichier et sous répertoires sont scannés.
	 * @param file -
	 * @return -
	 */
	public static FileDirectoryDescriptor create(final File file) {
		return new FileDirectoryDescriptor(file, true, true);
	}
	
	/**
	 * Créer un {@link FileDirectoryDescriptor} à partir d'un {@link File}. Ne scanne que les sous répertoires.
	 * @param file -
	 * @return -
	 */
	public static FileDirectoryDescriptor createWithSubDirectories(final File file) {
		return new FileDirectoryDescriptor(file, false, true);
	}
	
	/**
	 * Créer un {@link FileDirectoryDescriptor} à partir d'un {@link File}. Ne scanne que les fichiers.
	 * @param file -
	 * @return -
	 */
	public static FileDirectoryDescriptor createWithFiles(final File file) {
		return new FileDirectoryDescriptor(file, true, false);
	}
	
	/**
	 * Crée un {@link FileDirectoryDescriptor} sans sous grappe.
	 * @param file -
	 * @return -
	 */
	public static FileDirectoryDescriptor createSimple(final File file) {
		return new FileDirectoryDescriptor(file, false, false);
	}
}
