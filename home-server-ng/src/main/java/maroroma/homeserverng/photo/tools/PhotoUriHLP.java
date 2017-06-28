package maroroma.homeserverng.photo.tools;

import java.io.File;

import org.springframework.util.Assert;

/**
 * Classe utilitaire simplifiant la génération d'uri pour les ressources du module de photo.
 * @author RLEVEXIE
 *
 */
public abstract class PhotoUriHLP {

	/**
	 * {@value}.
	 */
	public static final String SEPARATOR_SLASH = "/";
	
	/**
	 * {@value}.
	 */
	public static final String SEPARATOR_DASH = "-";

	/**
	 * Retourne l'uri de base pour les ressources photos.
	 * @return -
	 */
	public static String photoRootPath() {
		return "photo/years";
	}
	
	/**
	 * retourne une uri complete à partir d'une uri racine et d'un nom de fichier.
	 * <br /> ici les "-" seront remplacés par des "/"
	 * @param parentUri -
	 * @param file -
	 * @return -
	 */
	public static String resolvePath(final String parentUri, final File file) {
		Assert.notNull(file, "file can't be null");
		
		// ajout de la racine
		return new StringBuilder(PhotoUriHLP.completeParentUri(PhotoUriHLP.photoRootPath()))
				// du path parent
				.append(PhotoUriHLP.completeParentUri(parentUri))
				// du path déduit du nom de fichier
				.append(file.getName().replace(SEPARATOR_DASH, SEPARATOR_SLASH))
				.toString();
	}
	
	/**
	 * retourne une uri complete à partir d'un nom de fichier.
	 * @param file -
	 * @return -
	 */
	public static String resolvePath(final File file) {
		return PhotoUriHLP.resolvePath(null, file);
	}
	
	/**
	 * Controle l'uri en parametre et rajoute ci-besoint un {@link PhotoUriHLP#SEPARATOR_SLASH} à la fin de la chaine.
	 * @param parentUri -
	 * @return -
	 */ 
	public static String completeParentUri(final String parentUri) {
		
		// si vide on retourne rien
		if (parentUri == null || parentUri.isEmpty()) {
			return "";
		}

		// sinon controle de la fin de chaine
		if (parentUri.endsWith(SEPARATOR_SLASH)) {
			return parentUri;
		} else {
			return parentUri + SEPARATOR_SLASH;
		}
	}
}
