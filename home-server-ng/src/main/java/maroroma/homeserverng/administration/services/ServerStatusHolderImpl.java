package maroroma.homeserverng.administration.services;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.administration.model.HomeServerRunningStatus;

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
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerRunningStatus getStatus() {
		return this.status;
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
	}

}
