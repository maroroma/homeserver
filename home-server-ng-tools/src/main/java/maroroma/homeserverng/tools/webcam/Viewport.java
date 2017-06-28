package maroroma.homeserverng.tools.webcam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Correspond à une résolution à appliquer à une webcam.
 * @author rlevexie
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Viewport {

	/**
	 * Largeur.
	 */
	private double width;
	
	/**
	 * Hauteur.
	 */
	private double height;
}
