package maroroma.homeserverng.tools.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
