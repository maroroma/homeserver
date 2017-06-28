package maroroma.homeserverng.tools.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Requête de déplacement.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {

	/**
	 * Liste des fichiers à déplacer.
	 */
	private List<FileToMoveDescriptor> filesToMove;
	
	/**
	 * Répertoire cible.
	 */
	private FileDescriptor target;
	
}
