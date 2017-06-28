package maroroma.homeserverng.photo.tools;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

import org.springframework.stereotype.Component;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;

/**
 * Classe utilitaire pour la résolution des noms de fichiers en fonction des entrées REST.
 * @author rlevexie
 *
 */
@Component
public class PhotoFileHLP {
	
	/**
	 * Permet de formater un nombre avec un leading 0.
	 */
	private static final String TWO_DIGIT_FORMAT = "-%02d";
	
	/**
	 * Emplacement des photos exposées par le service.
	 */
	@Property(value = "homeserver.photo.directory")
	private HomeServerPropertyHolder mainPhotoDirectory;
	
	/**
	 * Génère le nom de fichier correspondant à une année.
	 * @param year année à résoudre.
	 * @return -
	 */
	public File generateFileForYear(final Integer year) {
		return new File(this.mainPhotoDirectory.asFile(), year.toString());
	}

	/**
	 * Génère le nom de fichier correspondant à une année et à un mois.
	 * @param year année à resoudre
	 * @param month mois à résoudre
	 * @return -
	 */
	public File generateFileForMonth(final Integer year, final Integer month) {
		return new File(this.generateFileForYear(year), year.toString() + String.format(TWO_DIGIT_FORMAT, month));
	}

	/**
	 * Génère le nom de fichier correspondant à une année, un mois et un jour.
	 * @param year année à résoudre
	 * @param month mois à résoudre
	 * @param day jour à résoudre
	 * @return -
	 */
	public File generateFileForDay(final Integer year, final Integer month, final Integer day) {
		return new File(this.generateFileForMonth(year, month),
				year.toString() 
				+ String.format(TWO_DIGIT_FORMAT, month) 
				+ String.format(TWO_DIGIT_FORMAT, day));
	}

	/**
	 * Génère un nom de fichier pour une année, un mois, un jour et une photo de nom donnée.
	 * @param year anneé à résoudre
	 * @param month mois à résoudre
	 * @param day jour à résoudre
	 * @param id nom de la photo
	 * @return -
	 */
	public File generateFileForPhoto(final int year, final  int month, final  int day, final String id) {
		return new File(this.generateFileForDay(year, month, day), id);
	}
	
	/**
	 * Génère un nom de fichier pour une année, un mois, un jour et une photo de nom donnée.
	 * @param date date à résoudre
	 * @param id nom de la photo
	 * @return -
	 */
	public File generateFileForPhoto(final LocalDate date, final String id) {
		return new File(this.generateFileForDay(date.get(ChronoField.YEAR),
				date.get(ChronoField.MONTH_OF_YEAR), date.get(ChronoField.DAY_OF_MONTH)), id);
	}

}
