package maroroma.homeserverng.music.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.files.FileDescriptor;

/**
 * Descriptor des tags  d'un fichier mp3.
 * @author rlevexie
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackDescriptor {
	/**
	 * Nom de l'album.
	 */
	private String albumName;
	/**
	 * Nom de l'artiste.
	 */
	private String artistName;
	/**
	 * Nom du morceau.
	 */
	private String trackName;
	/**
	 * Numéro du morceau.
	 */
	private int trackNumber;
	/**
	 * Nouveau nom du fichier si mis à jour.
	 */
	private String newFileName;
	
	/**
	 * Descriptor de fichier correspondant.
	 */
	private FileDescriptor file;
}
