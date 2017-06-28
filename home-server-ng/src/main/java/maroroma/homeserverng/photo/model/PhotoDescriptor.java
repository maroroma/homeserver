package maroroma.homeserverng.photo.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import maroroma.homeserverng.tools.model.FileDescriptor;

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
	 * @param parentUri -
	 */
	public PhotoDescriptor(final File source, final String parentUri) {
		super(source, parentUri);
	}

	
	
}
