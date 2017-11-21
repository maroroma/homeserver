package maroroma.homeserverng.music.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.music.tools.CustomMp3File;
import maroroma.homeserverng.music.tools.LastRefreshPreProcessor;
import maroroma.homeserverng.music.tools.MusicTools;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.helpers.FileExtensionHelper;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.repositories.NanoRepository;

/**
 * Service de gestion des tags mp3 d'un album.
 * @author rlevexie
 *
 */
@Service
@Log4j2
public class MusicServiceImpl implements MusicService {

	/**
	 * Répertoire de travail principal.
	 */
	@Property("homeserver.music.directory")
	private HomeServerPropertyHolder workingDir;

	/**
	 * Repository pour la gestion des différents.
	 */
	@InjectNanoRepository(
			file = @Property("homeserver.music.albums.store"),
			persistedType = AlbumDescriptor.class,
			idField = "id",
			preProcessor = LastRefreshPreProcessor.class)
	private NanoRepository albumRepo;

	@Override
	public AlbumDescriptor prepareWorkingDirectory(final AlbumDescriptor request) throws HomeServerException {

		// controle des entrées
		Assert.notNull(request, "request can't be null");
		Assert.hasLength(request.getAlbumName(), "albumName can't be null or empty");
		Assert.hasLength(request.getArtistName(), "artistName can't be null or empty");

		
		// nettoyage auto
		this.autoCleanUpRepository();
		
		// création du répertoire de travail
		File workingDirFile = this.workingDir.asFile();
		if (!workingDirFile.exists()) {
			Assert.isTrue(workingDirFile.mkdirs(), "can't create music working dir [" + workingDirFile.getAbsolutePath() + "]");
		}

		// création du répertoire spécifique à l'artiste
		File artistDir = new File(workingDirFile, request.getArtistName());

		// création du répertoire spécifique à l'album
		File albumDir = new File(artistDir, 
				String.format(MusicTools.ALBUM_DIR_NAME_FORMAT, request.getArtistName(), request.getAlbumName()));

		Assert.isTrue(albumDir.mkdirs(), "can't create album working dir [" + albumDir.getAbsolutePath() + "]");

		FileDirectoryDescriptor fdd = FileDirectoryDescriptor.createSimple(albumDir);

		// recréation du descriptor pour insertion en repo
		// et retourn direct
		return this.albumRepo.saveAndReturn(AlbumDescriptor.builder()
				.albumName(request.getAlbumName())
				.artistName(request.getArtistName())
				.directoryDescriptor(fdd)
				.id(fdd.getBase64FullName())
				.build());

	}

	@Override
	public AlbumDescriptor addAlbumArt(final String toUpdatePath, final MultipartFile albumart) throws HomeServerException {

		// validation de l'album directory
		AlbumDescriptor albumDescriptor = this.validateAndReturnAlbumDescriptor(toUpdatePath);

		// validation du type de fichier
		Assert.hasValidExtension(albumart, FileExtensionHelper.JPEG, FileExtensionHelper.JPG, FileExtensionHelper.PNG);

		Assert.notNull(albumart, "albumart can't be null");

		// suppression si déjà présent
		MusicTools.removeExistingAlbumArt(albumDescriptor.getDirectoryDescriptor().createFile());

		// fichier final (album art)
		File albumArtFile = new File(albumDescriptor.getDirectoryDescriptor().createFile(), 
				String.format(MusicTools.ALBUM_ART_NAME_FORMAT, FilenameUtils.getExtension(albumart.getOriginalFilename())));

		// folder
		File folderArtFile = new File(albumDescriptor.getDirectoryDescriptor().createFile(), 
				String.format(MusicTools.FOLDER_NAME_FORMAT, FilenameUtils.getExtension(albumart.getOriginalFilename())));

		// création de l'album art.
		FileAndDirectoryHLP.convertByteArrayToFile(albumart, albumArtFile);

		// duplication pour le folder
		try {
			FileUtils.copyFile(albumArtFile, folderArtFile);
		} catch (IOException e) {
			throw new HomeServerException("Erreur lors de la création du fichier folder [" + folderArtFile.getAbsolutePath() + "]", e);
		}

		albumDescriptor.setAlbumart(MusicTools.extractAlbumArt(albumDescriptor));

		this.albumRepo.update(albumDescriptor);

		return this.albumRepo.find(albumDescriptor.getId());

	}

