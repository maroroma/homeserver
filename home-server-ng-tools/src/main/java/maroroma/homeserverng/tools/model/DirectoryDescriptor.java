package maroroma.homeserverng.tools.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Extension de {@link FileDescriptor} correspondant à un répertoire.
 * <br /> Permet de gérer une sous collection d'extension de {@link FileDescriptor}.
 * @author rlevexie
 *
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DirectoryDescriptor<T extends FileDescriptor> extends FileDescriptor {

	/**
	 * Liste de sous éléments.
	 */
	private List<T> subElements;

	/**
	 * Constructeur.
	 * @param source -
	 */
	public DirectoryDescriptor(final FileDescriptor source) {
		super(source);
	}

	/**
	 * Constructeur.
	 */
	public DirectoryDescriptor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructeur.
	 * @param file -
	 */
	public DirectoryDescriptor(final File file) {
		super(file);
	}

	/**
	 * Permet de peupler la collection {@link DirectoryDescriptor#subElements} avec des extensions de {@link FileDescriptor}.
	 * <br /> Les éléments sont ajoutés dans l'ordre alphabétique.
	 * @param liste de fichiers à utiliser pour l'alimention de {@link DirectoryDescriptor#subElements}
	 * @param mapper {@link Function} utilisée pour transformer un {@link File} en extension 
	 * 			de {@link FileDescriptor} gérée par cette instance de {@link DirectoryDescriptor}.
	 */
	protected void populate(final List<File> liste, final Function<File, T> mapper) {
		this.subElements = liste.parallelStream()
				// transformation
				.map(mapper)
				// tri par nom de fichier
				// retransformation en liste.
				.sorted((f1, f2) -> f1.getName().compareTo(f2.getName()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Permet de peupler la collection {@link DirectoryDescriptor#subElements} avec des extensions de {@link FileDescriptor}.
	 * <br /> Les éléments sont ajoutés dans l'ordre alphabétique.
	 * @param liste de fichiers à utiliser pour l'alimention de {@link DirectoryDescriptor#subElements}
	 * @param mapper {@link Function} utilisée pour transformer un {@link File} en extension 
	 * 			de {@link FileDescriptor} gérée par cette instance de {@link DirectoryDescriptor}.
	 */
	protected void populate(final File[] liste, final Function<File, T> mapper) {
		this.populate(Arrays.asList(liste), mapper);
	}
	
	/**
	 * Permet de peupler la collection {@link DirectoryDescriptor#subElements} avec des extensions de {@link FileDescriptor}.
	 * Ici la transformation des {@link File} est réalisée directement par l'implémentation.
	 * @param liste -
	 */
	public void populate(final File[] liste) {
		this.populate(Arrays.asList(liste));
	}

	/**
	 * Permet de peupler la collection {@link DirectoryDescriptor#subElements} avec des extensions de {@link FileDescriptor}.
	 * Ici la transformation des {@link File} est réalisée directement par l'implémentation.
	 * @param asList -
	 */
	public abstract void populate(List<File> asList);
	
	
}
