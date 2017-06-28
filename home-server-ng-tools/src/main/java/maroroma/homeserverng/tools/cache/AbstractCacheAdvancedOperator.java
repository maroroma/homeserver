package maroroma.homeserverng.tools.cache;


/**
 * Implémentation de base de {@link CacheAdvancedOperator}.
 * <br /> facilite le calcul de la taille du cache et la construction de son descriptor.
 * @author rlevexie
 *
 */
public abstract class AbstractCacheAdvancedOperator implements CacheAdvancedOperator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.getKeys().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheDescriptor buildCacheDescriptor() {
		return CacheDescriptor.builder()
				.name(this.getName())
				.nbElements(this.getKeys().size())
				.build();
	}

}
