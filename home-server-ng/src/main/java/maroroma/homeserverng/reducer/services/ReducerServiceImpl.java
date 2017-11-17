package maroroma.homeserverng.reducer.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Transport;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.reducer.model.ReducedImageInput;
import maroroma.homeserverng.reducer.model.SendMailRequest;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.helpers.StreamHLP;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.mail.ContactDescriptor;
import maroroma.homeserverng.tools.mail.MailBuilder;
import maroroma.homeserverng.tools.mail.RawFileAttachment;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.repositories.NanoRepository;

/**
 * Implémentation du service de sauvegarde des images réduites sur le serveur.
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
public class ReducerServiceImpl implements ReducerService {

	/**
	 * Emplacement par défaut de la sauvegarde.
	 */
	@Property("homeserver.reducer.reducedimages.directory")
	private HomeServerPropertyHolder uploadDir;

	/**
	 * Taille max pour la réduction.
	 */
	@Property("homeserver.reducer.reducedimages.maxsize")
	private HomeServerPropertyHolder maxSize;

	/**
	 * Repo pour les adresses mail saisies.
	 */
	@InjectNanoRepository(file = @Property("homeserver.reducer.mails.store"),
			persistedType = ContactDescriptor.class)
	private NanoRepository mailRepository;

	/**
	 * gestion des extensions supportées.
	 */
	@Property("homeserver.reducer.reducedimages.supportedextensions")
	private HomeServerPropertyHolder extensions;

	/**
	 * Centralisation de la configuration des mails.
	 */
	@Autowired
	private MailConfigHolder mailConfigHolder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uploadReducedImage(final ReducedImageInput imageInput) throws HomeServerException {
		Assert.notNull(imageInput, "imageInput can't be null");
		Assert.hasLength(imageInput.getOriginalName(), "imageInput.originalName can't be null or empty");
		Assert.hasLength(imageInput.getData(), "imageInput.data can't be null or empty");
		Assert.isTrue(Arrays.asList(this.extensions.asStringArray())
				.stream().anyMatch(extension -> imageInput.getOriginalName().toLowerCase().endsWith(extension.toLowerCase())),
				"Le fichier ne possède pas une extension supportée (" + this.extensions.getResolvedValue());

		File uploadConcreteDir = uploadDir.asFile();

		// fichier à créer
		File finalFile = new File(uploadConcreteDir.getAbsolutePath(), imageInput.getOriginalName());

		// si pas de rep, création
		if (!uploadConcreteDir.exists()) {
			uploadConcreteDir.mkdirs();
		}

		OutputStream os = null;

		try {
			os = Files.newOutputStream(finalFile.toPath());
			os.write(
					imageInput.getDecodedImageAsByteArray());

			log.info("Image enregistrée à l'emplacement " + finalFile.getAbsolutePath());

		} catch (IOException e) {
			String msg = "Erreur rencontrée lors de la création du fichier" + finalFile.getAbsolutePath();
			log.error(msg, e);
			throw new HomeServerException(msg, e);
		} finally {
			StreamHLP.safeClose(os);
		}


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FileDescriptor> getReducedImages() throws HomeServerException {

		List<FileDescriptor> returnValue = Collections.synchronizedList(new ArrayList<>());

		File uploadFileDir = this.uploadDir.asFile();

		// on passe en mode stream, avec création de l'ensemble des files descriptors en //
		if (uploadFileDir.exists()) {
			Arrays.asList(uploadFileDir
					.listFiles(CommonFileFilter.fileExtensionFilter(this.extensions.asStringArray())))
			.parallelStream()
			.forEach(oneFile -> returnValue.add(new FileDescriptor(oneFile)));
		}

		return returnValue;

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getReducedImage(final String fileName) throws HomeServerException {

		Assert.hasLength(fileName, "fileName can't be null or empty");
		File toDownload = new File(this.uploadDir.asFile().getAbsolutePath(), fileName);
		Assert.isValidFile(toDownload);

		return FileAndDirectoryHLP.convertFileToByteArray(toDownload);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteReducedImage(final FileDescriptor base64ReducedImage) throws HomeServerException {
		Assert.notNull(base64ReducedImage);
		Assert.hasLength(base64ReducedImage.getName(), "name must be provided");

		// controle par rapport au chemin voulu
		File finalFiletoDelete = new File(this.uploadDir.asFile().getAbsolutePath(), base64ReducedImage.getName());
		Assert.isValidFile(finalFiletoDelete);

		// controle que la suppression s'est bien passée
		Assert.isTrue(finalFiletoDelete.delete(), "Le fichier " + finalFiletoDelete.getAbsolutePath() + " n'a pas pus être supprimé");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMail(final SendMailRequest mailRequest) throws HomeServerException {

		// validation des entrées
		Assert.notNull(mailRequest, "mailRequest can't be null");
		Assert.hasLength(mailRequest.getSubject(), "mailRequest.subject can't be null or empty");
		Assert.hasLength(mailRequest.getMessage(), "mailRequest.message can't be null or empty");
		Assert.notEmpty(mailRequest.getEmailList(), "mailRequest.emailFlatList can't be null or empty");
		Assert.notEmpty(mailRequest.getImagesToSend(), "mailRequest.imagesToSend can't be null or empty");

		// TODO : basculer cette méthode sur une emission ok.
		for (String oneEmail : mailRequest.getEmailList()) {
			// la violence
			this.mailRepository.update(new ContactDescriptor(oneEmail, oneEmail));
		}

		// création du message de base, on garde le builder pour gérer les attachment ensuite
		MailBuilder builder = this.mailConfigHolder
				.createMailBuilder()
				.content(mailRequest.getMessage())
				.subject(mailRequest.getSubject())
				.date(new Date())
				.sendTo(mailRequest.getEmailList());


		// pour chacune des images à envoyer, on teste si elle est locale ou sur le serveur.
		// ce n'est pas le même type de pièce jointe pour l'émission.
		mailRequest.getImagesToSend().forEach(image -> {

			// si l'image n'est pas en base 64
			if (!image.isBase64()) {
				File finalFileToSend = null;
				if (StringUtils.hasLength(image.getBase64FullName())) {
					finalFileToSend = FileAndDirectoryHLP.decodeFile(image.getBase64FullName());
				} else {
					// controle par rapport au chemin voulu
					finalFileToSend = new File(this.uploadDir.asFile().getAbsolutePath(), image.getOriginalName());
				}
				Assert.isValidFile(finalFileToSend);
				builder.addAttachment(finalFileToSend);
				// sinon on génère un fichier brut en piece jointee
			} else {
				builder.addAttachment(RawFileAttachment.builder()
						.fileName(image.getOriginalName())
						.mimeType(MimeTypeUtils.IMAGE_JPEG_VALUE)
						.rawData(image.getDecodedImageAsByteArray()).build());
			}


		});



		// émission concrete du mail.
		try {
			Transport.send(builder.build());

			// si l'émission du mail est ok, on tente une sauvegarde de la liste utilisée pour réuse dans l'ihm



		} catch (MessagingException e) {
			String msg = "erreur rencontrée lors de l'émission du mail";
			log.error(msg, e);
			throw new HomeServerException(msg, e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactDescriptor> findContacts(final String pattern) throws HomeServerException {
		// la recherche se fait sur le mail, le tout insensible à la casse
		return this.mailRepository.findAll(contact -> contact.getEmail().toLowerCase().contains(pattern.toLowerCase()));
	}

	@Override
	public void deleteReducedImage(final String fileID) throws HomeServerException {
		Assert.hasLength(fileID, "fileID can't be null or empty");
		this.deleteReducedImage(FileAndDirectoryHLP.decodeFileDescriptor(fileID));
	}


	@Override
	public void reduceImage(final MultipartFile imageToReduce) throws HomeServerException {
		Assert.notNull(imageToReduce, "imageToReduce can't be null");
		BufferedImage originalImage = null;
		BufferedImage reducedImage = null;
		try {
			originalImage = ImageIO.read(imageToReduce.getInputStream());
			reducedImage = this.resizeImage(originalImage);

			ImageIO.write(reducedImage, FilenameUtils.getExtension(imageToReduce.getOriginalFilename()), 
					new File(this.uploadDir.asFile(), imageToReduce.getOriginalFilename()));
		} catch (IOException e) {
			String message = "Erreur rencontrée lors de la réduction de l'image";
			log.error(message, e);
			throw new HomeServerException(message, e);
		}
	}

	/**
	 * Réduit une image donnée à la dimension max paramétrée.
	 * @param originalImage -
	 * @return -
	 */
	private BufferedImage resizeImage(final BufferedImage originalImage) {

		// récupération de la résolution demandée
		int reduceSize = this.maxSize.asInt();

		// permet de stocker la taille finale de l'image
		Tuple<Integer, Integer> finalDimension;

		// image originale plus large que haute
		if (originalImage.getWidth() > originalImage.getHeight()) {

			// règle de 3 pour la nouvelle dimension
			finalDimension = Tuple.from(reduceSize,
					(int) ((double) originalImage.getHeight() / originalImage.getWidth() * reduceSize));
		} else { 
			finalDimension = Tuple.from((int) ((double) originalImage.getWidth() / originalImage.getHeight() * reduceSize), 
					reduceSize);
		}

		// recréation de la nouvelle image dans les dimensions attendues.
		BufferedImage resizedImage = new BufferedImage(finalDimension.getItem1(), finalDimension.getItem2(), 
				originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, finalDimension.getItem1(), finalDimension.getItem2(), null);
		g.dispose();

		return resizedImage;
	}

}
