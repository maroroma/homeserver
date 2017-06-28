package maroroma.homeserverng.notifyer.services;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;

/**
 * Implémentation de {@link Notifyer} permettant d'envoyer des mails.
 * Attention, ne fonctionne pas derrière certaines firewall.
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
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
		MimeMessage mail = this.mailConfigHolder.createMailBuilder()
//				.from(this.smtpLogin.getValue())
				.sendTo(this.notificationClients.asStringList())
				.subject(notification.getTitle())
				.content(notification.getMessage())
				.date(notification.getCreationDate())
				// construction finale
				.build();

		// émission standard
		try {
			Transport.send(mail);
			log.info("Emission d'une notification par mail ok (" + notification.toString() + ")");
		} catch (MessagingException e) {
			throw new HomeServerException("Erreur rencontrée lors de l'émission du mail", e);
		}
	}

}
