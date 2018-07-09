package maroroma.homeserverng.administration.model;

import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.model.Drive;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Vue pour connaitre l'état du serveur.
 * @author rlevexie
 *
 */
@Data
@Builder
public class HomeServerStatus {
	/**
	 * Version de l'application.
	 */
	private String version;
	/**
	 * Heure de démarrage du serveur.
	 */
	private LocalDateTime startUpTime;
	/**
	 * Nom de la machine.
	 */
	private String hostName;

	/**
	 * Adresse IP de la machine.
	 */
	private String ipAddress;

	/**
	 * Type d'os.
	 */
	private String operatingSystem;
	
	/**
	 * Liste des lecteurs logiques.
	 */
	private List<Drive> drives;
}
