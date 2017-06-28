package maroroma.homeserverng.scanner.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import maroroma.homeserverng.scanner.model.ScannerColorMode;

/**
 * Implémentation du {@link ScannerRepository}.
 * @author rlevexie
 *
 */
@Repository
public class ScannerRepositoryImpl implements ScannerRepository {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ScannerColorMode> getColorModes() {
		List<ScannerColorMode> returnValue = new ArrayList<>();
		
		returnValue.add(new ScannerColorMode("Gray", "Nuances de gris"));
		returnValue.add(new ScannerColorMode("Color", "Couleurs"));
		
		return returnValue;
	}

}
