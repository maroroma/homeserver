package maroroma.homeserverng.photo.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.photo.model.*;
import maroroma.homeserverng.photo.needers.PhotoCacheNeeder;
import maroroma.homeserverng.photo.tools.PhotoFileHLP;
import maroroma.homeserverng.photo.tools.PhotoMetadataHLP;
import maroroma.homeserverng.photo.tools.PhotoNamesDateHLP;
import maroroma.homeserverng.photo.tools.ResolutionHLP;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Implémentation de {@link PhotoScanService}.
 * @author rlevexie
 *
 */
@Service
@Slf4j
public class PhotoScanServiceImpl implements PhotoScanService {

	/**
	 * Emplacement des photos exposées par le service.
	 */
	@Property(value = "homeserver.photo.directory")
	private HomeServerPropertyHolder mainPhotoDirectory;


	/**
	 * Extensions des photos gérées par le service.
	 */
	@Property("homeserver.photo.extensions")
	private HomeServerPropertyHolder extensions;

	/**
	 * HElper pour la gestion de la résolution des photos.
	 */
	@Autowired
	private ResolutionHLP resolutionHLP;
	
	/**
	 * Permet de gérer le nettoyage des caches relatifs au photos.
	 */
	@Autowired
	private PhotoCacheEvicter cacheEvicter;

	/**
	 * Permet de gérer les miniatures, et la remontée vers les repertoires parents.
	 */
	@Autowired
	private ThumbService thumbService;

	/**
	 * Permet de construire les fichiers rapport aux ressources rest demandées.
	 */
	@Autowired
	private PhotoFileHLP photoFileHLP;


