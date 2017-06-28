package maroroma.homeserverng.photo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.photo.PhotoModuleDescriptor;
import maroroma.homeserverng.photo.model.DayDirectoryDescriptor;
import maroroma.homeserverng.photo.model.MonthDirectoryDescriptor;
import maroroma.homeserverng.photo.model.PhotoDescriptor;
import maroroma.homeserverng.photo.model.PhotoResolution;
import maroroma.homeserverng.photo.model.UploadedPhoto;
import maroroma.homeserverng.photo.model.YearDirectoryDescriptor;
import maroroma.homeserverng.photo.services.PhotoScanService;
import maroroma.homeserverng.photo.services.ThumbService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Controller rest pour la gestion des photos.
 * @author RLEVEXIE
 *
 */
@HomeServerRestController(moduleDescriptor = PhotoModuleDescriptor.class)
public class PhotoController {

	
	/**
	 * Service sous jacent.
	 */
	@Autowired
	private PhotoScanService photoService;
	
	/**
	 * Gestion des miniatures.
	 */
	@Autowired
	private ThumbService thumbService;
	
	
	/**
	 * Retourne l'ensemble des {@link YearDirectoryDescriptor}.
	 * @return -
	 */
	@RequestMapping("/photo/years")
	public ResponseEntity<List<YearDirectoryDescriptor>> getYears() {
		return ResponseEntity.ok(photoService.getYears());
	}
	
	/**
	 * Retourne le contenu d'une année.
	 * @param year -
	 * @return -
	 */
	@RequestMapping("/photo/years/{year}")
	public ResponseEntity<YearDirectoryDescriptor> getMonths(final @PathVariable("year") int year) {
		return ResponseEntity.ok(photoService.getMonths(year));
	}
	
	/**
	 * Retourne la miniature d'une année.
	 * @param year -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/photo/years/{year}/THUMB")
	public ResponseEntity<byte[]> getYearThumb(final @PathVariable("year") int year) throws HomeServerException {
		return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(thumbService.getThumb(year));
	}
	
	/**
	 * Retourne la miniature pour un mois.
	 * @param year -
	 * @param month -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/photo/years/{year}/{month}/THUMB")
	public ResponseEntity<byte[]> getMonthThumb(final @PathVariable("year") int year,
			final @PathVariable("month") int month) throws HomeServerException {
		return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(thumbService.getThumb(year, month));
	}
	
	/**
	 * Retourne la miniature pour un mois.
	 * @param year -
	 * @param month -
	 * @param day -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("/photo/years/{year}/{month}/{day}/THUMB")
	public ResponseEntity<byte[]> getDayThumb(final @PathVariable("year") int year,
			final @PathVariable("month") int month,
			final @PathVariable("day") int day) throws HomeServerException {
		return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(thumbService.getThumb(year, month, day));
	}
	
	/**
	 * REtourne le contenu d'un mois.
	 * @param year -
	 * @param month -
	 * @return -
	 */
	@RequestMapping("/photo/years/{year}/{month}")
	public ResponseEntity<MonthDirectoryDescriptor> getDays(final @PathVariable("year") int year, final @PathVariable("month") int month) {
		return ResponseEntity.ok(photoService.getDaysForMonth(year, month));
	}
	
	/**
	 * Retourne le contenu d'un jour.
	 * @param year -
	 * @param month -
	 * @param day -
	 * @return -
	 */
	@RequestMapping("/photo/years/{year}/{month}/{day}")
	public ResponseEntity<DayDirectoryDescriptor> getPhotos(final @PathVariable("year") int year,
			final @PathVariable("month") int month, final @PathVariable("day") int day) {
		return ResponseEntity.ok(photoService.getPhotosForDay(year, month, day));
	}
	
	/**
	 * Retourne la description d'une photo.
	 * @param year -
	 * @param month -
	 * @param day -
	 * @param id -
	 * @return -
	 */
	@RequestMapping("/photo/years/{year}/{month}/{day}/{id}/")
	public ResponseEntity<PhotoDescriptor> getPhoto(final @PathVariable("year") int year,
			final @PathVariable("month") int month,
			final @PathVariable("day") int day, 
			final @PathVariable("id") String id) {
		return ResponseEntity.ok(photoService.getPhoto(year, month, day, id));
	}
	
	/**
	 * Retourne une photo dans une résolution donnée.
	 * @param year -
	 * @param month -
	 * @param day -
	 * @param id -
	 * @param resolution -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/photo/years/{year}/{month}/{day}/{id}/{resolution}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public ResponseEntity<byte[]> getPhoto(final @PathVariable("year") int year,
			final @PathVariable("month") int month,
			final @PathVariable("day") int day, 
			final @PathVariable("id") String id, 
			final @PathVariable("resolution") PhotoResolution resolution) throws HomeServerException {
		return ResponseEntity.ok(photoService.getPhoto(year, month, day, id, resolution));
	}
	
	/**
	 * Upload une photo sur le serveur.
	 * @param photoToUpload -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "/photo", method = {RequestMethod.POST})
	public ResponseEntity<UploadedPhoto> uploadPhoto(@RequestBody final MultipartFile photoToUpload) throws HomeServerException {
		return ResponseEntity.ok(this.photoService.uploadPhoto(photoToUpload));
	}
	
	
}
