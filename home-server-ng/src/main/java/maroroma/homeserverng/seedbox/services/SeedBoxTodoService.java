package maroroma.homeserverng.seedbox.services;

import java.util.List;

import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.model.MoveRequest;
import maroroma.homeserverng.tools.model.MovedFile;

/**
 * Définition du service de manipulation de fichiers à traiter.
 * @author RLEVEXIE
 *
 */
public interface SeedBoxTodoService {

	/**
	 * Retourne la liste des fichiers à trier.
	 * @return -
	 */
	List<TodoFile> getTodoList();
	
	/**
	 * Retourne la liste des dossier cibles pour le déplacement.
	 * @return -
	 */
	List<TargetDirectory> getTargetList();
	
	/**
	 * Déplacement d'une liste de fichier dans un répertoire cible.
	 * @param request -
	 * @return -
	 */
	List<MovedFile> moveFiles(MoveRequest request);

	/**
	 * Retourne le détail d'un répertoire contenu dans l'arborescence des cibles.
	 * @param directoryToParse -
	 * @return -
	 */
	List<FileDescriptor> getDirectoryDetails(FileDescriptor directoryToParse);
	
	/**
	 * Retourne le détail d'un répertoire contenu dans l'arborescence des cibles.
	 * @param fileId -
	 * @return -
	 */
	FileDirectoryDescriptor getDirectoryDetails(final String fileId);

	/**
	 * Permet de supprimer un fichier de la todolist.
	 * @param toDelete -
	 * @return -
	 */
	List<TodoFile> deleteTodoFile(FileDescriptor toDelete);

	
	/**
	 * Permet de supprimer un fichier de la todolist.
	 * @param fileId -
	 * @return -
	 */
	List<TodoFile> deleteTodoFile(String fileId);
	
}
