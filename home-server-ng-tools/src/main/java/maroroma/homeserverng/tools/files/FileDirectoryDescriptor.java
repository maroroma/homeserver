package maroroma.homeserverng.tools.files;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.*;

/**
 * Implémentation non récursive de la description d'un répertoire.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
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
	
	public FileDirectoryDescriptor(final AbstractFileDescriptorAdapter adapter,
								   final boolean parseFiles,
								   final boolean parseDirectories) {
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

	public PathCombiner combinePath(String path) {
		var combiner = new PathCombiner(this);
		return combiner.combinePath(path);
	}


	public final class PathCombiner {
		private final StringBuilder pathBuilder = new StringBuilder();
		private final FileDirectoryDescriptor parent;

		PathCombiner(FileDirectoryDescriptor parent) {
			this.parent = parent;
		}

		public PathCombiner combinePath(String pathToAppend) {
			this.pathBuilder
					.append("/")
					.append(pathToAppend);
			return this;
		}

		public FileDescriptor asFile() {
			return new FileDescriptor(this.parent.getAdapter().combinePath(this.pathBuilder.toString()));
		}

		public FileDirectoryDescriptor asDirectory() {
			return new FileDirectoryDescriptor(this.parent.getAdapter().combinePath(this.pathBuilder.toString()), false, false);
		}
	}


	

}
