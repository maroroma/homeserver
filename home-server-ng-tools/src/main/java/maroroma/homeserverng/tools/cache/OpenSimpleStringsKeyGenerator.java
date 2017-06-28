package maroroma.homeserverng.tools.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

/**
 * Générateur de clef par défaut pour le homeserver.
 * @author rlevexie
 *
 */
public class OpenSimpleStringsKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(final Object target, final Method method, final Object... params) {
		if (params.length == 0) {
			return OpenSimpleStringsKey.EMPTY;
		} else {
			return new OpenSimpleStringsKey(params);
		}
	}

}
