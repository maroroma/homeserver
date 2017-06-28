package maroroma.homeserverng.tools.cache;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;


/**
 * Définition de base d'une clef de cache intelligente pour les caches de type fichier.
 * @author rlevexie
 *
 */
@AllArgsConstructor
public abstract class AbstractFileNameKey {
	/**
	 * Chemin relatif du fichier.
	 */
	@Getter
	private String relativeFilePath;
	
	/**
	 * Retourne le chemin absolu du fichier en fonction du filestore pointé par le holder.
	 * @param holder -
	 * @return -
	 */
	public abstract File generateFile(final HomeServerPropertyHolder holder);
	
	/**
	 * Détermine si le fichier existe déja dans le cache de fichier.
	 * @param holder -
	 * @return -
	 */
	public abstract boolean exists(final HomeServerPropertyHolder holder);
}
