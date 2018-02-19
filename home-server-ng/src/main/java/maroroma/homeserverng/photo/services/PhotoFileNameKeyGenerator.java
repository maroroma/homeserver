package maroroma.homeserverng.photo.services;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.cache.AbstractFileNameKeyGenerator;
import maroroma.homeserverng.tools.cache.StrictFileNameKey;

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
		
		String sb = Arrays.asList(params)
			.stream()
			.map(param -> param.toString() + "-")
			.collect(Collectors.joining());
		
		return new StrictFileNameKey(sb);
//		int year, int month, int day, String id, PhotoResolution resolution
	}

}
