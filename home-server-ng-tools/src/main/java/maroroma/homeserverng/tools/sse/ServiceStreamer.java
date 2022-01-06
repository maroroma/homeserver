package maroroma.homeserverng.tools.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.Streamed;
import maroroma.homeserverng.tools.config.HomeServerPluginPropertiesManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.SSEStreamableException;
import maroroma.homeserverng.tools.helpers.Assert;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Service permettant d'encapulser un bean implémentant {@link SSEStreamable}, avec l'utilisation 
 * obligatoire de l'annotation {@link Streamed} sur la méthode {@link SSEStreamable#process()}, annotation permettant
 * de piloter la fréquence et le type d'event émis à travers les {@link ManagedSseEmitter}.
 * @author rlevexie
 *
 * @param <T> {@link SSEStreamable}.
 */
@Component
@Scope("prototype")
@Slf4j
public class ServiceStreamer<T extends SSEStreamable> {

	/**
	 * Container de {@link ManagedSseEmitter}.
	 */
	@Autowired
	private ManagedSseEmitterCollection emitters;

	/**
	 * Permet de récupérer les {@link HomeServerPropertyHolder}.
	 */
	@Autowired
	private HomeServerPluginPropertiesManager propertiesManager;

	/**
	 * Gestion d'un verrou pour l'accès multiple au currentTask.
	 */
	private final Object currentTaskLock = new Object();

	/**
	 * Propriété correspondant à la fréquence d'appel du {@link SSEStreamable} et de l'émission des events.
	 */
	private HomeServerPropertyHolder fixedDelayProperty;

	/**
	 * TAche en cours avec le délai fixé pour l'appel du {@link SSEStreamable}.
	 */
	private ScheduledFuture<?> currentTask;
	
	/**
	 * Porte les informations pour le scheduling (fréquence, events, serialisation).
	 */
	private Streamed streamedAnnotation;

	/**
	 * Service implémentant {@link SSEStreamable} utisé à fréquence fixe pour appel et émission automatique à travers les {@link ManagedSseEmitter}.
	 */
	@Autowired
	private T streamableService;

	/**
	 * Retourne le {@link SSEStreamable} sous jacent.
	 * @return -
	 */
	public T getStreamableService() {
		return streamableService;
	}

	/**
	 * Schéduler des taches.
	 */
	private TaskScheduler taskScheduler;

	/**
	 * gestionnaire de thread.
	 */
	private ScheduledExecutorService localExecutor;

	/**
	 * Souscription aux events produits suite aux appels récurrents du {@link SSEStreamable}.
	 * Si la souscription est la première, on démarre automatiquement la programmation récurrente de l'appel au {@link SSEStreamable}.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	public SseEmitter subscribe(final String id) throws HomeServerException {
		ManagedSseEmitter emitter;
		
		try {
			// demande de créatioin de l'emitter
			emitter = this.emitters.createOrReplace(id).await();
			
			// abonnement à la fin de vie de l'emitter, pour controller qu'on arrete pas toutes les taches.
			emitter.onCompletion((type, stopedEmitter) -> {
				this.stopIfNecessary();
			});
			
			// démarrage de la programmation
			startScheduleIfNecessary();
		} catch (InterruptedException e) {
			String msg = "Erreur lors de la création de l'emitter";
			log.error(msg);
			throw new HomeServerException(msg, e);
		}

		return emitter.getInnerEmitter();
	}

	/**
	 * Détermine si la programmation de l'appel à {@link SSEStreamable#process()} doit être lancée.
	 */
	@Synchronized("currentTaskLock")
	private void startScheduleIfNecessary() {

		// si la tache est null, on peut lancer une programmation, celle-ci étant inexistante
		// on conditionne aussi sur le fait qu'il y a bien des cliens à alimenter.
		if (this.currentTask == null && !this.emitters.isEmpty()) {

			// si le scheduler est absent, on le crée aussi
			if (this.taskScheduler == null) {
				this.localExecutor = Executors.newSingleThreadScheduledExecutor();
				this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
			}

			// encapsulation de notre tache.
			// permet de spécifier un intervalle d'éxécution
			IntervalTask task = new IntervalTask(new Runnable() {
				@Override
				public void run() {
					
					try {
						// appel du service
						Object processReturnValue = streamableService.process();
						
						// construction de l'event
						SseEventBuilder builder = SseEmitter
								.event()
								.name(streamedAnnotation.eventName())
								.id("");
						builder = SseEventBuilderHelper.prepareForMediaType(builder, processReturnValue, streamedAnnotation.mediaType());
						
						// émission
						for (ManagedSseEmitter emiter : emitters.getEmitters()) {
							emiter.send(builder);
						}
					} catch (JsonProcessingException e) {
						log.warn("erreur lors de la préparation des données", e);
					} catch (IOException e) {
						log.warn("erreur lors de l'émission de l'event", e);
					} catch (SSEStreamableException e) {
						log.warn("erreur rencontrée lors de l'appel du service streamble", e);
					}	
					
					
				}
				// définition de l'intervalle / fréquence d'appel, porté par les propriétés
			}, this.fixedDelayProperty.asInt());

			// création de la tache et lancement
			this.currentTask = this.taskScheduler.scheduleAtFixedRate(task.getRunnable(), task.getInterval());
			log.info("Démarrage du streaming pour le service " + this.streamableService.getClass().getSimpleName());
			
		}
	}

	
	/**
	 * Désabonnements à l'émission du résultat des appels du {@link SSEStreamable}.
	 * @param id -
	 */
	public void unsuscribe(final String id) {
		this.emitters.removeEmitter(id);
		
		stopIfNecessary();
	}
	
