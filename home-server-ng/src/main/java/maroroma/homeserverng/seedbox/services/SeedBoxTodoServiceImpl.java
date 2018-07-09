package maroroma.homeserverng.seedbox.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.tools.SeedboxModuleConstants;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de manipulation des fichiers à trier au niveau de la seedbox.
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
public class SeedBoxTodoServiceImpl {

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
	 * Retourne la liste des fichiers à trier.
	 * @return -
	 */
	public List<TodoFile> getTodoList() {
		Assert.isValidDirectory(this.todoDirectory);

		// retour de la fonction
		List<TodoFile> returnValue = new ArrayList<>();
		
		// répertoire à scanner
		File file = this.todoDirectory.asFile();
		
		// liste complete des fichiers du répertoire à scanner
		List<FileDescriptor> rawTodoListToPopulate = new ArrayList<>();

		// lancement du listing des fichiers.
		this.listFiles(rawTodoListToPopulate, file, CommonFileFilter.pureFileFilter());

		returnValue = rawTodoListToPopulate
				.stream()
				// on convertit en todofile, en checkant si le fichier semble nouveau
				.map(oneFile -> new TodoFile(oneFile, !this.lastCompletedFileList.contains(oneFile.getFullName())))
				.collect(Collectors.toList());

		// mise à jour de la liste de noms de fichier pour le prochain appel
		this.lastCompletedFileList = returnValue.stream()
				.map(oneTodoFile -> oneTodoFile.getFullName())
				.collect(Collectors.toList());

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
	 * Retourne la liste des dossier cibles pour le déplacement.
	 * @return -
	 */
	public List<TargetDirectory> getTargetList() {
		return this.targetLoaders.stream()
				.map(loader -> loader.loadTargetDirectory())
				.collect(Collectors.toList());
	}

	/**
	 * Déplacement d'une liste de fichier dans un répertoire cible.
	 * @param request -
	 * @return -
	 */
	public List<MovedFile> moveFiles(final MoveRequest request) throws HomeServerException {

		log.info("déplacement d'un fichier : " + request.toString());


		// récupération du loader et controle de la bonne hierarchie
		TargetDirectoryLoader loader = this.targetLoaders.stream()
				.filter(tl -> tl.includes(request.getTarget()))
				.findFirst()
				.orElseThrow(() -> new HomeServerException("Le répertoire cible ne fait pas partie des cibles proposées"));



		// déplacement du fichier
		List<MovedFile> returnValue = request.getFilesToMove()
				.stream()
				.map(oneFileToMove -> this.moveOneFile(request, oneFileToMove))
				.collect(Collectors.toList());

		// lancement du scan vers kodi sur la target
		loader.executeScanOnKodiInstances();


		return returnValue;
	}

	/**
	 * Retourne le détail d'un répertoire contenu dans l'arborescence des cibles.
	 * @param directoryToParse -
	 * @return -
	 */
	public List<FileDescriptor> getDirectoryDetails(final FileDescriptor directoryToParse) {
		
		File toScan = directoryToParse.createFile();
		Assert.isValidDirectory(toScan);
		return FileDescriptor.toList(toScan.listFiles(CommonFileFilter.pureDirectoryFilter()));
		
	}

	/**
	 * Retourne le détail d'un répertoire contenu dans l'arborescence des cibles.
	 * @param fileId -
	 * @return -
	 */
	public FileDirectoryDescriptor getDirectoryDetails(final String fileId) {
		File toScan = FileAndDirectoryHLP.decodeFile(fileId);
		Assert.isValidDirectory(toScan);
		return FileDirectoryDescriptor.createWithSubDirectories(toScan);
	}

	/**
	 * Permet de supprimer un fichier de la todolist.
	 * @param toDelete -
	 * @return -
	 */
	public List<TodoFile> deleteTodoFile(final FileDescriptor toDelete) {
		Assert.notNull(toDelete, "toDelete can't be null");
		Assert.isTrue(toDelete.deleteFile(), "Le fichier n'a pu être supprimé");
		return this.getTodoList();
	}

	/**
	 * Permet de supprimer un fichier de la todolist.
	 * @param fileId -
	 * @return -
	 */
	public List<TodoFile> deleteTodoFile(final String fileId) {
		Assert.hasLength(fileId, "fileId can't be null");
		return this.deleteTodoFile(FileAndDirectoryHLP.decodeFileDescriptor(fileId));
	}

	private MovedFile moveOneFile(MoveRequest request, FileToMoveDescriptor oneFileToMove) {
		File finalFile = new File(request.getTarget().getFullName() + File.separator + oneFileToMove.getNewName());
		return MovedFile.builder()
				.sourceFile(oneFileToMove)
				.targetFile(new FileDescriptor(finalFile))
				.success(oneFileToMove.createFile().renameTo(finalFile))
				.finalPath(request.getTarget().getFullName())
				.build();
	}

}
