package maroroma.homeserverng.music.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.filemanager.services.FilesFactory;
import maroroma.homeserverng.music.model.AddTracksFromExistingSourceRequest;
import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.music.model.TrackDescriptor;
import maroroma.homeserverng.music.tools.CustomMp3File;
import maroroma.homeserverng.music.tools.LastRefreshPreProcessor;
import maroroma.homeserverng.music.tools.MusicTools;
import maroroma.homeserverng.seedbox.model.TodoFile;
import maroroma.homeserverng.seedbox.services.SeedBoxTodoServiceImpl;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDescriptorFilter;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.helpers.FileExtensionHelper;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import maroroma.homeserverng.tools.security.SecurityManager;
import maroroma.homeserverng.tools.streaming.input.UploadFileStream;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service de gestion des tags mp3 d'un album.
 * @author rlevexie
 *
 */
@Service
@Slf4j
public class MusicServiceImpl {

	/**
	 * Répertoire de travail principal.
	 */
	@Property("homeserver.music.working.directory")
	private HomeServerPropertyHolder workingDir;

	/**
	 * Répertoire où sont recopiés les musiques une fois traitées
	 */
	@Property("homeserver.music.target.directory")
	private HomeServerPropertyHolder targetDir;


	/**
	 * Repository pour la gestion des différents.
	 */
	@InjectNanoRepository(
			file = @Property("homeserver.music.albums.store"),
			persistedType = AlbumDescriptor.class,
			idField = "id",
			preProcessor = LastRefreshPreProcessor.class)
	private NanoRepository albumRepo;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private SeedBoxTodoServiceImpl seedBoxTodoService;

	@Autowired
	private FilesFactory filesFactory;

	/**
	 * Préparation du répertoire de travail pour cet album.
	 * @param request -
	 * @return -
	 * @throws HomeServerException -
	 */
	public AlbumDescriptor prepareWorkingDirectory(final AlbumDescriptor request) throws HomeServerException {

		// controle des entrées
		Assert.notNull(request, "request can't be null");
		Assert.hasLength(request.getAlbumName(), "albumName can't be null or empty");
		Assert.hasLength(request.getArtistName(), "artistName can't be null or empty");

		
		// nettoyage auto
		this.autoCleanUpRepository();
		
		// création du répertoire de travail sur toute son arborescence
		FileDirectoryDescriptor workingDirFile = filesFactory.directoryFromProperty(this.workingDir)
				.combinePath(request.getArtistName())
				.combinePath(String.format(MusicTools.ALBUM_DIR_NAME_FORMAT, request.getArtistName(), request.getAlbumName()))
				.asDirectory();

		if (!workingDirFile.exists()) {
			Assert.isTrue(workingDirFile.mkdirs(), "can't create music working dir [" + workingDirFile.getFullName() + "]");
		}

		// recréation du descriptor pour insertion en repo
		// et retour direct
		this.albumRepo.update(AlbumDescriptor.builder()
				.albumName(request.getAlbumName())
				.artistName(request.getArtistName())
				.directoryDescriptor(workingDirFile)
				.id(workingDirFile.getId())
				.build()
		);

		return this.albumRepo.findByIdMandatory(workingDirFile.getId());

	}

	/**
	 * Flague l'album comme complété
	 * @param albumId identifiant de l'album
	 * @return album mis à jour
	 * @throws HomeServerException -
	 */
	public AlbumDescriptor completeAlbumDescriptor(final String albumId) throws HomeServerException {
		// validation de l'album directory
		AlbumDescriptor albumDescriptor = this.validateAndReturnAlbumDescriptor(albumId);

		// l'album est completed
		albumDescriptor.setCompleted(true);

		// sauvegarde et retour
		this.albumRepo.update(albumDescriptor);

		return this.albumRepo.findByIdMandatory(albumId);
	}

