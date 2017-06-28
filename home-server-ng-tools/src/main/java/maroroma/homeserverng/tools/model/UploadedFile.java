package maroroma.homeserverng.tools.model;

import java.io.File;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Descriptor pour un fichier uploadé.
 * @author rlevexie
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UploadedFile extends FileDescriptor {

	/**
	 * Détermine si l'upload est ok.
	 */
	private boolean uploadOk;
	
	/**
	 * Constructeur.
	 * @param file -
	 */
	public UploadedFile(final File file) {
		this.setName(file.getName());
		this.setFullName(file.getAbsolutePath());
	}
	
}
