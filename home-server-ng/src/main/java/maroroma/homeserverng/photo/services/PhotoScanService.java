package maroroma.homeserverng.photo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.photo.model.DayDirectoryDescriptor;
import maroroma.homeserverng.photo.model.MonthDirectoryDescriptor;
import maroroma.homeserverng.photo.model.PhotoDescriptor;
import maroroma.homeserverng.photo.model.PhotoResolution;
import maroroma.homeserverng.photo.model.UploadedPhoto;
import maroroma.homeserverng.photo.model.YearDirectoryDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;


/**
 * SErvice permettant de lister les différents répertoires et sous répertoires de l'emplacement des photos.
 * @author RLEVEXIE
 *
 */
public interface PhotoScanService {

	/**
	 * Retourne la liste des années.
	 * @return -
	 */
	List<YearDirectoryDescriptor> getYears();
	
	/**
	 * Retourne un {@link MonthDirectoryDescriptor}, contenant tous les {@link DayDirectoryDescriptor} d'un mois donné.
	 * @param year année cible
	 * @param monthToScan mois cible
	 * @return -
	 */
	MonthDirectoryDescriptor getDaysForMonth(Integer year, Integer monthToScan);
	
	/**
	 * REtourne un {@link DayDirectoryDescriptor}, contenant tous les {@link PhotoDescriptor} d'une journée donnée.
	 * @param year année cible
	 * @param month mois cible
	 * @param day jour cible
	 * @return -
	 */
	DayDirectoryDescriptor getPhotosForDay(Integer year, Integer month, Integer day);

	/**
	 * Retourne un {@link YearDirectoryDescriptor}, contenant tous les {@link MonthDirectoryDescriptor} d'une année donnée.
	 * @param year année cible
	 * @return -
	 */
	YearDirectoryDescriptor getMonths(Integer year);

	/**
	 * Retourne un {@link PhotoDescriptor}.
	 * @param year année cible
	 * @param month mois cible
	 * @param day jour cible
	 * @param id identiant cible
	 * @return -
	 */
	PhotoDescriptor getPhoto(int year, int month, int day, String id);
	
	/**
	 * REtourne un tableau de {@link Byte} correspondant à une photo donnée, pour une résolution donnée..
	 * @param year année cible
	 * @param month mois cible
	 * @param day jour cible
	 * @param id nom de la photo
	 * @param resolution résolution attendue.
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] getPhoto(int year, int month, int day, String id, PhotoResolution resolution) throws HomeServerException;

	/**
	 * Permet d'uploader une photo sur le serveur.
	 * @param photoToUpload -
	 * @return -
	 * @throws HomeServerException -
	 */
	UploadedPhoto uploadPhoto(MultipartFile photoToUpload) throws HomeServerException;
	
}
