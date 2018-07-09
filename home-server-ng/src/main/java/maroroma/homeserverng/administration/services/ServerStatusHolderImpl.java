package maroroma.homeserverng.administration.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.administration.model.HomeServerRunningStatus;
import maroroma.homeserverng.administration.model.HomeServerStatus;
import maroroma.homeserverng.network.services.NetworkServiceImpl;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.DriveUtils;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Implémentation du holder du status du server.
 * @author rlevexie
 *
 */
@Service
@Log4j2
public class ServerStatusHolderImpl implements ServerStatusHolder {

	/**
	 * Status courant du server.
	 */
	private HomeServerRunningStatus status = HomeServerRunningStatus.STARTING;
	
	/**
	 * Heure de démarrage du serveur.
	 */
	private LocalDateTime startupTime;
	
	/**
	 * Version courante de l'application.
	 */
	@Value("${homeserver.version}")
	private String homeServerVersion;
	
	/**
	 * Liste des montages unix.
	 */
	@Property("homeserver.administrations.drives")
	private HomeServerPropertyHolder unixDrives;
	
	/**
	 * {@link Notifyer} général.
	 */
	@Autowired
	private NotifyerContainer notificationContainer;

	/**
	 * Pour les informations réseau.
	 */
	@Autowired
	private NetworkServiceImpl networkService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerStatus getStatus() {

		return HomeServerStatus.builder()
				.startUpTime(this.startupTime)
				.ipAddress(this.networkService.getMyIPAddress())
				.hostName(this.networkService.getHostName())
				.operatingSystem(System.getProperty("os.name"))
				.drives(DriveUtils.getDrives(this.unixDrives.asFileList()))
				.version(this.homeServerVersion).build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStatus(final HomeServerRunningStatus newstatus) {
		log.info("mise à jour du status du server : ["
				+ this.status
				+ "] vers ["
				+ newstatus + "]"
				);
		this.status = newstatus;

	}
	
	/**
	 * Permet de détecter une init du contexte spring et de passer
	 * le status du server à {@link HomeServerRunningStatus}.RUNNING.
	 */
	@PostConstruct
	protected void postInit() {
		log.info("init du contexte spring");
		this.setStatus(HomeServerRunningStatus.RUNNING);
		this.startupTime = LocalDateTime.now();
		
		// émission de notification
		try {
			this.notificationContainer.notify(NotificationEvent.builder()
					.creationDate(new Date())
					.title("Démarrage du serveur")
					.message("Le serveur homeserver vient de démarrer")
					.build());
		} catch (HomeServerException e) {
			log.warn("L'émission des notifications de démarrage a échoué");
		}
	}
}