	/**
	 * Rajout d'une jaquette dans le répertoire.
	 * @param toUpdatePath -
	 * @param request -
	 * @return -
	 * @throws HomeServerException -
	 */
	public AlbumDescriptor addAlbumArt(final String toUpdatePath, final HttpServletRequest request) throws HomeServerException {

		// validation de l'album directory
		AlbumDescriptor albumDescriptor = this.validateAndReturnAlbumDescriptor(toUpdatePath);

		Optional<FileDescriptor> oldAlbumArt = Optional.ofNullable(albumDescriptor.getAlbumart())
				.map(FileDescriptor::getId)
				.map(filesFactory::fileFromId);

		Optional<FileDescriptor> oldFolderArt = Optional.ofNullable(albumDescriptor.getFolderart())
				.map(FileDescriptor::getId)
				.map(filesFactory::fileFromId);

		UploadFileStream.fromRequest(request).first(uploadFile -> {

			Assert.hasValidExtension(uploadFile, FileExtensionHelper.JPEG, FileExtensionHelper.JPG, FileExtensionHelper.PNG);

			// suppression des anciens fichiers
			oldAlbumArt.ifPresent(FileDescriptor::deleteFile);
			oldFolderArt.ifPresent(FileDescriptor::deleteFile);

			// description du fichier albumart
			FileDescriptor albumArt = this.filesFactory.directoryFromPath(albumDescriptor.getDirectoryDescriptor().getFullName())
					.combinePath(String.format(MusicTools.ALBUM_ART_NAME_FORMAT, FilenameUtils.getExtension(uploadFile.getFileName())))
					.asFile();

			FileDescriptor folderArt = this.filesFactory.directoryFromPath(albumDescriptor.getDirectoryDescriptor().getFullName())
					.combinePath(String.format(MusicTools.FOLDER_NAME_FORMAT, FilenameUtils.getExtension(uploadFile.getFileName())))
					.asFile();

			// ecriture du nouveau albumArt
			albumArt.copyFrom(uploadFile.getInputStream()).crashIfFailed();

			// ecriture du nouveau folderArt
			folderArt.copyFrom(albumArt.getInputStream()).crashIfFailed();

			// mise à jour de l'album descripteur
			albumDescriptor.setAlbumart(albumArt);
			albumDescriptor.setFolderart(folderArt);

			return albumDescriptor;
		});

		this.albumRepo.update(albumDescriptor);

		return this.albumRepo.findByIdMandatory(albumDescriptor.getId());

	}

	/**
	 * Retourne l'album art.
	 * @param albumPath -
	 * @param response
	 * @return -
	 * @throws HomeServerException -
	 */
	public void getAlbumArt(final String albumPath, final HttpServletResponse response) throws HomeServerException {
		AlbumDescriptor albumDescriptor = this.validateAndReturnAlbumDescriptor(albumPath);

		this.filesFactory.fileFromId(albumDescriptor.getAlbumart().getId())
				.copyTo(Traper.trap(() -> response.getOutputStream()));
	}

	/**
	 * Retourne la liste des fichiers en attente de tri qui sont des mp3
	 * @return -
	 */
	public List<TodoFile> listExistingTodoFiles() {
		return this.seedBoxTodoService.getTodoList(FileExtensionHelper.MP3);
	}

