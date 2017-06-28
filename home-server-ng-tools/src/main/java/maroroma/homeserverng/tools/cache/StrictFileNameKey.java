package maroroma.homeserverng.tools.cache;

import java.io.File;

import lombok.Getter;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;

/**
 * Clef spécialisée pour la gestion du cache de type {@link TwoLevelFileCache}.
 * @author rlevexie
 *
 */
@Getter
public class StrictFileNameKey extends AbstractFileNameKey {

	/**
	 * Constructeur.
	 * @param relativePath -
	 */
	public StrictFileNameKey(final String relativePath) {
		super(relativePath);
	}
	
	/**
	 * Retourne le chemin absolu du fichier en fonction du filestore pointé par le holder.
	 * @param holder -
	 * @return -
	 */
	public File generateFile(final HomeServerPropertyHolder holder) {
		return new File(holder.asFile(), this.getRelativeFilePath());
	}
	
	/**
	 * Détermine si le fichier existe déja dans le cache de niveau 2 (ie présent sur le filesystem).
	 * @param holder -
	 * @return -
	 */
	public boolean exists(final HomeServerPropertyHolder holder) {
		return this.generateFile(holder).exists();
	}
}
