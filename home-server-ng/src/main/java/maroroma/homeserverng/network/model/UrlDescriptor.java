package maroroma.homeserverng.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Correspond à la description d'une url utile rattachée à un {@link ServerDescriptor} du réseau local.
 * @author rlevexie
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDescriptor {

	/**
	 * Url accessible.
	 */
	private String url;
	
	/**
	 * Nom.
	 */
	private String name;
	
}
