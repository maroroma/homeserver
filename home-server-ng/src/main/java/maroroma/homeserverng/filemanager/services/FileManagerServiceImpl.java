package maroroma.homeserverng.filemanager.services;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.FileDeletionResult;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;

/**
 * Implémentation du service pour la gestion des fichiers.
 * @author rlevexie
 *
 */
@Service
@Log4j2
public class FileManagerServiceImpl implements FileManagerService {

	/**
	 * Liste des répertoires racines gérables par le filemanager.
	 */
	@Property("homeserver.filemanager.rootdirectories")
	private HomeServerPropertyHolder rootDirectoriesList;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileDescriptor createDirectory(final DirectoryCreationRequest creationRequest) throws HomeServerException {
		
		// validation des entrées
		Assert.notNull(creationRequest, "creationRequest can't be null");
		Assert.notNull(creationRequest.getParentDirectory(), "creationRequest.getParentDirectory() can't be null");
		Assert.hasLength(creationRequest.getDirectoryName(), "creationRequest.getDirectoryName() can't be null or empty");
		Assert.isValidDirectory(creationRequest.getParentDirectory());
		
		// création du répertoire
		File target = new File(creationRequest.getParentDirectory().createFile(), creationRequest.getDirectoryName());
		
		// création physique du répertoire
		if (!target.mkdir()) {
			throw new HomeServerException("Le répertoire " + target.getAbsolutePath() + " n'a pas pu être créé");
		}

		// retour si succès
		return new FileDescriptor(target);
	}

	@Override
	public List<FileDirectoryDescriptor> getRootDirectories() throws HomeServerException {
			return this.rootDirectoriesList.asFileList().stream()
					.map(oneFile -> FileDirectoryDescriptor.createSimple(oneFile)).collect(Collectors.toList());
	}

	@Override
	public FileDirectoryDescriptor getDirectoryDetail(final String id) throws HomeServerException {
		File toScan = FileAndDirectoryHLP.decodeFile(id);
		Assert.isValidDirectory(toScan);
		return FileDirectoryDescriptor.create(toScan);
	}

	@Override
	public FileDeletionResult deleteFile(final String id) {
		Assert.hasLength(id, "id can't be null or empty");
		final FileDescriptor fileToDelete = FileAndDirectoryHLP.decodeFileDescriptor(id);
		Assert.isValidFileOrDirectory(fileToDelete);
		
		Assert.isTrue(this.rootDirectoriesList.asStringList().stream()
				.anyMatch(rootPath -> FileAndDirectoryHLP.isParentOf(rootPath, fileToDelete.createFile())),
				"Le fichier à supprimer ne fait pas partie des répertoires gérables");
		
		return new FileDeletionResult(fileToDelete, fileToDelete.deleteFile());
		
		
	}

}
