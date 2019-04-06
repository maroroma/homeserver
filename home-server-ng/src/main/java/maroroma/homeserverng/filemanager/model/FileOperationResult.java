package maroroma.homeserverng.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class FileOperationResult {
	
	/**
	 * Fichier dont on a demandé la suppression.
	 */
	private FileDescriptor initialFile;
	
	/**
	 * Détermine si l'opération sur le fichier est complétée.
	 */
	private boolean completed;
}
