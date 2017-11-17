package maroroma.homeserverng.administration.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.administration.model.HomeServerRunningStatus;
import maroroma.homeserverng.administration.model.HomeServerStatus;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.DriveUtils;

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
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerStatus getStatus() {
		
		String hostName = "NC";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn("Impossible de déterminer le nom de l'host");
		}
		
		return HomeServerStatus.builder()
				.startUpTime(this.startupTime)
				.hostName(hostName)
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
	}
}
