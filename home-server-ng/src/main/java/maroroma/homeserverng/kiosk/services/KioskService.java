package maroroma.homeserverng.kiosk.services;

import maroroma.homeserverng.kiosk.model.KioskDisplayOption;


/**
 * Service utilisé pour la gestion du kiosk.
 * @author rlevexie
 *
 */
public interface KioskService {

	/**
	 * Retourne les options d'affichage du kiosk.
	 * @return -
	 */
	KioskDisplayOption getOptions();
	
}
