package maroroma.homeserverng.tools.cache;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Description d'une clef de cache.
 * @author rlevexie
 *
 */
@Data
@Builder
public class CacheKeyDescriptor {
	/**
	 * valeur constituant la clef.
	 */
	private List<String> value;
	
	/**
	 * Type de clef.
	 */
	private CacheKeyDescriptorType type;
}
