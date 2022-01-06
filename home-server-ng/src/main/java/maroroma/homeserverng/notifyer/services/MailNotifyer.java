package maroroma.homeserverng.notifyer.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * Implémentation de {@link Notifyer} permettant d'envoyer des mails.
 * Attention, ne fonctionne pas derrière certaines firewall.
 * @author RLEVEXIE
 *
 */
@Service
@Slf4j
public class MailNotifyer extends AbstractDisableableNotifyer implements Notifyer {

	/**
	 * Effort de centralisation de la configuration des mails.
	 */
	@Autowired
	private MailConfigHolder mailConfigHolder;

	/**
	 * liste des adresse mails pour lesquels on envoie les notifications.
	 */
	@Property("homeserver.notifyer.mail.smtp.clients")
	private HomeServerPropertyHolder notificationClients;

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected void doNotify(final NotificationEvent notification)   throws HomeServerException {
		// construction du mail via le builder
		MimeMessage mail = this.mailConfigHolder
				.createMailBuilder()
				.sendTo(this.notificationClients.asStringList())
				.notification(notification)
				// construction finale
				.build();

		// émission standard
		try {
			Transport.send(mail);
			log.info("Emission d'une notification par mail ok (" + notification.toString() + ")");
		} catch (MessagingException e) {
			log.warn("Emission d'une notification par mail KO", e);
			throw new HomeServerException("Erreur rencontrée lors de l'émission du mail", e);
		}
	}

}
