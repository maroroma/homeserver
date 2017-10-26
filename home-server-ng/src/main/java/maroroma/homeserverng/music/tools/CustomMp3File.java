package maroroma.homeserverng.music.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.model.FileDescriptor;

/**
 * Utilitaire pour la gestion d'un {@link Mp3File}.
 * <br /> apporte des fonctionnalité de type fluent et faciliter l'écriture du fichier à travers la gestion de fichier
 * temporaire.
 * @author rlevexie
 *
 */
public final class CustomMp3File {

	/**
	 * Extension pour les fichiers temporaires.
	 */
	private static final String WORKFILE_EXTENSION = ".tmp";

	/**
	 * Constante pour message d'erreur.
	 */
	private static final String READONLY_ERROR_MSG = "le fichier est en lecture seule";
	
	/**
	 * {@link Mp3File} interne pour la manipulation des tags.
	 */
	private Mp3File innerMp3file;
	
	/**
	 * Fichier original à modifier.
	 */
	private File originalFile;
	
	/**
	 * Fichier temporaire de travail.
	 */
	private File tmpFile;
	
	/**
	 * Fichier en lecture seule ?
	 */
	private boolean readOnly;

	/**
	 * Retourne un {@link CustomMp3File} pour de la lecture seule.
	 * @param file -
	 * @return -
	 * @throws HomeServerException -
	 */
	public static CustomMp3File readOnly(final File file) throws HomeServerException {
		return new CustomMp3File(file, true);
	}

	/**
	 * Retourne un {@link CustomMp3File} pour de l'édition.
	 * @param file -
	 * @return -
	 * @throws HomeServerException -
	 */
	public static CustomMp3File rw(final File file) throws HomeServerException {
		return new CustomMp3File(file, false);
	}

	/**
	 * Constructeur.
	 * @param file fichier à manipuler
	 * @param isReadOnly détermine si le fichier ne doit pas être modifié
	 * @throws HomeServerException -
	 */
	private CustomMp3File(final File file, final boolean isReadOnly) throws HomeServerException {
		this.originalFile = file;
		this.readOnly = isReadOnly;
		
		// si lecture seule
		if (this.readOnly) {
			try {
				// on créer le fichier mp3 directement sans fichier temporaire
				this.innerMp3file = new Mp3File(this.originalFile);
			} catch (UnsupportedTagException | InvalidDataException | IOException e) {
				throw new HomeServerException("Erreur lors de la lecture du fichier", e);
			}
		} else {
			// sinon, création d'un fichier temporaire, résultat du renommage du fichier original.
			// cela permet lors de l'application des modifications de récréer le fichier avec les modifications avec le nom
			// original
			this.tmpFile = new File(file.getAbsolutePath() + WORKFILE_EXTENSION);
			try {
				// renommage du fichier original en fichier temporaire
				Files.move(this.originalFile.toPath(), this.tmpFile.toPath());
				
				// création du mp3file à partir du fichier temporaire.
				this.innerMp3file = new Mp3File(this.tmpFile);
			} catch (IOException e) {
				throw new HomeServerException("Erreur lors de la création du fichier temporaire", e);
			} catch (UnsupportedTagException | InvalidDataException e) {
				throw new HomeServerException("Erreur lors de la lecture du fichier temporaire", e);
			}
		}
	}

	/**
	 * Nettoyage des tags existants.
	 * @return -
	 */
	private CustomMp3File clearExistingTags() {
		Assert.isTrue(!this.readOnly, READONLY_ERROR_MSG);
		this.innerMp3file.removeCustomTag();
		this.innerMp3file.removeId3v1Tag();
		this.innerMp3file.removeId3v2Tag();
		return this;
	}

	/**
	 * Mise à jour des tags du fichier à partir d'un {@link TrackDescriptor}.
	 * @param td -
	 * @return -
	 * @throws HomeServerException -
	 */
	public CustomMp3File updateTags(final TrackDescriptor td)  throws HomeServerException {
		Assert.notNull(td, "td can't be null");

		new TagBuilder(td)
		.update(this.innerMp3file.getId3v1Tag())
		.update(this.innerMp3file.getId3v2Tag());


		return this;
	}


	/**
	 * Récupération et réinsertion des tags de base pour un fichier.
	 * On garde les infos de base (track et trackname), on nettoye les tags existants pour les réinsérer.
	 * @param albumDescriptor -
	 * @return -
	 * @throws HomeServerException -
	 */
	public CustomMp3File prefillTags(final AlbumDescriptor albumDescriptor) throws HomeServerException {
		Assert.isTrue(!this.readOnly, READONLY_ERROR_MSG);
		
		// on récupère les informations de l'album
		TagBuilder tagBuilder = new TagBuilder(albumDescriptor);

		
		// selon les tags présents, on récupère le track et le titire
		if (this.innerMp3file.hasId3v1Tag()) {
			tagBuilder.fromTag(this.innerMp3file.getId3v1Tag());

		} else if (this.innerMp3file.hasId3v2Tag()) {
			tagBuilder.fromTag(this.innerMp3file.getId3v2Tag());
		}

		// on nettoye les tags
		this.clearExistingTags();

		// on réinsère les tags propres et net (album, artist, track title et albumart)
		this.innerMp3file.setId3v2Tag(tagBuilder.buildV2Tag());
		this.innerMp3file.setId3v1Tag(tagBuilder.buildV1Tag());
		return this;
	}

	/**
	 * Application des modifications.
	 * @return -
	 * @throws HomeServerException -
	 */
	public CustomMp3File save() throws HomeServerException {
		Assert.isTrue(!this.readOnly, READONLY_ERROR_MSG);
		try {
			// enregistrement des modifications sur le nom du fichier original
			this.innerMp3file.save(this.originalFile.getAbsolutePath());
			// suppression du fichier temporaire
			this.tmpFile.delete();
		} catch (NotSupportedException | IOException e) {
			throw new HomeServerException("erreur lors de la mise à jour des tags", e);
		}
		return this;
	}

	/**
	 * Création d'un {@link TrackDescriptor} à partir des tags portés par le fichier.
	 * @return -
	 */
	public TrackDescriptor createTrackDescriptor() {
		return TrackDescriptor.builder()
				.file(new FileDescriptor(this.originalFile))
				.newFileName(this.originalFile.getName())
				.trackName(this.innerMp3file.getId3v2Tag().getTitle())
				.trackNumber(Integer.parseInt(this.innerMp3file.getId3v2Tag().getTrack()))
				.artistName(this.innerMp3file.getId3v2Tag().getArtist())
				.albumName(this.innerMp3file.getId3v2Tag().getAlbum())
				.build();
	}

}
