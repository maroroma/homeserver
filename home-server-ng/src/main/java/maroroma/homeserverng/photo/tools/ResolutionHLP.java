package maroroma.homeserverng.photo.tools;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.photo.model.PhotoResolution;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.FluentMap;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Permet de résoudre la résolution attendue en fonction de l'énumération {@link PhotoResolution}.
 * <br /> Le passage par un composant spring permet de le rendre paramétrable.
 * @author rlevexie
 *
 */
@Component
public class ResolutionHLP {

	/**
	 * Nom de la propriétés pour la résolution des miniatures.
	 */
	private static final String HOMESERVER_PHOTO_RESOLUTION_THUMB = "homeserver.photo.resolution.thumb";

	/**
	 * Nom de la propriétés pour la résolution des images moyenne.
	 */
	private static final String HOMESERVER_PHOTO_RESOLUTION_MEDIUM = "homeserver.photo.resolution.medium";

	/**
	 * HOlder pour les couples {@link PhotoResolution} / {@link Tuple} de dimension.
	 */
	private FluentMap<PhotoResolution, HomeServerPropertyHolder> resolutionMappong;
	
	/**
	 * Porte les résolutions.
	 */
	@Property(HOMESERVER_PHOTO_RESOLUTION_MEDIUM)
	private HomeServerPropertyHolder resolutionMedium;
	
	/**
	 * Porte les résolutions.
	 */
	@Property(HOMESERVER_PHOTO_RESOLUTION_THUMB)
	private HomeServerPropertyHolder resolutionThumb;
	
	/**
	 * Méthode d'initialisation.
	 */
	@PostConstruct
	private void initMethod() {
		
		this.resolutionMappong =
				FluentMap.<PhotoResolution, HomeServerPropertyHolder>create()
				.add(PhotoResolution.MEDIUM, this.resolutionMedium)
				.add(PhotoResolution.THUMB, this.resolutionThumb);
	}
	
	/**
	 * REtourne la dimension correspondant à la résolution attendue.
	 * @param resolution -
	 * @return -
	 */
	public Tuple<Integer, Integer> getConcreteResolution(final PhotoResolution resolution) {
		return this.resolutionMappong.get(resolution).asDimension();
	}
	
}
