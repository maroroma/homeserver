package maroroma.homeserverng.tools.directorymonitoring;

import maroroma.homeserverng.tools.files.FileDescriptorFactory;
import maroroma.homeserverng.tools.helpers.Assert;

import java.nio.file.*;

/**
 * {@link Runnable} dédié à la surveillance de répertoire.
 * @author rlevexie
 *
 */
public class DirectoryMonitoringTask implements Runnable {

	
	/**
	 * Monitor qui a lancé le thread.
	 */
	private DirectoryMonitor mainMonitor;
	
	
	/**
	 * Constructeur.
	 * @param monitor -
	 */
	public DirectoryMonitoringTask(final DirectoryMonitor monitor) {
		Assert.notNull(monitor, "monitor can't be null");
		this.mainMonitor = monitor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (this.mainMonitor.isRunning()) {
			 WatchKey key;
			    try {
			        // wait for a key to be available
			        key = this.mainMonitor.getWatchService().take();
			    } catch (InterruptedException | ClosedWatchServiceException ex) {
			        return;
			    }
			 
			    for (WatchEvent<?> event : key.pollEvents()) {
			        // get event type
			        WatchEvent.Kind<?> kind = event.kind();
			 
			        // get file name
			        @SuppressWarnings("unchecked")
			        WatchEvent<Path> ev = (WatchEvent<Path>) event;
			        Path fileName = ev.context();
			 
			        if (kind == StandardWatchEventKinds.OVERFLOW) {
			            continue;
			        } else if (this.mainMonitor.containsEventListeners(kind)) {

			        	// création de l'event spécifique
			        	DirectoryEvent eventToDispatch = DirectoryEvent.builder()
			        			.eventType(DirectoryEventType.fromKind(kind))
								.file(FileDescriptorFactory
										.fromPath(this.mainMonitor.getDirectoryToWatch().getAbsolutePath())
										.combinePath(fileName.toString())
										.fileDescriptor()
								)
								.build();
			        	
			        	// récupération des events listeners associé pour appel
			        	this.mainMonitor.getEventListener(kind).forEach(
			        			de -> de.onEvent(eventToDispatch)
			        			);
			        }			 
			    }
			 
			    // IMPORTANT: The key must be reset after processed
			    boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
		}

	}

}
