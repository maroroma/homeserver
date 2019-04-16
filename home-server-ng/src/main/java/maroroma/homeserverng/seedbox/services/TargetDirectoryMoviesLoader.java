package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TargetDirectoryType;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.springframework.stereotype.Component;

/**
 * Implémentation de {@link TargetDirectoryLoader} pour le répertoire des films.
 * @author rlevexie
 *
 */
@Component
public class TargetDirectoryMoviesLoader extends AbstractTargetDirectoryLoader {

	/**
	 * Répertoire contenant les répertoires cibles.
	 */
	@Property("homeserver.seedbox.target.directory.movies")
	private HomeServerPropertyHolder targetDirectory;

	@Property("homeserver.seedbox.target.directory.movies.kodialias")
	private HomeServerPropertyHolder kodiAlias;

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


	@Override
	public String[] getKodiAliases() {
		return this.kodiAlias.asStringArray();
	}

	@Override
	public boolean includes(FileDescriptor fileDescriptor) {
		return fileDescriptor.createFile()
				.getAbsolutePath()
				.startsWith(this.loadTargetDirectory().getFullName());
	}
}
