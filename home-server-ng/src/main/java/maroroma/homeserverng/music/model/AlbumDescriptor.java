package maroroma.homeserverng.music.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;

/**
 * Permet de décrire un répertoire correspondant à un album.
 * @author rlevexie
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDescriptor {
	
	/**
	 * Identifiant de l'album. Correspond à l'identifiant base 64 du répertoire correspondant à l'album.
	 */
	private String id;
	
	/**
	 * Nom de l'album.
	 */
	private String albumName;
	/**
	 * Nom de l'artise.
	 */
	private String artistName;
	
	/**
	 * Dossier correspondant.
	 */
	private FileDirectoryDescriptor directoryDescriptor;

	
	/**
	 * Album art associé.
	 */
	private FileDescriptor albumart;

	/**
	 * Folder art associé
	 */
	private FileDescriptor folderart;

	/**
	 * Temps de la dernier rafraichissement pour l'album descriptor.
	 */
	private long lastRefresh;

	/**
	 * L'album est-il complété ? Cela permet de démarrer un transfert vers un répertoire cible
	 */
	private boolean completed;
	
	
}
