package maroroma.homeserverng.photo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maroroma.homeserverng.photo.tools.PhotoUriHLP;
import maroroma.homeserverng.tools.files.DirectoryDescriptor;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;

import java.io.File;
import java.time.LocalDate;

/**
 * Implémentation abstraite de {@link DirectoryDescriptor} pour une spécialisation de la gestion des photos.
 * @author rlevexie
 *
 * @param <T>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"fullName"
//		, "httpRessource"
		})
public abstract class AbstractPhotoDirectoryDescriptor<T extends FileDescriptor, U extends AbstractPhotoDirectoryDescriptor<T, U>> 
extends DirectoryDescriptor<T> {

	/**
	 * Uri vers la miniature représentative de ce {@link AbstractPhotoDirectoryDescriptor}.
	 */
	private String thumbnailResource;
	
	/**
	 * Date correspondante du répertoire. Permet d'organiser les photos par années, mois et jour.
	 */
	private LocalDate date = LocalDate.now();
	
	/**
	 * Type de répertoire.
	 */
	private PhotoDirectoryType directoryType;
	
	/**
	 * Constructeur simplle.
	 * @param type type de répertoire, cadré par {@link PhotoDirectoryType}
	 */
	public AbstractPhotoDirectoryDescriptor(final PhotoDirectoryType type) {
		super();
		this.directoryType = type;
	}

	/**
	 * Constructeur.
	 * @param file -
	 * @param type type de répertoire, cadré par {@link PhotoDirectoryType}
	 */
	public AbstractPhotoDirectoryDescriptor(final File file, final PhotoDirectoryType type) {
		super(file);
		this.directoryType = type;
		// récupération de la date correspondant au nom de fichier
		this.date = doParseNameToDate();
		// résolution de l'accessibilité de répertoire à travers une ressource http.
		// facilite la navigation dans les ressources pour les clients.
		this.setHttpRessource(PhotoUriHLP.resolvePath(file));
	}
	
	/**
	 * Méthode permettant de transformer le nom de fichier en date équivalente.
	 * @return -
	 */
	protected abstract LocalDate doParseNameToDate();

	/**
	 * Méthode permettant de peupler les sous éléments du répertoire.
	 * @return this
	 */
	public U autoPopulate() {
		// lance la population du répertoire à partir du fichier courant
		this.populate(CommonFileFilter.listDirectories(this.createFile()));
		return returnThis();
	}
	
	/**
	 * -.
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	protected U returnThis() {
		return (U) this;
	}

}
