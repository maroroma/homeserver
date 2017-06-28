package maroroma.homeserverng.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Résultat de la suppression d'un fichier.
 * @author rlevexie
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDeletionResult {
	
	/**
	 * Fichier dont on a demandé la suppression.
	 */
	private FileDescriptor initialFile;
	
	/**
	 * Détermine si le fichier est supprimé.
	 */
	private boolean deleted;
}
