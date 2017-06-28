package maroroma.homeserverng.photo.tools;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Classe utilitaire pour la récupération de dates et leur conversion en {@link LocalDate}.
 * @author rlevexie
 *
 */
public class DateHandler {
	
	/**
	 * Date à convertir.
	 */
	private Date dateToConvert;

	/**
	 * Mise à jour de la {@link Date}.
	 * @param updated -
	 */
	public void updateDateToConvert(final Date updated) {
		this.dateToConvert = updated;
	}
	
	/**
	 * Conversion en {@link LocalDate}.
	 * @return -
	 */
	public LocalDate getLocalDate() {
		Instant instant = this.dateToConvert.toInstant();
		ZoneId z = ZoneId.of("Europe/Paris");
		ZonedDateTime zdt = instant.atZone(z);
		return zdt.toLocalDate();
	}
}
