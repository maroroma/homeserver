package maroroma.homeserverng.tools.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description d'un contact.
 * Dans cette version, se limite à un idenfitiant et mail identique.
 * @author RLEVEXIE
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDescriptor {

	/**
	 * Identifiant du contact.
	 */
	private String id;
	
	/**
	 * EMail du contact.
	 */
	private String email;
}
