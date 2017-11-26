package maroroma.homeserverng.kiosk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Options d'affichage du mode kiosk.
 * @author rlevexie
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskDisplayOption {

	/**
	 * Doit-on afficher la météo ?
	 */
	private boolean displayWeather;
	/**
	 * Doit on afficher l'horloge ?
	 */
	private boolean displayHour;
	/**
	 * Doit on afficher les lectures courantes dans les mediacenter.
	 */
	private boolean displayCurrentReading;
	
	/**
	 * Doit on afficher les téléchargements en cours.
	 */
	private boolean displayCurrentDownload;
	
}
