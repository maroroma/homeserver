package maroroma.homeserverng.tools.repositories;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.Predicate;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Repository simplifié pour la sauvegarde de liste dans un fichier au format JSON.
 * Attention, cette implémentation n'a pas vocation à être performante, mais bien de faire le travail
 * de sauvegarde au plus simple. On est pas sur du hibernate.
 * <br /> On gère toutefois un verrou pour les accès concurrentiel sur le fichier json de persistance.
 * @author rlevexie
 * TEST
 */
@Log4j2
public class NanoRepository {

	/**
	 * Constante pour le nommage de l'objet de verrou.
	 */
	private static final String NANO_LOCK_NAME = "nanoLock";

	/**
	 * Gestion d'un verrou pour l'accès multiple au fichier.
	 */
	private final Object nanoLock = new Object();
	
	/**
	 * Type cible.
	 */
	private Class<?> targetType;

	/**
	 * Propriété indiquant l'emplacement du fichier.
	 */
	private HomeServerPropertyHolder path;

	/**
	 * Champ correspondant à l'identifiant de l'objet traité par ce repository.
	 */
	private Field idField;

	/**
	 * Identifiant du repository. Correspond à la clef de la propriété qui permet de le chargerr.
	 * @return -
	 */
	public String getId() {
		return this.path.getId();
	}
	
	/**
	 * Constructeur.
	 * @param targetType type cible du repo
	 * @param path chemin pour le fichier de persistance
	 * @param idName nom du champ correspondant de l'id pour le type donné.
	 */
	public NanoRepository(final Class<?> targetType, final String path, final String idName) {
		this(targetType, new HomeServerPropertyHolder("idMock", path), idName);
	}
	
	/**
	 * Constructeur.
	 * @param targetType type cible du repo.
	 * @param path Propriété correspondant au chemin.
	 * @param idName nom du champ correspondant de l'id pour le type donné.
	 */
	public NanoRepository(final Class<?> targetType, final HomeServerPropertyHolder path, final String idName) {
		super();
		Assert.notNull(targetType, "targetType can't be null");
		Assert.notNull(path, "path can't be null");
		Assert.hasLength(idName, "idName can't be null or empty");
		
		// récupération par réflexion du champ id du type que l'on doit gérer via ce Repo
		try {
			this.idField = targetType.getDeclaredField(idName);
			ReflectionUtils.makeAccessible(this.idField);
		} catch (NoSuchFieldException | SecurityException e) {
			log.warn("pb avec la récup du champ " + idName, e);
		}
		Assert.notNull(this.idField, "idField can't be resolved to field on " + targetType.getName());

		this.targetType = targetType;
		this.path = path;
	}

