package maroroma.homeserverng.scanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Option de couleur pour un scan.
 * @author rlevexie
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScannerColorMode {
	
	/**
	 * Identifiant de l'option.
	 */
	private String code;
	
	/**
	 * Label pour l'option.
	 */
	private String label;
	
}
