package maroroma.homeserverng.seedbox.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.tools.model.FileDescriptor;

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
	public TargetDirectory(final File file, final TargetDirectoryType theType) {
		super(file);
		this.type = theType;
		this.subDirectories = new ArrayList<>();
	}
	
}
