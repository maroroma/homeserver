package maroroma.homeserverng.seedbox.services;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.tools.SeedboxModuleConstants;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.model.FileToMoveDescriptor;
import maroroma.homeserverng.tools.model.MoveRequest;
import maroroma.homeserverng.tools.model.MovedFile;

/**
 * Implémentation du service de manipulation des fichiers à trier au niveau de la seedbox.
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
public class SeedBoxTodoServiceImpl implements SeedBoxTodoService {

	/**
	 * Répertoire pour les fichiers à trier.
	 */
	@Property(SeedboxModuleConstants.HOMESERVER_SEEDBOX_TODO_DIRECTORY_PROP_KEY)
	private HomeServerPropertyHolder todoDirectory;

	/**
	 * Liste des répertoires cibles à charger pour le déplacement.
	 */
	@Autowired
	private List<TargetDirectoryLoader> targetLoaders;

	/**
	 * Permet de stocker la dernière liste de nom de fichiers scannés.
	 * <br /> amène l'introduction d'un indicateur {@link TodoFile#isNew()} dans le retour du service, permettant de savoir quel fichier a été ajouté
	 * depuis le dernier appel.
	 */
	private List<String> lastCompletedFileList = new ArrayList<>();
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TodoFile> getTodoList() {
		Assert.isValidDirectory(this.todoDirectory);

		// retour de la fonction
		List<TodoFile> returnValue = new ArrayList<>();
		
		// préparation de la liste des derniers fichiers scannés
		List<String> newLastCompletedFileList = new ArrayList<>();
		
		// répertoire à scanner
		File file = this.todoDirectory.asFile();
		
		// liste complete des fichiers du répertoire à scanner
		List<FileDescriptor> rawTodoListToPopulate = new ArrayList<>();

		// lancement du listing des fichiers.
		this.listFiles(rawTodoListToPopulate, file, CommonFileFilter.pureFileFilter());

		// création de la sortie avec l'indicateur isNew
		for (FileDescriptor fileDescriptor : rawTodoListToPopulate) {
			// détermine si le fichier était déjà présent sur le dernier appel
			boolean isNew = !this.lastCompletedFileList.contains(fileDescriptor.getFullName());
			
			// préparation de la mise à jour de la liste
			newLastCompletedFileList.add(fileDescriptor.getFullName());
			
			// création du retour finalement utilisé par l'ihm
			returnValue.add(new TodoFile(fileDescriptor, isNew));
		}

		// mise à jour de la liste de noms de fichier pour le prochain appel
		this.lastCompletedFileList.clear();
		this.lastCompletedFileList.addAll(newLastCompletedFileList);
		
		
		return returnValue;



	}

	/**
	 * Scan récursif d'un répertoire.
	 * @param toPopulate liste à peupler à par la fonction récursive
	 * @param srcFile repertoire source
	 * @param filter fitlre pour le type de fichier à scanner.
	 */
	private void listFiles(final List<FileDescriptor> toPopulate, final File srcFile, final FileFilter filter) {

		// récupération des fichiers
		File[] listeFilesTodo = srcFile.listFiles(filter);

		FileDescriptor.addToList(toPopulate, listeFilesTodo);


		// récupération des sous dossiers
		File[] listeDirectoryTodo = srcFile.listFiles(CommonFileFilter.pureDirectoryFilter());

		for (File file : listeDirectoryTodo) {
			this.listFiles(toPopulate, file, filter);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
//	@Cacheable(SeedboxModuleConstants.SEEDBOX_TARGET_LIST_CACHE_NAME)
	public List<TargetDirectory> getTargetList() {
		
		List<TargetDirectory> returnValue = new ArrayList<>();
		
		for (TargetDirectoryLoader loader : this.targetLoaders) {
			returnValue.add(loader.loadTargetDirectory());
		}
		
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MovedFile> moveFiles(final MoveRequest request) {

		log.info("déplacement d'un fichier : " + request.toString());
		
		List<MovedFile> returnValue = new ArrayList<>();


		for (FileToMoveDescriptor fd : request.getFilesToMove()) {

			try {

				
				// recopie du fichier par renommage avec le nouveau nom
				boolean moveResult = fd.createFile().renameTo(new File(request.getTarget().getFullName() + File.separator + fd.getNewName()));
				
				returnValue.add(new MovedFile(fd, request.getTarget().getFullName(), moveResult));
				
			} catch (Exception e) {

			}
		}

		return returnValue;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FileDescriptor> getDirectoryDetails(final FileDescriptor directoryToParse) {
		
		File toScan = directoryToParse.createFile();
		Assert.isValidDirectory(toScan);
		return FileDescriptor.toList(toScan.listFiles(CommonFileFilter.pureDirectoryFilter()));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileDirectoryDescriptor getDirectoryDetails(final String fileId) {
		
		File toScan = FileAndDirectoryHLP.decodeFile(fileId);
		Assert.isValidDirectory(toScan);
		return FileDirectoryDescriptor.createWithSubDirectories(toScan);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TodoFile> deleteTodoFile(final FileDescriptor toDelete) {
		Assert.notNull(toDelete, "toDelete can't be null");
		Assert.isTrue(toDelete.deleteFile(), "Le fichier n'a pu être supprimé");
		return this.getTodoList();
	}

	@Override
	public List<TodoFile> deleteTodoFile(final String fileId) {
		Assert.hasLength(fileId, "fileId can't be null");
		return this.deleteTodoFile(FileAndDirectoryHLP.decodeFileDescriptor(fileId));
	}
	
	
}
