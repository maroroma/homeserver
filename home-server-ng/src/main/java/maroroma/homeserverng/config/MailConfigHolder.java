package maroroma.homeserverng.config;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.mail.MailBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Permet de centraliser la configuration du compte mail utilisé au niveau du serveur
 * pour tout envoi de mail.
 * @author rlevexie
 *
 */
@Component
public class MailConfigHolder {
	/**
	 * identifiant de la connexion au serveur smtp. dans le cas d'un compte gmail, l'id est l'adresse mail du producer.
	 */
	@Property("homeserver.notifyer.mail.smtp.login")
	private HomeServerPropertyHolder smtpLogin;

	/**
	 * Mot de passe de l'utilisateur smtp.
	 */
	@Property("homeserver.notifyer.mail.smtp.password")
	private HomeServerPropertyHolder smtpPassword;

	/**
	 * Adresse du serveur smtp.
	 */
	@Property("homeserver.notifyer.mail.smtp.host")
	private HomeServerPropertyHolder smtpHost;

	/**
	 * Port du serveur smtp.
	 */
	@Property("homeserver.notifyer.mail.smtp.port")
	private HomeServerPropertyHolder smtpPort;
	
	/**
	 * Génère le {@link MailBuilder} avec comme base la configuration du serveur SMTP et du compte à utiliser.
	 * @deprecated au profit du frameworks spring
	 * @return -
	 */
	@Deprecated
	public MailBuilder createMailBuilder() {
		return new MailBuilder()
				.from(this.smtpLogin.getResolvedValue())
				.connectionBuilder()
		.authenticated(true)
		.startTLS(true)
		.host(this.smtpHost.getValue())
		.port(this.smtpPort.getValue())
		.login(this.smtpLogin.getValue())
		.password(this.smtpPassword.getValue())
		.end();
	}

	public JavaMailSender createSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(this.smtpHost.getValue());
		mailSender.setPort(this.smtpPort.asInt());

		mailSender.setUsername(this.smtpLogin.getValue());
		mailSender.setPassword(this.smtpPassword.getValue());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}
