package maroroma.homeserverng.music.tools;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;

import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;

/**
 * Classe utilitaire pour le build chainé de tag.
 * @author rlevexie
 */
public class TagBuilder {

	/**
	 * Nom du morceau.
	 */
	private String trackName;
	
	/**
	 * Numéro du morceau.
	 */
	private String trackNumber;
	
	/**
	 * Album descriptor parent.
	 */
	private AlbumDescriptor albumDescriptor;
	
	/**
	 * Track descriptor correspondant.
	 */
	private TrackDescriptor trackDescriptor;
	
	/**
	 * Constructeur à partir d'un {@link AlbumDescriptor}.
	 * @param album -
	 */
	public TagBuilder(final AlbumDescriptor album) {
		Assert.notNull(album, "album can't be null");
		this.albumDescriptor = album;
	}
	
	/**
	 * Constructeur à partir d'un {@link TrackDescriptor}.
	 * @param td -
	 */
	public TagBuilder(final TrackDescriptor td) {
		Assert.notNull(td, "td can't be null");
		this.trackDescriptor = td;
	}
	
	/**
	 * Met à jour les informations à partir d'un tag v1.
	 * @param tag -
	 * @return -
	 */
	public TagBuilder fromTag(final ID3v1 tag) {
		this.trackName = tag.getTitle();
		this.trackNumber = tag.getTrack();
		return this;
	}
	
	/**
	 * Mise à jour d'un {@link ID3v1}.
	 * @param tag -
	 * @return -
	 */
	public TagBuilder update(final ID3v1 tag) {
		Assert.notNull(tag, "tag can't be null");
		tag.setTitle(this.trackDescriptor.getTrackName());
		tag.setTrack("" + this.trackDescriptor.getTrackNumber());
		return this;
	}
	
	/**
	 * mise à jour du nom du morceau.
	 * @param newTrackName - 
	 * @return -
	 */
	public TagBuilder trackName(final String newTrackName) {
		this.trackName = newTrackName;
		return this;
	}
	
	/**
	 * Mise à jour du numéro de morceau.
	 * @param newTrackNumber -
	 * @return -
	 */
	public TagBuilder trackNumber(final String newTrackNumber) {
		this.trackNumber = newTrackNumber;
		return this;
	}
	
	/**
	 * Construit un {@link ID3v2} à partir des informations gérées par le builder.
	 * @return -
	 * @throws HomeServerException -
	 */
	public ID3v2 buildV2Tag() throws HomeServerException {
		Assert.notNull(this.albumDescriptor, "albumDescriptor missing");
		ID3v2 returnValue = new ID3v24Tag();
		
		setBasicValues(returnValue);
		
		returnValue.setAlbumImage(FileAndDirectoryHLP.convertFileToByteArray(this.albumDescriptor.getAlbumart().createFile()), 
				MusicTools.getMimeFromAlbumArt(this.albumDescriptor));
		
		return returnValue;
	}
	
	/**
	 * Construit un {@link ID3v1} à partir des informations gérées par le builder.
	 * @return -
	 * @throws HomeServerException -
	 */
	public ID3v1 buildV1Tag() throws HomeServerException {
		Assert.notNull(this.albumDescriptor, "albumDescriptor missing");
		ID3v1 returnValue = new ID3v1Tag();
		setBasicValues(returnValue);
		return returnValue;
	}

	/**
	 * Positionnement des valeurs communes sur un tag mp3.
	 * @param returnValue -
	 */
	private void setBasicValues(final ID3v1 returnValue) {
		Assert.notNull(this.albumDescriptor, "albumDescriptor missing");
		returnValue.setAlbum(this.albumDescriptor.getAlbumName());
		returnValue.setArtist(this.albumDescriptor.getArtistName());
		returnValue.setTrack(this.trackNumber);
		returnValue.setTitle(this.trackName);
	}
	
}
