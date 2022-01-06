package maroroma.homeserverng.photo.tools;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.xmp.XmpDirectory;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.FluentList;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Classe utilitaire pour extraire les métadonnées d'une photo.
 * @author rlevexie
 *
 */
@Slf4j
public abstract class PhotoMetadataHLP {

	/**
	 * .
	 */
	private static final int EXIF_IFD0_DATE_TIME = 306;


	/**
	 * Message d'erreur par défaut.
	 */
	private static final String DEFAULT_PARSE_MESSAGE = "Erreur rencontrée lors du parsing de la photo uploadée";


	/** XMP_CREATE_DATE. */
	private static final int XMP_CREATE_DATE = 514;
	/**
	 * XMP_TIME_DIGITIZED.
	 */
	private static final int XMP_TIME_DIGITIZED = 14;

	/**
	 * XMP_TIME_ORIGINAL.
	 */
	private static final int XMP_TIME_ORIGINAL = 13;

	/**
	 * Extrait des métadatas d'une photo la date de création présumée de celle-ci.
	 * @param file -
	 * @return {@link LocalDate}
	 * @throws HomeServerException -
	 */
	public static LocalDate extractCreationDate(final MultipartFile file) throws HomeServerException {
		try {
			return PhotoMetadataHLP.extractCreationDate(file.getInputStream());
		} catch (HomeServerException | IOException e) {
			log.error(DEFAULT_PARSE_MESSAGE, e);
			throw new HomeServerException(DEFAULT_PARSE_MESSAGE, e);
		}
	}

	/**
	 * Extrait des métadatas d'une photo la date de création présumée de celle-ci.
	 * @param imageInputStream -
	 * @return {@link LocalDate}.
	 * @throws HomeServerException -
	 */
	public static LocalDate extractCreationDate(final InputStream imageInputStream) throws HomeServerException {

		// TODO : réaliser la conversion
		DateHandler gc = new DateHandler();
		
		
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);

//			PhotoMetadataHLP.debugMetadata(metadata);

			//			XMP-Date/Time Original-13				2008-02-11T20:32:24.080+0100
			//			XMP-Date/Time Digitized-14				2008-02-11T20:32:24.080+0100
			//			XMP-Create Date-514						2008-02-11T20:32:24.078+0100
//			com.drew.metadata.exif.ExifIFD0Directory-Date/Time-306


			// récupération des sous répertoires de type XmpDirectory
//			metadata.getDirectoriesOfType(XmpDirectory.class)
			FluentList.<Directory>create()
			.addAllAnd(metadata.getDirectoriesOfType(ExifIFD0Directory.class))
			.addAllAnd(metadata.getDirectoriesOfType(XmpDirectory.class)).stream()
			// on ne récupère que les xmp avec des tags correspondant aux dates recherchées
			.filter(dirToTest -> 
			dirToTest.containsTag(XMP_TIME_ORIGINAL) 
			|| dirToTest.containsTag(XMP_TIME_DIGITIZED) 
			|| dirToTest.containsTag(XMP_CREATE_DATE)
			|| dirToTest.containsTag(EXIF_IFD0_DATE_TIME))
			// on ne garde que le premier, qui devrait suffir car porte au moins une des dates
			.findFirst()
			// si présent
			.ifPresent(finalDir -> {
				// on filtre les tags
				finalDir.getTags().stream()
				// dont le type nous intéresse
				.filter(tagToTest -> 
				tagToTest.getTagType() == XMP_TIME_ORIGINAL 
				|| tagToTest.getTagType() == XMP_TIME_DIGITIZED 
				|| tagToTest.getTagType() == XMP_CREATE_DATE
				|| tagToTest.getTagType() == EXIF_IFD0_DATE_TIME)
				// on en récupère la valeur entiere
				.map(tagWithDate -> tagWithDate.getTagType())
				// et seulement la première occurence
				.findFirst()
				// et on extrait la date en fonction de la valeur entière récupérée
				.ifPresent(tagType ->  {
					// TODO : réaliser la conversion
					gc.updateDateToConvert(finalDir.getDate(tagType));
					log.info("date recup : " + finalDir.getDate(tagType).toString());
				});
			});

		} catch (ImageProcessingException | IOException e) {
			log.error(DEFAULT_PARSE_MESSAGE, e);
			throw new HomeServerException(DEFAULT_PARSE_MESSAGE, e);
		}

		
		return gc.getLocalDate();
		
//		return LocalDate.of(gc.get(GregorianCalendar.YEAR),
//				gc.get(GregorianCalendar.MONTH + 1),
//				gc.get(GregorianCalendar.DAY_OF_MONTH));
	}

	/**
	 * Permet de debuger les metadat, compliquer de les retrouver sinon.
	 * @param metadata -
	 */
	protected static void debugMetadata(final Metadata metadata) {
		metadata.getDirectories().forEach(d -> d.getTags().forEach(t -> {
//			log.info("{}-{}-{}", d.getClass(), t.getTagName(), t.getTagType());
			if (t.getTagName().toLowerCase().contains("date")) {
				log.info(d.getDate(t.getTagType()).toString());
			}
		}));
	}

}
