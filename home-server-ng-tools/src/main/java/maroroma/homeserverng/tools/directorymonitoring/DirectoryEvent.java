package maroroma.homeserverng.tools.directorymonitoring;


import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.files.FileDescriptor;

/**
 * Correspond à une notification de modificcation de répertoire.
 * @author rlevexie
 *
 */
@Data
@Builder
public class DirectoryEvent {
	/**
	 * Type d'évennement.
	 */
	private DirectoryEventType eventType;
	
	/**
	 * Fichier impacté, et qui a provoqué la levée de l'event.
	 */
	private FileDescriptor file;
}
