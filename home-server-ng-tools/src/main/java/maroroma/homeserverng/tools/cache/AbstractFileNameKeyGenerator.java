package maroroma.homeserverng.tools.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

/**
 * Classe de base pour la génération de {@link AbstractFileNameKey}, clef spécialisée pour un cache de fichier.
 * @author rlevexie
 *
 */
public abstract class AbstractFileNameKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(final Object target, final Method method, final Object... params) {
		return generateKeyFromParams(params);
	}
	
	/**
	 * L'implémentation permet de générer un {@link StrictFileNameKey}.
	 * @param params -
	 * @return -
	 */
	protected abstract AbstractFileNameKey generateKeyFromParams(final Object... params);

}