	/**
	 * Si plus aucun {@link ManagedSseEmitter} n'est présent, arrêt de la tache programmée.
	 */
	@Synchronized("currentTaskLock")
	private void stopIfNecessary() {
		if (this.emitters.isEmpty() && this.currentTask != null) {
			log.info("plus de clients : stop du streaming pour le service " + this.streamableService.getClass().getSimpleName());
			this.currentTask.cancel(true);
			this.currentTask = null;
		}
	}
	
	/**
	 * Force l'arrêt de la programmation de la tache récurrente.
	 */
	@Synchronized("currentTaskLock")
	private void forceStop() {
		if (this.currentTask != null) {
			log.info("Stop forcé du streaming pour le service " + this.streamableService.getClass().getSimpleName());
			this.currentTask.cancel(true);
			this.currentTask = null;
		}
	}

	/**
	 * Méthode d'initialisation spring.
	 * Permet de récupérer le {@link Property} porté par l'annotation {@link Streamed} de l'implémentation du {@link SSEStreamable}.
	 * Cette propriété est utilisé pour détermine la fréquence d'appel.
	 * Cette récupération permet aussi de s'abonner à la mise à jour de la {@link HomeServerPropertyHolder} correspondance, pour une reconstruction
	 * de la programmation récurrent de l'appel du {@link SSEStreamable}.
	 */
	@PostConstruct
	private void initMethod() {
		Assert.notNull(this.streamableService, "streamableService can't be null");
		try {
			// récupération de la méthode process du SSStreamable
			Method processMethod = this.streamableService.getClass().getMethod("process");
			// récupération de l'annotation permettant de paramétré la fréquence et l'émission des events.
			// elle doit obligatoirement être renseignée.
			Assert.isTrue(processMethod.isAnnotationPresent(Streamed.class), "Streamed doit être présente sur la méthode process");
			streamedAnnotation = processMethod.getAnnotation(Streamed.class);
			
			// récupération de la propriété dans le manager
			this.fixedDelayProperty =  propertiesManager.getPropertyHolder(streamedAnnotation.fixedDelay().value());
			
			// abonnement à la modification de la propriété.
			// permet de reprogrammer la tache récurrente lorsque le délai change
			this.fixedDelayProperty.addSetEventHandler(homeServerPropertyHolder -> {
					log.info("modification du fixed delay, reprogrammation du job");
					forceStop();
					startScheduleIfNecessary();
				});
		} catch (NoSuchMethodException | SecurityException e) {
			throw new BeanCreationException("problème pour la récupération de la méthod process sur streamableService", e);
		}
	}
}
