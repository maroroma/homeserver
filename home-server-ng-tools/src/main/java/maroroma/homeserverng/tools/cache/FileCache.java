package maroroma.homeserverng.tools.cache;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.helpers.FluentList;

/**
 * Implémentation de base d'un cache de fichier.
 * La gestion du nom de fichier tel que enregistrer dans le répertoire dépend
 * de l'implémentation de la {@link AbstractFileNameKey} sous jacente à l'emploi de ce cache.
 * @author rlevexie
 *
 */
@Log4j2
public class FileCache  extends AbstractCacheAdvancedOperator implements Cache, CacheAdvancedOperator {

	/**
	 * Nom du cache.
	 */
	private String cacheName;

	/**
	 * Emplacement du répertoire pour le cache de fichier.
	 */
	private HomeServerPropertyHolder cacheDirectory;

	/**
	 * Constructeur.
	 * @param name nom du cache
	 * @param fileStore emplacement du cache fichier.
	 */
	public FileCache(final String name, final HomeServerPropertyHolder fileStore) {
		Assert.hasLength(name, "name can't be null or empty");
		Assert.notNull(fileStore, "filestore can't be null");
		this.cacheName = name;
		this.cacheDirectory = fileStore;
	}
	
	@Override
	public List<CacheKeyDescriptor> getKeys() {
		return 
				// on récupère l'ensemble des noms de fichiers que l'on converti en descriptor.
				Arrays.asList(this.cacheDirectory.asFile().list())
				.stream().map(key -> CacheKeyDescriptor.builder()
						.value(new FluentList<String>().addAnd(key))
						.type(CacheKeyDescriptorType.STRING_KEY).build()).collect(Collectors.toList());
	}

	@Override
	public String getName() {
		return this.cacheName;
	}

	@Override
	public Object getNativeCache() {
		return this.cacheDirectory.asFile();
	}

	@Override
	public ValueWrapper get(final Object key) {

		// on tente de récupérer une clef exploitable
		AbstractFileNameKey fileKey = validateKey(key);

		byte[] rawValue = null;

		if (fileKey != null) {
			// si clef du bon type, on essaye de lire le fichier sur le disque.
			rawValue = getFromDirectory(fileKey);
		}

		// si fichier trouvé, encapsulation et retour.
		if (rawValue != null) {
			return new SimpleValueWrapper(rawValue);
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
			this.putToDirectory(fileKey, (byte[]) value);
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
		File directoryToDelete = this.cacheDirectory.asFile();
		if (directoryToDelete.exists()) {
			FileAndDirectoryHLP.deleteGenericFile(directoryToDelete.listFiles());
		}
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

	/**
	 * Obtient l'élément dans le cache si présent.
	 * @param key - 
	 * @return null si absent.
	 */
	protected byte[] getFromDirectory(final AbstractFileNameKey key) {
		if (key.exists(this.cacheDirectory)) {
			try {
				return FileAndDirectoryHLP.convertFileToByteArray(key.generateFile(this.cacheDirectory));
			} catch (HomeServerException e) {
				log.warn("Impossible de récupérer le fichier " + key.generateFile(cacheDirectory).getAbsolutePath(), e);
			}
		}

		return null;
	}

	/**
	 * Rajoute l'élément dans le cache fichier.
	 * @param key -
	 * @param data -
	 */
	protected void putToDirectory(final AbstractFileNameKey key, final byte[] data) {
		try {
			FileAndDirectoryHLP.convertByteArrayToFile(data, key.generateFile(this.cacheDirectory));
		} catch (HomeServerException e) {
			log.warn("Impossible de d'écrire le fichier " + key.generateFile(cacheDirectory).getAbsolutePath(), e);
		}
	}

}