	/**
	 * Permet de copier les fichiers spécifiés dans l'album en cours de préparation
	 * @param albumId identifiant de l'album
	 * @param addTracksFromExistingSourceRequest
	 * @return liste des morceaux déplacés et préparés
	 * @throws HomeServerException
	 */
	public List<TrackDescriptor> addExistingTracks(final String albumId, final AddTracksFromExistingSourceRequest addTracksFromExistingSourceRequest) throws HomeServerException {
		AlbumDescriptor albumDescriptor = validateAndReturnAlbumDescriptor(albumId);
		Assert.notNull(addTracksFromExistingSourceRequest, "request can't be null");
		Assert.isNotEmpty(addTracksFromExistingSourceRequest.getFileIdsForWorkingDirectory());

		// liste des fichiers à recopier
		List<FileDescriptor> srcFiles = addTracksFromExistingSourceRequest.getFileIdsForWorkingDirectory()
				.stream()
				.map(this.filesFactory::fileFromId)
				.collect(Collectors.toList());

		// recopie des fichiers, application des tags par défaut et création des tracks descriptors
		return srcFiles.stream()
				.map(oneSrcFile -> this.filesFactory
						.directoryFromPath(albumDescriptor.getDirectoryDescriptor().getFullName())
						.combinePath(oneSrcFile.getName())
						.asFile()
						.copyFrom(oneSrcFile)
						.crashIfFailed()
						.getInitialFile())
				.map(oneTargetFile -> Traper.trap(() -> CustomMp3File.rw(oneTargetFile)
						.prefillTags(albumDescriptor)
						.save()
						.createTrackDescriptor()))
				.collect(Collectors.toList());
	}

	/**
	 * Ajoute un fichier mp3 dans le répertoire de travail.
	 * @param toUpdatePath -
	 * @param oneTrack -
	 * @return -
	 * @throws HomeServerException -
	 */
	public TrackDescriptor addUploadedTrack(final String toUpdatePath, final HttpServletRequest oneTrack) throws HomeServerException {

		// récup de l'album
		AlbumDescriptor albumDescriptor = validateAndReturnAlbumDescriptor(toUpdatePath);
		Assert.notNull(oneTrack, "oneTrack can't be null");

		// validation du type de fichier
//		Assert.hasValidExtension(oneTrack, FileExtensionHelper.MP3);

		// recopie du fichier en local
		FileDescriptor uploadedTrack = UploadFileStream
				.fromRequest(oneTrack)
				.first(uploadedFile -> this.filesFactory.directoryFromPath(albumDescriptor.getDirectoryDescriptor().getFullName())
						.combinePath(uploadedFile.getFileName())
						.asFile()
						.copyFrom(uploadedFile.getInputStream())
						.crashIfFailed()
						.getInitialFile()
				);

		return CustomMp3File.rw(uploadedTrack)
				.prefillTags(albumDescriptor)
				.save()
				.createTrackDescriptor();

	}

	/**
	 * Retourne les tracks descriptors correspondants au mp3 correspondant l'album.
	 * @param toUpdatePath -
	 * @return -
	 * @throws HomeServerException -
	 */
	public List<TrackDescriptor> getAllTracks(final String toUpdatePath) throws HomeServerException {
		// récup du répertoire de travail.
		AlbumDescriptor albumDescriptor = validateAndReturnAlbumDescriptor(toUpdatePath);

		return this.filesFactory.directoryFromPath(albumDescriptor.getDirectoryDescriptor().getFullName())
				// que les fichiers mp3
				.listFiles(FileDescriptorFilter.withExtensions(FileExtensionHelper.MP3))
				.stream()
				.map(oneFileDescriptor -> Traper.trapOr(
						// si possible trackDescriptor à partir des tags
						() -> CustomMp3File.readOnly(oneFileDescriptor).createTrackDescriptor(),
						// sinon conversion à la dure
						() -> TrackDescriptor.builder()
								.file(oneFileDescriptor)
								.newFileName(oneFileDescriptor.getName())
								.albumName(albumDescriptor.getAlbumName())
								.artistName(albumDescriptor.getArtistName())
								.build())
				)
				.collect(Collectors.toList());
	}

	/**
	 * Valide l'entrée en base 64 et retourne le descriptor correspondant.
	 * @param albumId -
	 * @return -
	 * @throws HomeServerException -
	 */
	private AlbumDescriptor validateAndReturnAlbumDescriptor(final String albumId) throws HomeServerException {
		Assert.hasLength(albumId, "albumId can't be null or empty");
		return this.albumRepo.findByIdMandatory(albumId);
	}

