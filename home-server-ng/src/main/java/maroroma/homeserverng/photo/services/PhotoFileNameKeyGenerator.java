package maroroma.homeserverng.photo.services;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.cache.AbstractFileNameKeyGenerator;
import maroroma.homeserverng.tools.cache.StrictFileNameKey;
import maroroma.homeserverng.tools.helpers.CustomCollectors;

/**
 * Permet de générer un {@link StrictFileNameKey} à partir des paramètres de la méthode de récupération
 * d'une photo.
 * @author RLEVEXIE
 *
 */
@Component("photoFileNameKeyGenerator")
public class PhotoFileNameKeyGenerator extends AbstractFileNameKeyGenerator {

	@Override
	protected StrictFileNameKey generateKeyFromParams(final Object... params) {
		
		StringBuilder sb = Arrays.asList(params)
			.stream()
			.map(param -> param.toString() + "-")
			.collect(CustomCollectors.toConcatenatedString());
		
		return new StrictFileNameKey(sb.toString());
//		int year, int month, int day, String id, PhotoResolution resolution
	}

}
