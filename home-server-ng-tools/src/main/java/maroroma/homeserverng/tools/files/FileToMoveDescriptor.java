package maroroma.homeserverng.tools.files;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Descriptor pour un fichier à déplacer.
 * @author rlevexie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileToMoveDescriptor extends FileDescriptor {

	/**
	 * Nouveau nom pour le fichier déplacé.
	 */
	private String newName;
}
