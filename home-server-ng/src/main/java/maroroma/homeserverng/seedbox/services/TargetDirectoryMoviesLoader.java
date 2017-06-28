package maroroma.homeserverng.seedbox.services;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TargetDirectoryType;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Implémentation de {@link TargetDirectoryLoader} pour le répertoire des films.
 * @author rlevexie
 *
 */
@Component
public class TargetDirectoryMoviesLoader implements TargetDirectoryLoader {

	/**
	 * Répertoire contenant les répertoires cibles.
	 */
	@Property("homeserver.seedbox.target.directory.movies")
	private HomeServerPropertyHolder targetDirectory;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TargetDirectory loadTargetDirectory() {
		
		Assert.isValidDirectory(this.targetDirectory);
		
		TargetDirectory returnValue = new TargetDirectory(targetDirectory.asFile(), TargetDirectoryType.MOVIES);
		
		returnValue.getSubDirectories().add(new FileDescriptor(targetDirectory.asFile()));
		
		return returnValue;
	}

}
