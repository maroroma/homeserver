package maroroma.homeserverng.tools.directorymonitoring;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.util.Map;

import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FluentMap;

/**
 * Type d'event levé lors de la modification d'un répertoire.
 * @author rlevexie
 *
 */
public enum DirectoryEventType {
	/** event de création.*/
	CREATION,
	/** event de suppression. */
	DELETION,
	/** event de modification. */
	MODIFICATION;
	
	/**
	 * Association type d'event natif java avec les types d'event {@link DirectoryEventType}.
	 */
	private static Map<Kind<?>, DirectoryEventType> mapKindEventType = null;
	
	/**
	 * Init de la map.
	 */
	static {
		mapKindEventType = FluentMap.<Kind< ?>, DirectoryEventType>create()
			.add(StandardWatchEventKinds.ENTRY_CREATE, DirectoryEventType.CREATION)
			.add(StandardWatchEventKinds.ENTRY_DELETE, DirectoryEventType.DELETION)
			.add(StandardWatchEventKinds.ENTRY_MODIFY, DirectoryEventType.MODIFICATION);
	}
	
	/**
	 * PErmet de récupérer le bon type de {@link DirectoryEventType} à partir d'un {@link Kind}.
	 * @param kind -
	 * @return -
	 */
	public static DirectoryEventType fromKind(final Kind<?> kind) {
		Assert.isTrue(DirectoryEventType.mapKindEventType.containsKey(kind), "kind is not mapped");
		return DirectoryEventType.mapKindEventType.get(kind);
	}
}
