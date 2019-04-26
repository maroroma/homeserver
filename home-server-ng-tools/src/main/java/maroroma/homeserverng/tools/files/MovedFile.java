package maroroma.homeserverng.tools.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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

	/**
	 * Fichier final.
	 */
	private FileDescriptor targetFile;

}
