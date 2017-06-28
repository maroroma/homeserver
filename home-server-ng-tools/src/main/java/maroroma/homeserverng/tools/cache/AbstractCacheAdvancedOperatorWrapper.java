package maroroma.homeserverng.tools.cache;

import org.springframework.cache.Cache;

import lombok.Getter;

/**
 * Spécialisation de {@link CacheAdvancedOperator} pour la gestion de cache non spécifique au HomeServer.
 * <br /> Permet d'offrir quelques fonctionnalités de base sur la base du {@link #getWrapped()}.
 * @author rlevexie
 *
 * @param <T> implémentation de {@link Cache} sous jacent à ce {@link AbstractCacheAdvancedOperatorWrapper}
 */
public abstract class AbstractCacheAdvancedOperatorWrapper<T extends Cache> extends AbstractCacheAdvancedOperator implements CacheAdvancedOperator {

	/**
	 * Cache sous jacent.
	 */
	@Getter
	private T wrapped;
	
	/**
	 * Constructeur.
	 * @param cache cache encapsulé
	 */
	protected AbstractCacheAdvancedOperatorWrapper(final Cache cache) {
		this.wrapped = this.validateAndCast(cache);
	}
	
	/**
	 * Permet de récupérer le cache dans le type voulu.
	 * @param cache -
	 * @return -
	 */
	protected abstract T validateAndCast(Cache cache);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.wrapped.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.wrapped.clear();
	}
	
	
	

}
