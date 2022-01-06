package maroroma.homeserverng.tools.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire de liste de {@link ManagedSseEmitter}.
 * Permet de centraliser la gestion de l'ajout, la suppression de {@link ManagedSseEmitter} avec leur cycle de vie liée à leur completion.
 * @author RLEVEXIE
 *
 */
@Component
@Scope(value = "prototype")
@Slf4j
public class ManagedSseEmitterCollection {

	/**
	 * Liste des {@link ManagedSseEmitter}.
	 */
	private Map<String, ManagedSseEmitter> emitters = new ConcurrentHashMap<>();
	
	
	/**
	 * Crée un {@link ManagedSseEmitter}, le remplaçant le cas échéant si prééxistant.
	 * @param identifiant identifiant du {@link ManagedSseEmitter} à créé.
	 * @return La promesse correspondant à la création effective du {@link ManagedSseEmitter}.
	 */
	public ManagedSseEmiterCreationHandler createOrReplace(final String identifiant) {
		
		ManagedSseEmitter newEmitter = createSseEmitter(identifiant);

		// création du handler, qui sera retourné par la fonction
		// il permet de s'assurer que le SSEEMitter sous jacent à bien été créé en cas de destruction (asynchrone) et reconstruction)
		ManagedSseEmiterCreationHandler returnValue = new ManagedSseEmiterCreationHandler();
		
		if (this.containsEmitter(newEmitter)) {
			log.info("emitter [" + identifiant + "] already exists : replace");
			this.replaceEmitter(newEmitter, returnValue);
		} else {
			log.info("emitter [" + identifiant + "] doesnt' exists : create");
			this.addEmitter(newEmitter, returnValue);
		}
		
		
		return returnValue;
	}
	
	/**
	 * Retourne les {@link ManagedSseEmitter} sous forme de liste.
	 * @return -
	 */
	public List<ManagedSseEmitter> getEmitters() {
		return Collections.unmodifiableList(new ArrayList<>(emitters.values()));
	}
	
	/**
	 * Remplace le {@link ManagedSseEmitter} prééxistant par newEmitter.
	 * @param newEmitter -
	 * @param returnValue 
	 */
	protected void replaceEmitter(final ManagedSseEmitter newEmitter, final ManagedSseEmiterCreationHandler returnValue) {
		
		// récup de l'ancien emitter
		ManagedSseEmitter oldEmitter = this.getEmitter(newEmitter.getId());
		
		// demande explicite de completion, avec gestion par callback de l'ajout du nouvel
		// emitter dans la collection en remplacement.
		oldEmitter.complete(new ManagedSseEmitterEventHandler() {
			
			@Override
			public void onEvent(final String typeEvent, final ManagedSseEmitter emitter) {
				log.info("ManagedSseEmitterCollection::onCompletion - Completion of handler [" + emitter + "] - emitter is replaced in collection");
				addEmitter(newEmitter, returnValue);
			}
		});
		
	}
	
	/**
	 * Ajout d'un nouvel {@link ManagedSseEmitter} dans la collection.
	 * @param newEmitter -
	 * @param returnValue -
	 */
	protected void addEmitter(final ManagedSseEmitter newEmitter, final ManagedSseEmiterCreationHandler returnValue) {
		Assert.notNull(newEmitter, "newEmitter can't be null");
		Assert.hasLength(newEmitter.getId(), "newEmitter.id can't be null or empty");
		this.emitters.put(newEmitter.getId(), newEmitter);
		
		// on s'assure de débloquer le code en attente de la fin de création du ManagedSSEEmitter
		returnValue.complete(newEmitter);
	}

	/**
	 * Retourne le {@link ManagedSseEmitter} d'identifiant donné.
	 * @param id -
	 * @return -
	 */
	public ManagedSseEmitter getEmitter(final String id) {
		Assert.hasLength(id, "id must be fullfilled");
		Assert.isTrue(this.containsEmitter(id), "no emitter of id [" + id + "] in collection");
		return this.emitters.get(id);
	}

	/**
	 * Détermine si emitter est présent dans la collection.
	 * @param emitter -
	 * @return -
	 */
	public boolean containsEmitter(final ManagedSseEmitter emitter) {
		Assert.notNull(emitter, "emitter can't be null");
		return this.containsEmitter(emitter.getId());
	}
	
	/**
	 * Détermine si la collection dispose d'un emitter d'identifiant donné.
	 * @param id -
	 * @return -
	 */
	public boolean containsEmitter(final String id) {
		Assert.hasLength(id, "id can't be null or emty");
		
		return this.emitters.containsKey(id);
		
	}
	
	/**
	 * Créée un nouvel {@link ManagedSseEmitter} pour l'identifiant donné.
	 * Abonne automatiquement pour la completion pour la suppression de la collection.
	 * @param identifiant -
	 * @return -
	 */
	protected ManagedSseEmitter createSseEmitter(final String identifiant) {
		ManagedSseEmitter returnValue = new ManagedSseEmitter(identifiant);
		
		return returnValue.onCompletion(new ManagedSseEmitterEventHandler() {
			
			@Override
			public void onEvent(final String typeEvent, final ManagedSseEmitter emitter) {
				log.info("ManagedSseEmitterCollection::onCompletion - Completion of handler [" + emitter + "] - emitter is removed from collection");
				emitters.remove(emitter.getId());
			}
		});
		
	}
	
	/**
	 * Permet de supprimer un emitter d'identifiant donné.
	 * @param identifiant id de l'emitter à virer
	 */
	public void removeEmitter(final String identifiant) {
		if (this.containsEmitter(identifiant)) {
			this.getEmitter(identifiant).complete();
		}
	}
	
	/**
	 * Détermine si aucun emitter est présent.
	 * @return -
	 */
	public boolean isEmpty() {
		return this.emitters == null || this.emitters.isEmpty();
	}
	
	
}
