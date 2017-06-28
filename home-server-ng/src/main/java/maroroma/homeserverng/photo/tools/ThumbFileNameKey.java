package maroroma.homeserverng.photo.tools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import maroroma.homeserverng.photo.needers.PhotoCacheNeeder;
import maroroma.homeserverng.tools.cache.AbstractFileNameKey;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;

/**
 * Clef de cache spécifique à la gestion des miniatures. Est utilisée pour le cache {@link PhotoCacheNeeder#THUMB_CACHE}.
 * <br /> cette clef est non bijective : elle permet de récupérer des éléments pour un nom approchant la clef d'insertion.
 * <br /> Elle permet en gros de récupérer les miniatures pour les mois, alors que celles-ci ont été générées pour un jour donné.
 * @author rlevexie
 *
 */
public class ThumbFileNameKey extends AbstractFileNameKey {

	/**
	 * Constructeur.
	 * @param fields -
	 */
	public ThumbFileNameKey(final int... fields) {
		super(PhotoNamesDateHLP.generateThumbName(fields));
	}
	
	@Override
	public File generateFile(final HomeServerPropertyHolder holder) {
		
		// récup de la liste de fichiers.
		List<File> files = this.generateFileMatchingList(holder);
		
		// 2 cas :
		// si des fichiers sont présents dans la liste, on retourne le premier
		if (!files.isEmpty()) {
			return files.get(0);
		} else {
			// sinon on retourne un nom généré
			return new File(holder.asFile(), this.getRelativeFilePath());
		}
		
		// ceci permet de gérer le cas de la récupération de la miniature ou sa génération
	}

	@Override
	public boolean exists(final HomeServerPropertyHolder holder) {
		return !generateFileMatchingList(holder).isEmpty();
	}

	/**
	 * Permet de lister le fichier correspondant aux champs demandés.
	 * @param holder -
	 * @return -
	 */
	private List<File> generateFileMatchingList(final HomeServerPropertyHolder holder) {
		return Arrays.asList(
				holder.asFile().listFiles(
				CommonFileFilter.fileStartWithFilter(this.getRelativeFilePath())));
	}

}