	/**
	 * Mise à jour des tags mp3 de l'album.
	 * @param td -
	 * @return -
	 * @throws HomeServerException -
	 */
	public TrackDescriptor updateTrack(final TrackDescriptor td) throws HomeServerException {
		// récup et validation du fichier pour le morceau
		FileDescriptor oneTrack = this.filesFactory.fileFromId(td.getFile().getId());


		// si le fichier est renommé, on le déplace
		if (!oneTrack.getName().equals(td.getNewFileName())) {

			oneTrack.renameFile(td.getNewFileName()).crashIfFailed();

			oneTrack = this.filesFactory.directoryFromPath(oneTrack.getParent())
					.combinePath(td.getNewFileName())
					.asFile();
		}

		// modification des tags
		return CustomMp3File
				.rw(oneTrack)
				.updateTags(td)
				.save()
				.createTrackDescriptor();
	}

	/**
	 * Retourne un fichier mp3 de l'album.
	 * @param trackPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	public byte[] getTrack(final String trackPath) throws HomeServerException {
		return FileAndDirectoryHLP.convertFileToByteArray(trackPath);
	}

	/**
	 * Retourne l'album complet au format .tar .
	 * @param albumPath -
	 * @return -
	 * @throws HomeServerException -
	 */
	public byte[] downloadAllFiles(final String albumPath) throws HomeServerException {

		// TODO : à améliorer pour optimiser l'écriture directement dans le flux http.
		File albumDirectory = FileAndDirectoryHLP.decodeFile(albumPath);
		Assert.isValidDirectory(albumDirectory);

		return FileAndDirectoryHLP.convertFileToByteArray(FileAndDirectoryHLP.tarDirectory(albumDirectory));

	}

    /**
     * Retourne la liste des albums complétés
     * @return -
     * @throws HomeServerException -
     */
	public List<AlbumDescriptor> getCompletedAlbumDescriptors() throws HomeServerException {
	    return this.albumRepo.findAll(AlbumDescriptor::isCompleted);
    }

	/**
	 * Permet de nettoyer le repo en fonction des répertoires qui ont pu être supprimés en dehors du gestionnaire
	 * de musique.
	 * @throws HomeServerException 
	 */
	private void autoCleanUpRepository() throws HomeServerException {
		this.albumRepo.<AlbumDescriptor>delete(ad -> !filesFactory
				.directoryFromId(ad.getDirectoryDescriptor().getId())
				.exists()
		);
	}


	/**
	 * Recopie d'un album ok vers le repo de musique
	 * @param albumToArchiveId
	 * @return
	 * @throws HomeServerException
	 */
	public AlbumDescriptor archive(String albumToArchiveId) throws HomeServerException {
		// validation de l'album directory
		AlbumDescriptor albumDescriptor = this.validateAndReturnAlbumDescriptor(albumToArchiveId);

		// album à archiver
		FileDescriptor albumToArchive = filesFactory.fileFromId(albumDescriptor.getId());

		// répertoire cible
		FileDirectoryDescriptor targetDirectory = filesFactory.directoryFromProperty(this.targetDir)
				.combinePath(albumDescriptor.getArtistName())
				.combinePath(albumDescriptor.getDirectoryDescriptor().getName())
				.asDirectory();


		if (!targetDirectory.exists()) {
			targetDirectory.mkdirs();
		}

		// recopie des différents fichiers
		albumToArchive.listFilesOnly()
				.stream()
				.map(oneTrackToArchiveDescriptor -> Tuple.from(oneTrackToArchiveDescriptor, targetDirectory
						.combinePath(albumDescriptor.getArtistName())
						.combinePath(albumDescriptor.getDirectoryDescriptor().getName())
						.combinePath(oneTrackToArchiveDescriptor.getName())
						.asFile()))
				.forEach(copyPair -> copyPair.getItem1().copyTo(copyPair.getItem2()));

		return albumDescriptor;

	}
}
