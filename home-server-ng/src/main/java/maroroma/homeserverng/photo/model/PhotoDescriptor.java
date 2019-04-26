package maroroma.homeserverng.photo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import maroroma.homeserverng.tools.files.FileDescriptor;

import java.io.File;

/**
 * Permet de décrire une photo.
 * @author RLEVEXIE
 *
 */
@JsonIgnoreProperties(value = {"fullName"})
public class PhotoDescriptor extends FileDescriptor {

	/**
	 * constructeur.
	 * @param source -
	 */
	public PhotoDescriptor(final File source) {
		super(source);
	}

	
	
}
