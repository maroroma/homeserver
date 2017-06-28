package maroroma.homeserverng.tools.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FluentMap;

/**
 * Classe utilitaire pour la construction d'email.
 * @author rlevexie
 *
 */
public class MailBuilder {





	/**
	 * Encodage du corps et de l'objet du mail.
	 */
	private static final String SUBJECT_AND_TEXT_ENCODING = "UTF-8";


	/**
	 * Sous builder pour la gestion de la connexion.
	 */
	private SessionBuilder connectionBuilder = null;

	/**
	 * Adresse email de l'émetteur.
	 */
	private String senderEmailAddress;

	/**
	 * sujet du mail.
	 */
	private String emailSubject;

	/**
	 * Date d'émission du mail.
	 */
	private Date sentDate;

	/**
	 * liste de destinataires.
	 */
	private List<String> sendToList = new ArrayList<>();

	/**
	 * Contenu simple du mail.
	 */
	private String simpleContent;

	/**
	 * Liste des fichiers à transmettre.
	 * <br /> cette liste correspond aux fichiers présents en local sur le serveur, et qui seront attachés au mail.
	 */
	private List<File> attachmentFiles = new ArrayList<>();

	/**
	 * Liste de fichiers bruts à transmettre.
	 */
	private List<RawFileAttachment> rawAttachmentFiles = new ArrayList<>();


	/**
	 * Constructeur.
	 */
	public MailBuilder() {

	}

	/**
	 * Ajoute un fichier en pièce jointe du mail.
	 * @param file -
	 * @return -
	 */
	public MailBuilder addAttachment(final File file) {
		// on controle que le fichier existe bien...
		Assert.isValidFile(file);
		this.attachmentFiles.add(file);
		return this;
	}
	
	/**
	 * Ajout d'un fichier brut dans la liste des pieces jointes du mail.
	 * @param file -
	 * @return -
	 */
	public MailBuilder addAttachment(final RawFileAttachment file) {
		Assert.notNull(file);
		this.rawAttachmentFiles.add(file);
		return this;
	}

	
	/**
	 * Ajoute des fichiers en pièces jointes du mail.
	 * @param files -
	 * @return -
	 */
	public MailBuilder addAttachments(final List<File> files) {
		Assert.notNull(files);
		files.forEach(oneFile -> addAttachment(oneFile));
		return this;
	}

	/**
	 * REtourne un {@link SessionBuilder} pour la gestion de la connexion.
	 * @return -
	 */
	public SessionBuilder connectionBuilder() {
		this.connectionBuilder = new SessionBuilder(this);
		return this.connectionBuilder;
	}

	/**
	 * adresse email de l'émetteur.
	 * @param emailAddress -
	 * @return -
	 */
	public MailBuilder from(final String emailAddress) {
		Assert.hasLength(emailAddress);
		this.senderEmailAddress = emailAddress;
		return this;
	}

	/**
	 * Sujet du mail.
	 * @param subject -
	 * @return -
	 */
	public MailBuilder subject(final String subject) {
		Assert.hasLength(subject);
		this.emailSubject = subject;
		return this;
	}

	/**
	 * Date du mail.
	 * @param date -
	 * @return -
	 */
	public MailBuilder date(final Date date) {
		Assert.notNull(date);
		this.sentDate = date;
		return this;
	}

	/**
	 * Un destinataire du mail.
	 * @param address -
	 * @return -
	 */
	public MailBuilder sendTo(final String address) {
		Assert.hasLength(address);
		this.sendToList.add(address);
		return this;
	}

	/**
	 * Liste de destinataires du mail.
	 * @param addresses -
	 * @return -
	 */
	public MailBuilder sendTo(final List<String> addresses) {
		Assert.notEmpty(addresses);
		this.sendToList.addAll(addresses);
		return this;
	}

	/**
	 * Contenu simple du mail.
	 * @param content -
	 * @return -
	 */
	public MailBuilder content(final String content) {
		Assert.hasLength(content);
		this.simpleContent = content;
		return this;
	}

	/**
	 * Construction du {@link MimeMessage}.
	 * @return -
	 * @throws HomeServerException -
	 */
	public MimeMessage build() throws HomeServerException {

		// grosse validation

		Assert.notNull(this.connectionBuilder, "la session n'a pas été initialisée");
		Assert.hasLength(this.emailSubject, "no subject");
		Assert.hasLength(this.senderEmailAddress, "from n'a pas été renseigné");
		Assert.hasLength(this.simpleContent, "aucun contenu");
		Assert.notEmpty(this.sendToList, "le mail n'a pas de destinataires");
		Assert.notNull(this.sentDate, "le mail n'a pas de date de création");





		Session newSession = this.connectionBuilder.buildSession();

		MimeMessage returnValue = new MimeMessage(newSession);

		try {
			// rajout des headers
			addHeaders(returnValue);

			// infos de base
			returnValue.setFrom(new InternetAddress(this.senderEmailAddress));
			returnValue.setSubject(this.emailSubject, SUBJECT_AND_TEXT_ENCODING);
			returnValue.setSentDate(this.sentDate);

			// contenu du mail, à travers les parts
			generateContent(returnValue);

			//			returnValue.setText(this.simpleContent, SUBJECT_AND_TEXT_ENCODING);




			for (String clientAddress : this.sendToList) {
				returnValue.addRecipient(Message.RecipientType.TO, new InternetAddress(clientAddress));
			}
		}  catch (MessagingException me) {
			throw new HomeServerException("Une erreur est survenue lors de la constitution d'un mail", me);
		}

		return returnValue;
	}

