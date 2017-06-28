package maroroma.homeserverng.administration.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;

/**
 * Implémentation du service de redémarrage du server.
 * @author rlevexie
 *
 */
@Service
@Log4j2
public class RestarterServiceImpl implements RestarterService {

	/**
	 * Contexte spring qui sera ciblé lors d'un redémarrage.
	 */
	@Autowired
	private AbstractApplicationContext applicationContext;
	
	/**
	 * {@link Notifyer} général.
	 */
	@Autowired
	private NotifyerContainer notificationContainer;

	/**
	 * Délai par défaut pour la prise en compte d'un redémarrage.
	 */
	@Value("${homeserver.administration.reboot.delay}")
	private int defaultRebootDelay;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Async
	public void scheduleStop() throws HomeServerException {
		this.scheduleStop(this.defaultRebootDelay);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Async
	public void scheduleStop(final int millisBeforeRestart) throws HomeServerException {
		
		try {
			log.info("Arrêt du server dans " + millisBeforeRestart + " millisecondes");
			this.notificationContainer.notify(
					NotificationEvent.builder()
					.creationDate(new Date())
					.title("Arrêt du serveur")
					.message("Une demande d'arrêt du serveur est intervenue").build());
			Thread.sleep(millisBeforeRestart);
			log.info("Arrêt du server");
			// arrêt du contexte spring
			applicationContext.stop();
			
			// arrêt de la JVM (YOLO)
			System.exit(0);
		} catch (InterruptedException e) {
			String msg = "Une erreur est survenue lors de la programmation de l'arrêt du systeme";
			log.warn(msg, e);
			throw new HomeServerException(msg, e);
		}
		
	}
	
}
