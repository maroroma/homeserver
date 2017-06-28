package maroroma.homeserverng.tools.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FluentList;


/**
 * Implémentation de {@link Cache} permettant de gérer un cache de deux niveau pour des fichiers :
 * <br /> Le niveau 1 correspondant à un cache mémoire
 * <br /> Le niveau 2 correspondant à un cache fichier, porté ici par  {@link FileCache}.
 * @author rlevexie
 *
 */
@Log4j2
public class TwoLevelFileCache extends AbstractCacheAdvancedOperator implements Cache, CacheAdvancedOperator {

	/**
	 * Nom du cache.
	 */
	private String cacheName;

	/**
	 * CAche de niveau 2.
	 */
	private FileCache layer2Cache;
	
	/**
	 * Cache de niveau 1.
	 */
	private Map<String, byte[]> layer1Cache;

	/**
	 * Constructeur.
	 * @param name nom du cache
	 * @param fileStore emplacement pour l'écriture du cache de niveau 2
	 */
	public TwoLevelFileCache(final String name, final HomeServerPropertyHolder fileStore) {
		Assert.hasLength(name, "name can't be null or empty");
		Assert.notNull(fileStore, "filestore can't be null");
		this.cacheName = name;
		this.layer2Cache = new FileCache("LEVEL2_IMPL_" + name, fileStore);
		this.layer1Cache = new ConcurrentHashMap<>();
	}

	@Override
	public String getName() {
		return this.cacheName;
	}

	@Override
	public Object getNativeCache() {
		return this.layer1Cache;
	}

	@Override
	public ValueWrapper get(final Object key) {
		// récupération de la clef
		AbstractFileNameKey fileKey = validateKey(key);
		byte[] returnValue = null;

		// si la clef est exploitable
		if (fileKey != null) {

			// cache de niveau 1 : mémoire
			returnValue = this.getFromLevel1(fileKey);

			// si absent du cache de niveau 2 : filestore
			if (returnValue == null) {
				returnValue = this.getFromLevel2(fileKey);

				// si présent dans le cache de niveau 2, on met à jour le cache de niveau 1.
				if (returnValue != null) {
					this.putToLevel1(fileKey, returnValue);
				}
			}

		} else {
			log.warn("la clef n'est pas de type FileNameKey");
		}

		if (returnValue == null) {
			return null;
		} else {
			return new SimpleValueWrapper(returnValue);
		}
	}

	/**
	 * Rajoute l'élément dans le cache mémoire de niveau 1.
	 * @param key -
	 * @param data -
	 */
	protected void putToLevel1(final AbstractFileNameKey key, final byte[] data) {
		this.layer1Cache.put(key.getRelativeFilePath(), data);
	}

	/**
	 * Rajoute l'élément dans le cache fichier de niveau 2.
	 * @param key -
	 * @param data -
	 */
	protected void putToLevel2(final AbstractFileNameKey key, final byte[] data) {
		this.layer2Cache.put(key, data);
	}

	/**
	 * Obtient l'élément dans le cache de niveau 1 si présent. 
	 * @param key -
	 * @return null si absent
	 */
	protected byte[] getFromLevel1(final AbstractFileNameKey key) {
		if (this.layer1Cache.containsKey(key.getRelativeFilePath())) {
			return layer1Cache.get(key);
		} else {
			return null;
		}
	}

	/**
	 * Obtient l'élément dans le cache de niveau 2 si présent.
	 * @param key - 
	 * @return null si absent.
	 */
	protected byte[] getFromLevel2(final AbstractFileNameKey key) {
		ValueWrapper wrapper =  this.layer2Cache.get(key);
		if (wrapper != null) {
			return (byte[]) wrapper.get();
		} else {
			return null;
		}
	}

	@Override
	public <T> T get(final Object key, final Class<T> type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T get(final Object key, final Callable<T> valueLoader) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(final Object key, final Object value) {
		AbstractFileNameKey fileKey = validateKey(key);
		Assert.isInstanceOf(byte[].class, value, "value must be a byte array");
		if (fileKey != null) {
			this.putToLevel1(fileKey, (byte[]) value);
			this.putToLevel2(fileKey, (byte[]) value);
		} else {
			log.warn("la clef n'est pas de type FileNameKey");
		}
	}

	@Override
	public ValueWrapper putIfAbsent(final Object key, final Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void evict(final Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		this.clearLevel1();
		this.clearLevel2();
	}


	/**
	 * Nettoyage du cache de level 2.
	 */
	private void clearLevel2() {
		this.layer2Cache.clear();
	}

	/**
	 * Nettoyage du cache de level1.
	 */
	private void clearLevel1() {
		this.layer1Cache.clear();
	}

	/**
	 * Permet de retourner la clef pour ce type de cache si bien dans le type attendu.
	 * @param key clef à convertir
	 * @return null si non convertible en {@link AbstractFileNameKey}.
	 */
	private AbstractFileNameKey validateKey(final Object key) {
		if (key instanceof AbstractFileNameKey) {
			return (AbstractFileNameKey) key;
		} else {
			return null;
		}
	}


	@Override
	public List<CacheKeyDescriptor> getKeys() {
		
		// récupération des clefs du niveau 2
		List<CacheKeyDescriptor> globalList = this.layer2Cache.getKeys();
		
		// récupération des clefs du niveau 1, qui nécessite une transformation à partir du string de base
		globalList.addAll(
				this.layer1Cache.keySet().stream()
				.map(key -> CacheKeyDescriptor.builder()
						.value(new FluentList<String>().addAnd(key))
						.type(CacheKeyDescriptorType.STRING_KEY).build())
				.collect(Collectors.toList()));
		
		// suppression des doublons
		return globalList.stream()
				.distinct().collect(Collectors.toList());
	}


}
