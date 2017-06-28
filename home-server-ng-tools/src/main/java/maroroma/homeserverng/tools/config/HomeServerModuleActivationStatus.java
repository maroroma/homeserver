package maroroma.homeserverng.tools.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simple pour la gestion de l'activation des modules.
 * @author rlevexie
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeServerModuleActivationStatus {
	/**
	 * Identifiant du module.
	 */
	private String id;
	/**
	 * Etat d'activation du module.
	 */
	private boolean enabled;
}
