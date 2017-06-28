package maroroma.homeserverng.scanner.repositories;

import java.util.List;

import maroroma.homeserverng.scanner.model.ScannerColorMode;

/**
 * Interface de définition du DAO pour la gestion du scanner.
 * @author rlevexie
 *
 */
public interface ScannerRepository {
	/**
	 * Retourne la liste des options couleur possibles pour le scanner.
	 * @return -
	 */
	List<ScannerColorMode> getColorModes();
}
