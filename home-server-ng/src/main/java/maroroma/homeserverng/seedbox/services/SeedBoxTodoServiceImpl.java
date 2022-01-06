package maroroma.homeserverng.seedbox.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.tools.SeedboxModuleConstants;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.files.*;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileExtensionHelper;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de manipulation des fichiers à trier au niveau de la seedbox.
 * @author RLEVEXIE
 *
 */
@Service
@Slf4j
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

	@Autowired
	private SecurityManager securityManager;

	/**
	 * Permet de stocker la dernière liste de nom de fichiers scannés.
	 * <br /> amène l'introduction d'un indicateur {@link TodoFile#isNew()} dans le retour du service, permettant de savoir quel fichier a été ajouté
	 * depuis le dernier appel.
	 */
	private List<String> lastCompletedFileList = new ArrayList<>();


	/**
	 * Retourne la liste des fichiers à trier, filtré sur les extensions données
	 * @param extensions extension pour filtrage
	 * @return liste filtrée
	 */
	public List<TodoFile> getTodoList(String... extensions) {
		return this.getTodoList().stream()
				.filter(oneTodoFile -> FileExtensionHelper.hasExtension(oneTodoFile.getName(), extensions))
				.collect(Collectors.toList());
	}

	/**
	 * Retourne la liste des fichiers à trier.
	 * @return -
	 */
	public List<TodoFile> getTodoList() {

		FileDescriptor todoDirectoryDescriptor = this.todoDirectory.asFileDescriptorFactory()
				.withSecurityManager(this.securityManager)
				.fileDescriptor();

		List<FileDescriptor> allTodoFiles = Collections.synchronizedList(new ArrayList<>());
		this.listFiles(allTodoFiles, todoDirectoryDescriptor);

		List<TodoFile> todoFiles = allTodoFiles.stream()
				.map(oneFile -> new TodoFile(oneFile, !this.lastCompletedFileList.contains(oneFile.getId())))
				.collect(Collectors.toList());

		// mise à jour de la liste de noms de fichier pour le prochain appel
		this.lastCompletedFileList = todoFiles.stream()
				.map(oneTodoFile -> oneTodoFile.getId())
				.collect(Collectors.toList());


		return todoFiles;
	}

	private void listFiles(final List<FileDescriptor> toPopulate, final FileDescriptor srcFile) {

		// récupération oneshot de tous les fichiers
		List<FileDescriptor> allFiles = srcFile.listFiles(FileDescriptorFilter.noFilter());

		// récupération des fichiers
		toPopulate.addAll(allFiles.stream().filter(FileDescriptorFilter.fileFilter()).collect(Collectors.toList()));

		allFiles.parallelStream()
				.filter(FileDescriptorFilter.directoryFilter())
				.forEach(oneDirectory -> listFiles(toPopulate, oneDirectory));
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

		// résolution répertoire cible
		FileDescriptor selectedTargetDirectory = FileDescriptorFactory
				.fromId(request.getTarget().getId())
				.withSecurityManager(this.securityManager)
				.fileDescriptor();

		// récupération du loader et controle de la bonne hierarchie
		TargetDirectoryLoader loader = this.targetLoaders.stream()
				.filter(tl -> tl.includes(selectedTargetDirectory))
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
	 * @param fileId -
	 * @return -
	 */
	public FileDirectoryDescriptor getDirectoryDetails(final String fileId) {
		return FileDescriptorFactory.fromId(fileId)
				.withSecurityManager(this.securityManager)
				.fileDirectoryDescriptor(false, true);
	}

	/**
	 * Permet de supprimer un fichier de la todolist.
	 * @param fileId -
	 * @return -
	 */
	public List<TodoFile> deleteTodoFile(final String fileId) {
		Assert.hasLength(fileId, "fileId can't be null");
		FileDescriptorFactory.fromId(fileId)
				.withSecurityManager(this.securityManager)
				.fileDescriptor()
				.deleteFile();
		return this.getTodoList();
	}

	private MovedFile moveOneFile(MoveRequest request, FileToMoveDescriptor oneFileToMove) {

		FileDescriptor source = FileDescriptorFactory
				.fromId(oneFileToMove.getId())
				.withSecurityManager(this.securityManager)
				.fileDescriptor();

		FileDescriptor target = FileDescriptorFactory
				.fromId(request.getTarget().getId())
				.withSecurityManager(this.securityManager)
				.combinePath(oneFileToMove.getNewName())
				.fileDescriptor();

		return MovedFile.builder()
				.sourceFile(oneFileToMove)
				.targetFile(target)
				.success(source.moveFile(target).isCompleted())
				.finalPath(request.getTarget().getFullName())
				.build();
	}

}