	/**
	 * {@inheritDoc}
	 */
	@Cacheable(cacheNames = PhotoCacheNeeder.ALL_YEARS_CACHE)
	@Override
	public List<YearDirectoryDescriptor> getYears() {
		return  CommonFileFilter
				.listDirectories(mainPhotoDirectory)
				.parallelStream()
				.filter(rawFile -> StringUtils.isInteger(rawFile.getName()))
				.map(rawFile -> new YearDirectoryDescriptor(rawFile))
				.sorted((y1, y2) -> y2.getYear().compareTo(y1.getYear()))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable(cacheNames = PhotoCacheNeeder.MONTH_CACHE)
	@Override
	public MonthDirectoryDescriptor getDaysForMonth(final Integer year, final Integer monthToScan) {
		File monthTargeted = photoFileHLP.generateFileForMonth(year, monthToScan);
		Assert.isValidDirectory(monthTargeted);

		return new MonthDirectoryDescriptor(monthTargeted).autoPopulate();
	}

	/**
	 * {@inheritDoc}.
	 */
	@Cacheable(cacheNames = PhotoCacheNeeder.DAY_CACHE)
	@Override
	public DayDirectoryDescriptor getPhotosForDay(final Integer year, final Integer month, final Integer day) {
		File dayTargeted = photoFileHLP.generateFileForDay(year, month, day);
		Assert.isValidDirectory(dayTargeted);

		return new DayDirectoryDescriptor(dayTargeted).acceptedExtensions(this.extensions.asStringArray()).autoPopulate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable(cacheNames = PhotoCacheNeeder.YEAR_CACHE)
	@Override
	public YearDirectoryDescriptor getMonths(final Integer year) {
		File yearTargeted = photoFileHLP.generateFileForYear(year);
		Assert.isValidDirectory(yearTargeted);

		return new YearDirectoryDescriptor(yearTargeted).autoPopulate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PhotoDescriptor getPhoto(final int year, final int month, final int day, final String id) {
		File photoTargeted = photoFileHLP.generateFileForPhoto(year, month, day, id);
		Assert.isValidFile(photoTargeted);

		return new PhotoDescriptor(photoTargeted);
	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable(cacheNames = PhotoCacheNeeder.FULL_SIZE_CACHE, keyGenerator = "photoFileNameKeyGenerator")
	@Override
	public byte[] getPhoto(final int year, final int month, final int day, final String id,
			final PhotoResolution resolution) throws HomeServerException {

		byte[] returnValue = null;

		// récupération du fichier cible
		File originalFile = this.getPhoto(year, month, day, id).createFile();

		// en fonction de la résolution demandée.
		switch (resolution) {
		case FULL:
			// pleine taille.
			returnValue = FileAndDirectoryHLP.convertFileToByteArray(originalFile);
			break;
		case MEDIUM:
			// taille intermédiaire (gallerie)
			returnValue = this.resizeImage(originalFile, resolution);
			break;
		case THUMB:
			// miniature
			returnValue = this.resizeImage(originalFile, resolution);
			
			// on met à jour le cache de miniature, ce qui permettra une alimentation automatique des miniatures
			// des répertoires parents
			this.thumbService.putThumb(returnValue, year, month, day);
			break;
		default:
			break;
		}

		return returnValue;
	}

	/**
	 * Redimensionnement de l'image pointée par le fichier dans la résolution attendue.
	 * @param originalFile -
	 * @param resolution -
	 * @return -
	 */
	private byte[] resizeImage(final File originalFile, final PhotoResolution resolution) {

		byte[] returnValue = null;

		BufferedImage originalImage = null;
		BufferedImage reducedImage = null;
		ByteArrayOutputStream baos = null;

		try {
			// image en entrée
			originalImage = ImageIO.read(originalFile);
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

			// image en sortie
			reducedImage = this.resizeImage(originalImage, type, resolution);

			// sortie dans byte array
			baos = new ByteArrayOutputStream();
			ImageIO.write(reducedImage, FilenameUtils.getExtension(originalFile.getName()), baos);

			returnValue = baos.toByteArray();

		} catch (IOException e) {
			String msg = "erreur rencontrée lors du redimentsionnement de l'image " + originalFile.getAbsolutePath();
			log.error(msg, e);
		} finally {
			StreamHLP.safeClose(baos);
		}

		return returnValue;
	}

	/**
	 * Retourne une image produite à partir de l'image en entrée pour la taille demandée.
	 * @param originalImage -
	 * @param type -
	 * @param resolution -
	 * @return -
	 */
	private BufferedImage resizeImage(final BufferedImage originalImage, final int type, final PhotoResolution resolution) {

		// récupération de la résolution demandée
		Tuple<Integer, Integer> concreteREsolution = this.resolutionHLP.getConcreteResolution(resolution);

		// permet de stocker la taille finale de l'image
		Tuple<Integer, Integer> finalDimension;

		// image originale plus large que haute
		if (originalImage.getWidth() > originalImage.getHeight()) {

			// règle de 3 pour la nouvelle dimension
			finalDimension = Tuple.from(concreteREsolution.getItem1(),
					(int) ((double) originalImage.getHeight() / originalImage.getWidth() * concreteREsolution.getItem1()));
		} else { 
			finalDimension = Tuple.from((int) ((double) originalImage.getWidth() / originalImage.getHeight() * concreteREsolution.getItem1()), 
					concreteREsolution.getItem1());
		}

		// recréation de la nouvelle image dans les dimensions attendues.
		BufferedImage resizedImage = new BufferedImage(finalDimension.getItem1(), finalDimension.getItem2(), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, finalDimension.getItem1(), finalDimension.getItem2(), null);
		g.dispose();

		return resizedImage;
	}

	@Override
	public UploadedPhoto uploadPhoto(final MultipartFile photoToUpload) throws HomeServerException {
		UploadedPhoto returnValue = new UploadedPhoto();
		returnValue.setName(photoToUpload.getOriginalFilename());

		returnValue.setEstimatedDate(PhotoMetadataHLP.extractCreationDate(photoToUpload));

		File target = this.photoFileHLP.generateFileForPhoto(returnValue.getEstimatedDate(), returnValue.getName());

		if (!target.getParentFile().exists()) {
			target.getParentFile().mkdirs();
		}
		
		this.cacheEvicter.evictDaysForMonth(PhotoNamesDateHLP.getYear(target.getParentFile()),
				PhotoNamesDateHLP.getMonth(target.getParentFile()));
		
		this.cacheEvicter.evictPhotosForDay(PhotoNamesDateHLP.getYear(target.getParentFile()),
				PhotoNamesDateHLP.getMonth(target.getParentFile()), PhotoNamesDateHLP.getDay(target.getParentFile()));
		
		this.cacheEvicter.evictMonths(PhotoNamesDateHLP.getYear(target.getParentFile()));
		
		this.cacheEvicter.evictYears();

		FileAndDirectoryHLP.convertByteArrayToFile(photoToUpload, target);

		return returnValue;
	}

}