	@Override
	public byte[] getAlbumArt(final String albumPath, final String albumArtPath) throws HomeServerException {
		Assert.hasLength(albumArtPath, "albumPath can't be null or empty");
		this.validateAndReturnAlbumDescriptor(albumPath);
		
		return FileAndDirectoryHLP.convertFileToByteArray(albumArtPath);
	}

	@Override
	public TrackDescriptor addTrack(final String toUpdatePath, final MultipartFile oneTrack) throws HomeServerException {

		// récup de l'album
		AlbumDescriptor albumDescriptor = validateAndReturnAlbumDescriptor(toUpdatePath);
		Assert.notNull(oneTrack, "oneTrack can't be null");

		// validation du type de fichier
		Assert.hasValidExtension(oneTrack, FileExtensionHelper.MP3);

		// fichier final	
		File oneTrackFile = new File(albumDescriptor.getDirectoryDescriptor().createFile(), 
				oneTrack.getOriginalFilename());

		// copie dans le fichier temporaire, le fichier final sera géré par le gestionnaire de tag mp3
		FileAndDirectoryHLP.convertByteArrayToFile(oneTrack, oneTrackFile);

		return CustomMp3File.rw(oneTrackFile)
				.prefillTags(albumDescriptor)
				.save()
				.createTrackDescriptor();

	}

	@Override
	public List<TrackDescriptor> getAllTracks(final String toUpdatePath) throws HomeServerException {
		// récup du répertoire de travail.
		AlbumDescriptor albumDescriptor = validateAndReturnAlbumDescriptor(toUpdatePath);

		// on ne récupère que le fichiers mp3
		return Arrays.stream(albumDescriptor.getDirectoryDescriptor().createFile()
				.listFiles(CommonFileFilter.fileExtensionFilter(FileExtensionHelper.MP3)))
					.parallel()
					// on les transforme en TrackDescriptor
					.map(oneFile -> { 
						try {
							return CustomMp3File.readOnly(oneFile).createTrackDescriptor();
						} catch (HomeServerException e) {
							log.warn("Erreur lors de la lecture des tags");
							return TrackDescriptor.builder()
									.file(new FileDescriptor(oneFile))
									.newFileName(oneFile.getName())
									.albumName(albumDescriptor.getAlbumName())
									.artistName(albumDescriptor.getArtistName())
									.build();
						}
					})
					// on les retourns en tant que liste
					.collect(Collectors.toList());
	}

	/**
	 * Valide l'entrée en base 64 et retourne le descriptor correspondant.
	 * @param base64dir -
	 * @return -
	 * @throws HomeServerException -
	 */
	private AlbumDescriptor validateAndReturnAlbumDescriptor(final String base64dir) throws HomeServerException {
		Assert.hasLength(base64dir, "toUpdate can't be null or empty");
		return this.albumRepo.find(base64dir);
	}

	@Override
	public TrackDescriptor updateTrack(final TrackDescriptor td) throws HomeServerException {
		// récup et validation du fichier pour le morceau
		File oneTrack = td.getFile().createFile();
		Assert.isValidFile(oneTrack);
		try {

			// si le fichier est renommé, on le déplace
			if (!oneTrack.getName().equals(td.getNewFileName())) {
				File newFile = new File(oneTrack.getParentFile(), td.getNewFileName());
				Files.move(oneTrack.toPath(), newFile.toPath());
				oneTrack = newFile;
			}

			// modification des tags
			return CustomMp3File
					.rw(oneTrack)
					.updateTags(td)
					.save()
					.createTrackDescriptor();

		} catch (IOException e) {
			throw new HomeServerException("Une erreur est survenue lors de la modification du fichier", e);
		}
	}

	@Override
	public byte[] getTrack(final String trackPath) throws HomeServerException {
		return FileAndDirectoryHLP.convertFileToByteArray(trackPath);
	}

	@Override
	public byte[] downloadAllFiles(final String albumPath) throws HomeServerException {


		File albumDirectory = FileAndDirectoryHLP.decodeFile(albumPath);
		Assert.isValidDirectory(albumDirectory);

		return FileAndDirectoryHLP.convertFileToByteArray(FileAndDirectoryHLP.tarDirectory(albumDirectory));

	}

	/**
	 * Permet de nettoyer le repo en fonction des répertoires qui ont pu être supprimés en dehors du gestionnaire
	 * de musique.
	 * @throws HomeServerException 
	 */
	private void autoCleanUpRepository() throws HomeServerException {
		this.albumRepo.<AlbumDescriptor>delete(ad -> {
			return !ad.getDirectoryDescriptor().createFile().exists();
		});
	}





}
