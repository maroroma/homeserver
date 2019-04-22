package maroroma.homeserverng.tools.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.tools.files.AbstractFileDescriptorAdapter;
import maroroma.homeserverng.tools.files.FileDescriptorFilter;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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
	@Deprecated
	private FileDirectoryDescriptor(final File file, final boolean parseFiles, final boolean parseDirectories) {
		super(file);
		if (parseDirectories) {
			this.directories = FileDescriptor.toList(CommonFileFilter.listDirectories(file));
		}
		if (parseFiles) {
			this.files = FileDescriptor.toList(CommonFileFilter.listFiles(file));
		}
	}

	public FileDirectoryDescriptor(final AbstractFileDescriptorAdapter adapter, final boolean parseFiles, final boolean parseDirectories) {
		super(adapter);

		// optimisons un peu
		if (parseDirectories || parseFiles) {
			List<FileDescriptor> allFiles = this.listFiles(FileDescriptorFilter.noFilter());

			if (parseFiles) {
				this.files = allFiles.stream().filter(FileDescriptorFilter.fileFilter()).collect(Collectors.toList());
			}

			if (parseDirectories) {
				this.directories = allFiles.stream().filter(FileDescriptorFilter.directoryFilter()).collect(Collectors.toList());
			}
		}
	}
	
	/**
	 * Créer un {@link FileDirectoryDescriptor} à partir d'un {@link File}. Les sous fichier et sous répertoires sont scannés.
	 * @param file -
	 * @return -
	 */
	@Deprecated
	public static FileDirectoryDescriptor create(final File file) {
		return new FileDirectoryDescriptor(file, true, true);
	}
	
	/**
	 * Créer un {@link FileDirectoryDescriptor} à partir d'un {@link File}. Ne scanne que les sous répertoires.
	 * @param file -
	 * @return -
	 */
	@Deprecated
	public static FileDirectoryDescriptor createWithSubDirectories(final File file) {
		return new FileDirectoryDescriptor(file, false, true);
	}
	
	/**
	 * Créer un {@link FileDirectoryDescriptor} à partir d'un {@link File}. Ne scanne que les fichiers.
	 * @param file -
	 * @return -
	 */
	@Deprecated
	public static FileDirectoryDescriptor createWithFiles(final File file) {
		return new FileDirectoryDescriptor(file, true, false);
	}
	
	/**
	 * Crée un {@link FileDirectoryDescriptor} sans sous grappe.
	 * @param file -
	 * @return -
	 */
	@Deprecated
	public static FileDirectoryDescriptor createSimple(final File file) {
		return new FileDirectoryDescriptor(file, false, false);
	}
}
