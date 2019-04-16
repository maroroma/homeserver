package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TargetDirectoryType;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.springframework.stereotype.Component;

/**
 * Implémentation de {@link TargetDirectoryLoader} pour les séries tv.
 * @author rlevexie
 *
 */
@Component
public class TargetDirectoryTvShowLoader extends AbstractTargetDirectoryLoader {

	/**
	 * Répertoire contenant les répertoires cibles.
	 */
	@Property("homeserver.seedbox.target.directory.tvshows")
	private HomeServerPropertyHolder targetDirectory;

	@Property("homeserver.seedbox.target.directory.tvshows.kodialias")
	private HomeServerPropertyHolder kodiAlias;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TargetDirectory loadTargetDirectory() {
		
		Assert.isValidDirectory(this.targetDirectory);
		
		TargetDirectory returnValue = new TargetDirectory(targetDirectory.asFile(), TargetDirectoryType.TVSHOWS);
				
		FileDescriptor.addToList(returnValue.getSubDirectories(), targetDirectory.asFile().listFiles(CommonFileFilter.pureDirectoryFilter()));
		
		return returnValue;
	}

	@Override
	public String[] getKodiAliases() {
		return this.kodiAlias.asStringArray();
	}

}
