package maroroma.homeserverng.tools.files;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
}
