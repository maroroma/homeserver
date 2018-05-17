package maroroma.homeserverng.tools.directorymonitoring;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;

/**
 * Classe utilitaire pour le monitoring des modifications au sein d'un répertoire.
 * @author rlevexie
 *
 */
@Log4j2
public class DirectoryMonitor {

	/**
	 * Répertoire à surveiller.
	 */
	@Getter
	private File directoryToWatch;

	/**
	 * Listing des events monitorés et des listeners associés.
	 */
	private Map<Kind<?>, List<DirectoryEventListener>> eventsMonitored;

	/**
	 * Service java de monitoring.
	 */
	@Getter
	private WatchService watchService;

	/**
	 * Gestion d'un verrou pour la modification de l'état du thread.
	 */
	private final Object monitoringLock = new Object();	

	/**
	 * Détermine si le monitoring est en cours.
	 */
	private boolean isRunning;

	/**
	 * Détermine si le monitoring est en cours.
	 * @return -
	 */
	@Synchronized("monitoringLock")
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Détermine si le monitoring est en cours.
	 * @param newState -
	 */
	@Synchronized("monitoringLock")
	private void setRunning(final boolean newState) {
		this.isRunning = newState;
	}


	/**
	 * Constructeur.
	 * @param directory -
	 */
	public DirectoryMonitor(final File directory) {
		Assert.isValidDirectory(directory);
		this.directoryToWatch = directory;
		this.eventsMonitored = new HashMap<>();
	}

	/**
	 * Détermine si des {@link DirectoryEventListener} sont présents pour l'event donné.
	 * @param eventType -
	 * @return -
	 */
	public boolean containsEventListeners(final Kind<?> eventType) {
		Assert.notNull(eventType, "eventType can't be null");
		return this.eventsMonitored.containsKey(eventType) && !this.eventsMonitored.get(eventType).isEmpty();
	}

	/**
	 * Retourne la liste de {@link DirectoryEventListener} associé à l'event.
	 * @param eventType -
	 * @return -
	 */
	public List<DirectoryEventListener> getEventListener(final Kind<?> eventType) {
		Assert.notNull(eventType, "eventType can't be null");
		Assert.isTrue(this.eventsMonitored.containsKey(eventType), eventType.name() + " is not used");
		return this.eventsMonitored.get(eventType);
	}

	/**
	 * Ajout d'un {@link DirectoryEventListener} au {@link Kind} associé.
	 * @param eventType -
	 * @param listener -
	 * @return -
	 */
	private DirectoryMonitor addEventListener(final Kind<?> eventType, final DirectoryEventListener listener) {
		Assert.notNull(eventType, "eventType can't be null");
		Assert.notNull(listener, "listener can't be null");

		if (!this.eventsMonitored.containsKey(eventType)) {
			this.eventsMonitored.put(eventType, new ArrayList<>());
		}

		this.eventsMonitored.get(eventType).add(listener);

		return this;
	}

	/**
	 * Rajout d'un listener pour la création de fichiers.
	 * @param listener -
	 * @return -
	 */
	public DirectoryMonitor watchCreation(final DirectoryEventListener listener) {
		Assert.notNull(listener, "listener can't be null");
		return this.addEventListener(StandardWatchEventKinds.ENTRY_CREATE, listener);
	}

	/**
	 * Rajout d'un listener pour la suppression de fichiers.
	 * @param listener -
	 * @return -
	 */
	public DirectoryMonitor watchDeletion(final DirectoryEventListener listener) {
		Assert.notNull(listener, "listener can't be null");
		return this.addEventListener(StandardWatchEventKinds.ENTRY_DELETE, listener);
	}

	/**
	 * Rajout d'un listener pour la modification de fichiers.
	 * @param listener -
	 * @return -
	 */
	public DirectoryMonitor watchModification(final DirectoryEventListener listener) {
		Assert.notNull(listener, "listener can't be null");
		return this.addEventListener(StandardWatchEventKinds.ENTRY_MODIFY, listener);
	}

	/**
	 * Démarrage du monitoring.
	 * @return -
	 * @throws HomeServerException -
	 */
	public DirectoryMonitor start() throws HomeServerException {

		Assert.notEmpty(this.eventsMonitored, "eventsMonitored can't be empty");

		Path dir = this.directoryToWatch.toPath();



		try {
			this.watchService = FileSystems.getDefault().newWatchService();
			dir.register(this.watchService,
					this.eventsMonitored.keySet().stream().toArray(Kind<?>[]::new));
		} catch (IOException e) {
			String msg = "Erreur rencontrée lors de la mise en place du monitorin du répertoire " + this.directoryToWatch.getAbsolutePath();
			log.error(msg, e);
			throw new HomeServerException(msg, e);
		}

		this.setRunning(true);

		Thread threadMonitoring = new Thread(new DirectoryMonitoringTask(this));
		threadMonitoring.start();

		return this;
	}

	/**
	 * Arrêt du monitoring.
	 * @throws HomeServerException -
	 */
	public void stop() throws HomeServerException {
		if (this.isRunning()) {
			
			// va provoquer l'arrêt du thread
			this.setRunning(false);
			try {
				// sortie propre
				this.watchService.close();
			} catch (IOException e) {
				String msg = "une erreur est survenue lors de l'arrêt du monitoring du répertoire " +  this.directoryToWatch.getAbsolutePath();
				log.error(msg, e);
				throw new HomeServerException(msg, e);
			}
		}
	}

}
