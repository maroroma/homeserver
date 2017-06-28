package maroroma.homeserverng.tools.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente le résultat d'un déplacement de fichier.
 * @author rlevexie
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovedFile {

	/**
	 * Fichier déplacé.
	 */
	private FileToMoveDescriptor sourceFile;
	
	/**
	 * Emplacement final du fichier déplacé.
	 */
	private String finalPath;
	
	/**
	 * Indique si le fichier a bien pu être déplacé.
	 */
	private boolean success;
	
}