	/**
	 * Sauvegarde dans le fichier.
	 * @param toSave object à sauvegarder.
	 * @throws HomeServerException -3
	 */
	@Synchronized(NanoRepository.NANO_LOCK_NAME)
	private void saveToFile(final Object toSave) throws HomeServerException {
		Assert.notNull(toSave, "toSave can't be null");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(this.path.asFile(), toSave);
		} catch (IOException e) {
			String msg = "Erreur rencontrée lors de la persistence dans le fichier " + path.asFile().getAbsolutePath();
			log.error(msg, e);
			throw new HomeServerException(msg, e);
		}
	}

	/**
	 * Désérialisation du fichier dans la liste de bon type.
	 * @return -
	 * @param <T> type attendu
	 * @throws HomeServerException -
	 */
	@Synchronized(NanoRepository.NANO_LOCK_NAME)
	private <T> List<T> getFromFile() throws HomeServerException {
		ObjectMapper serializer = new ObjectMapper();

		List<T> returnValue = null;

		// on test avant si le fichier existe
		if (this.path.asFile().exists()) {
			try {
				returnValue = serializer.readValue(this.path.asFile(), serializer.getTypeFactory().constructCollectionType(List.class, targetType));
			} catch (IOException e) {
				String msg = "Erreur rencontrée lors de la lecture du fichier " + this.path.asFile().getAbsolutePath();
				log.warn(msg, e);
				throw new HomeServerException(msg, e);
			}
		} else {
			// sinon on retourne une liste vide.
			returnValue = new ArrayList<>();
		}

		return returnValue;
	}

	/**
	 * Retourne la liste complète des items sauvegardés dans le fichier.
	 * @return -
	 * @param <T> type attendu
	 * @throws HomeServerException -
	 */
	public <T> List<T> getAll() throws HomeServerException {
		return this.getFromFile();
	}

	/**
	 * Sauvegarde d'un item dans la liste.
	 * @param item item à sauvegarder
	 * @return -
	 * @param <T> type attendu
	 * @throws HomeServerException -
	 */
	public <T> List<T> save(final T item) throws HomeServerException {
		Assert.notNull(item, "item can't be null");
		
		Object itemId = this.getIdValue(item);
		
		Assert.notNull(itemId, "item id (" + this.idField.getName() + ") can't be null");
		
		
		List<T> toComplete = this.getAll();

		toComplete.forEach(itemToTest -> {
			Assert.isTrue(!getIdValue(itemToTest).equals(itemId), itemId + " already present");
		});
		
		
		toComplete.add(item);
		this.saveToFile(toComplete);
		return this.getAll();
	}

	/**
	 * Permet de récupérer l'identifiant d'un item en fonction du field renseigné lors de la construction du {@link NanoRepository}.
	 * @param item objet sur lequel on tente de récupérer l'identifiant.
	 * @return -
	 * @param <T> type de l'item.
	 */
	private <T> Object getIdValue(final T item) {

		Assert.notNull(item, "item can't be null");


		Object id = null;

		try {
			id = this.idField.get(item);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.warn("id " + this.idField.getName() + " unreachable", e);
		}

		Assert.notNull(id, "id is not accessible or is not set");


		return id;
	}


	/**
	 * Permet de trouver un item par son identifiant.
	 * @param id id de l'objet 
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> T find(final Object id) throws HomeServerException {
		Assert.notNull(id, "id can't be null");
		return this.find(item -> this.getIdValue(item).equals(id));
	}

	/**
	 * Permet de trouver un item selon le prédicat donné.
	 * @param predicate -
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> T find(final Predicate<T> predicate) throws HomeServerException {
		Assert.notNull(predicate, "predicate can't be null");
		List<T> toScan = this.getAll();
		for (T t : toScan) {
			if (predicate.accept(t)) {
				return t;
			}
		}
		throw new HomeServerException("item can't be found");

	}
	
	/**
	 * Permet de trouver des items selon le prédicat donné.
	 * @param predicate -
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> List<T> findAll(final Predicate<T> predicate) throws HomeServerException {
		Assert.notNull(predicate, "predicate can't be null");
		List<T> returnValue = new ArrayList<>();
		List<T> toScan = this.getAll();
		for (T t : toScan) {
			if (predicate.accept(t)) {
				returnValue.add(t);
			}
		}
		
		return returnValue;

	}

	/**
	 * Met à jour l'item donné.
	 * @param item item à remplacer dans la liste.
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> List<T> update(final T item) throws HomeServerException {
		Assert.notNull(item, "item can't be null");

		return this.update(item, itemToTest -> this.getIdValue(item).equals(this.getIdValue(itemToTest)));

	}

	/**
	 * Met à jour l'item, que l'on trouve en fonction du {@link Predicate} en entrée.
	 * @param item item à remplacer dans la liste.
	 * @param predicate -
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> List<T> update(final T item, final Predicate<T> predicate) throws HomeServerException {
		Assert.notNull(item, "item can't be null");
		Assert.notNull(predicate, "predicate can't be null");

		this.delete(predicate);
		return this.save(item);

	}

	/**
	 * Suppression d'un item en fonction de son identifiant.
	 * @param id -
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> List<T> delete(final Object id) throws HomeServerException {
		Assert.notNull(id, "id can't be null");
		return this.delete(item -> this.getIdValue(item).equals(id));
	}

	/**
	 * Suppression d'un item trouvé en fonction du {@link Predicate} en entrée.
	 * @param predicate -
	 * @return -
	 * @param <T> type de l'item.
	 * @throws HomeServerException -
	 */
	public <T> List<T> delete(final Predicate<T> predicate) throws HomeServerException {
		Assert.notNull(predicate, "predicate can't be null");


		List<T> toDecrease = this.getAll();
		List<T> toSave = new ArrayList<>();

		for (T itemToTest : toDecrease) {
			if (!predicate.accept(itemToTest)) {
				toSave.add(itemToTest);
			}
		}

		this.saveToFile(toSave);
		return this.getAll();


	}
	
	/**
	 * Retourne un {@link NanoRepositoryDescriptor} associé à ce repository.
	 * @return -
	 */
	public NanoRepositoryDescriptor generateDescriptor()  {
		return 
				NanoRepositoryDescriptor.builder()
				.file(new FileDescriptor(this.path.asFile()))
				.exists(this.path.asFile().exists())
				.propertyKey(this.path.getId()).build();
	}
	
	/**
	 * Retourne le contenu du repo au format json.
	 * @return -
	 * @throws HomeServerException -
	 */
	public String readAsJson() throws HomeServerException {
		try {
			return new ObjectMapper().writeValueAsString(this.getAll());
		} catch (JsonProcessingException | HomeServerException e) {
			throw new HomeServerException("Erreur de la lecture au format json");
		}
	}
	
}