	/**
	 * Génération du contenu du mail.
	 * @param returnValue -
	 * @throws MessagingException -
	 */
	private void generateContent(final MimeMessage returnValue) throws MessagingException {
		// multipart message
		Multipart contentMultipart = new MimeMultipart();

		// contenu du mail
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(this.simpleContent);

		// rajout dans le contenu principal
		contentMultipart.addBodyPart(messageBodyPart);


		// gestion des pièces jointes si présentes
		for (File file : attachmentFiles) {
			// création de la part concernant la piecce jointe
			BodyPart attachmentPart = new MimeBodyPart();

			// nom du fichier
			attachmentPart.setFileName(file.getName());

			// contenu du fichier
			attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));

			// rajout de la piece jointee dans le contenu principal
			contentMultipart.addBodyPart(attachmentPart);
		}


		for (RawFileAttachment rawFile : this.rawAttachmentFiles) {
			// rajout de la piece jointee dans le contenu principal
			contentMultipart.addBodyPart(rawFile.convertToBodyPart());
		}

		// rajout dans le mail de l'ensemble du contenu
		returnValue.setContent(contentMultipart);

	}

	/**
	 * Rajout des headers.
	 * @param returnValue -
	 * @throws MessagingException -
	 */
	private void addHeaders(final MimeMessage returnValue) throws MessagingException {
		// contenu
		returnValue.addHeader("Content-type", "text/HTML; charset=UTF-8");

		// format
		returnValue.addHeader("format", "flowed");

		// ??
		returnValue.addHeader("Content-Transfer-Encoding", "8bit");
	}

	/**
	 * Sous builder pour la connexion smtp.
	 * @author RLEVEXIE
	 *
	 */
	public final class SessionBuilder {

		/**
		 * Builder parent.
		 */
		private MailBuilder parent;

		/**
		 * -.
		 */
		private String smtpHost;
		/**
		 * -.
		 */
		private String smtpPort;

		/**
		 * -.
		 */
		private boolean smtpAuth = true;
		/**
		 * -.
		 */
		private boolean startTLSEnabled = true;
		/**
		 * -.
		 */
		private String smtpLogin;
		/**
		 * -.
		 */
		private String smtpPassword;

		/**
		 * Constructeur.
		 * @param mailBuilder -
		 */
		private SessionBuilder(final MailBuilder mailBuilder) {
			this.parent = mailBuilder;
		}

		/**
		 * Hote smtp.
		 * @param host -
		 * @return -
		 */
		public SessionBuilder host(final String host) {
			Assert.hasLength(host);
			this.smtpHost = host;
			return this;
		}

		/**
		 * Port smtp.
		 * @param port -
		 * @return -
		 */
		public SessionBuilder port(final String port) {
			Assert.hasLength(port);
			this.smtpPort = port;
			return this;
		}

		/**
		 * Utilisation de l'authentification ?
		 * @param authenticated -
		 * @return -
		 */
		public SessionBuilder authenticated(final boolean authenticated) {
			this.smtpAuth = authenticated;
			return this;
		}

		/**
		 * Login smtp.
		 * @param login -
		 * @return -
		 */
		public SessionBuilder login(final String login) {
			Assert.hasLength(login);
			this.smtpLogin = login;
			return this;
		}

		/**
		 * Password smtp.
		 * @param password -
		 * @return -
		 */
		public SessionBuilder password(final String password) {
			Assert.hasLength(password);

			this.smtpPassword = password;
			return this;
		}

		/**
		 * .
		 * @param enabled -
		 * @return -
		 */
		public SessionBuilder startTLS(final boolean enabled) {
			this.startTLSEnabled = enabled;
			return this;
		}

		/**
		 * Retour au {@link MailBuilder}.
		 * @return -
		 */
		public MailBuilder end() {
			Assert.hasLength(this.smtpHost, "aucun hote smtp n'a été renseigné");
			Assert.hasLength(this.smtpPort, "aucun port smtp n'a été renseigné");
			Assert.hasLength(this.smtpLogin, "aucun login renseigné");
			Assert.hasLength(this.smtpPassword, "aucun password renseigné");
			return this.parent;
		}


		/**
		 * Construction de la {@link Session}.
		 * @return -
		 */
		private Session buildSession() {
			return Session.getInstance(this.buildConnectionProperties(),
					this.buildAuthenticator()); 
		}

		/**
		 * Création des properties pour la création de la session.
		 * @return -
		 */
		private Properties buildConnectionProperties() {
			return FluentMap.<Object, Object>create()
					.add("mail.smtp.host", this.smtpHost)
					.add("mail.smtp.port", this.smtpPort)
					.add("mail.smtp.auth", Boolean.toString(this.smtpAuth).toLowerCase())
					.add("mail.smtp.starttls.enable", Boolean.toString(this.startTLSEnabled).toLowerCase()).buildProperties();
		}

		/**
		 * Création de l' Authenticator pour la création de la session.
		 * @return -
		 */
		private Authenticator buildAuthenticator() {
			return new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpLogin, smtpPassword);
				}
			};
		}
	}

}
