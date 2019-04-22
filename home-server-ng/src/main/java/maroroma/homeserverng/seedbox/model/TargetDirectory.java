package maroroma.homeserverng.seedbox.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.tools.model.FileDescriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Correspond à un répertoire cible, ie un {@link FileDescriptor} disposant de sous {@link FileDescriptor}.
 * @author rlevexie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TargetDirectory extends FileDescriptor {

	/**
	 * Type de cible.
	 */
	private TargetDirectoryType type;
	
	/**
	 * Liste des sous répertoires contenus par ce {@link TargetDirectory}.
	 */
	private List<FileDescriptor> subDirectories;
	
	/**
	 * Constructeur.
	 * @param file fichier correspondant au répertoire.;
	 * @param theType -
	 */
	@Deprecated
	public TargetDirectory(final File file, final TargetDirectoryType theType) {
		super(file);
		this.type = theType;
		this.subDirectories = new ArrayList<>();
	}

	public TargetDirectory(final FileDescriptor fileDescriptor, final TargetDirectoryType theType) {
		super(fileDescriptor);
		this.type = theType;
		this.subDirectories = new ArrayList<>();
	}
	
}
