package maroroma.homeserverng.music.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;

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
	
	
}
