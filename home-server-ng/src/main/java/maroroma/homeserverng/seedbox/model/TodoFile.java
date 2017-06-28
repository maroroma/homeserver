package maroroma.homeserverng.seedbox.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * DTO pour la gestion des fichiers de la todolist.
 * @author rlevexie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TodoFile extends FileDescriptor {

	/**
	 * Détermine si le fichier a été détecté comme nouveau.
	 */
	private boolean isNew;
	
	/**
	 * Constructeur.
	 * @param source -
	 * @param isnew -
	 */
	public TodoFile(final FileDescriptor source, final boolean isnew) {
		super(source);
		this.isNew = isnew;
	}
}
