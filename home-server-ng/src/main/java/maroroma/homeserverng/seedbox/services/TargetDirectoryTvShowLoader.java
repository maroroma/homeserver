package maroroma.homeserverng.seedbox.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.filemanager.services.FilesFactory;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TargetDirectoryType;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDescriptorFilter;
import maroroma.homeserverng.tools.helpers.Assert;
import org.springframework.stereotype.Component;

/**
 * Implémentation de {@link TargetDirectoryLoader} pour les séries tv.
 * @author rlevexie
 *
 */
@Component
@RequiredArgsConstructor
public class TargetDirectoryTvShowLoader extends AbstractTargetDirectoryLoader {

	/**
	 * Répertoire contenant les répertoires cibles.
	 */
	@Property("homeserver.seedbox.target.directory.tvshows")
	private HomeServerPropertyHolder targetDirectoryPropertyHolder;

	@Property("homeserver.seedbox.target.directory.tvshows.kodialias")
	private HomeServerPropertyHolder kodiAlias;

	private final FilesFactory filesFactory;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TargetDirectory loadTargetDirectory() {
		
		Assert.isValidDirectory(this.targetDirectoryPropertyHolder);

		FileDescriptor targetDirectoryDescriptor = filesFactory.directoryFromProperty(this.targetDirectoryPropertyHolder);

		TargetDirectory returnValue = new TargetDirectory(targetDirectoryDescriptor, TargetDirectoryType.TVSHOWS);

		returnValue.getSubDirectories()
				.addAll(targetDirectoryDescriptor.listFiles(FileDescriptorFilter.directoryFilter()));
		return returnValue;
	}

	@Override
	public String[] getKodiAliases() {
		return this.kodiAlias.asStringArray();
	}

}
