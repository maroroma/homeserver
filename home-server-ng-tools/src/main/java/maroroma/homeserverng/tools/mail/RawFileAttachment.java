package maroroma.homeserverng.tools.mail;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Permet de modéliser une pièce jointe de mail dont la forme adopte un tableau de {@link Byte}.
 * <br /> Ce micro container permet d'y associer pour la gestion des pièces jointes un nom de fichier et un type MIME.
 * @author RLEVEXIE
 *
 */
@Data
@Builder
@AllArgsConstructor
public class RawFileAttachment {
	/**
	 * Nom de fichier.
	 */
	private String fileName;
	/**
	 * Type mime.
	 */
	private String mimeType;
	/**
	 * Données brutes.
	 */
	private byte[] rawData;
	
	/**
	 * Convertit le {@link RawFileAttachment} en {@link BodyPart} exploitable comme pièce jointe d'un mail.
	 * @return -
	 * @throws MessagingException -
	 */
	public BodyPart convertToBodyPart() throws MessagingException {
		BodyPart attachmentPart = new MimeBodyPart();

		// nom du fichier
		attachmentPart.setFileName(this.getFileName());

		// contenu du fichier
		attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(this.getRawData(), this.getMimeType())));
		
		return attachmentPart;
	}
	
}
