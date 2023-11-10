package maroroma.homeserverng.filemanager.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.RenameFileDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.FileOperationResult;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.streaming.input.UploadFileStream;
import maroroma.homeserverng.tools.streaming.ouput.StreamingFileSender;
import maroroma.homeserverng.tools.streaming.ouput.StreamingFileSenderException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implémentation du service pour la gestion des fichiers.
 * @author rlevexie
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileManagerServiceImpl {

	private final FilesWithAccessManagementFactory filesWithAccessManagementFactory;

	/**
	 * Création d'un répertoir.
	 * @param creationRequest -
	 * @return -
	 * @throws HomeServerException -
	 */
	public FileDescriptor createDirectory(final DirectoryCreationRequest creationRequest) throws HomeServerException {

		// validation des entrées
		Assert.notNull(creationRequest, "creationRequest can't be null");
		Assert.notNull(creationRequest.getParentDirectory(), "creationRequest.getParentDirectory() can't be null");
		Assert.hasLength(creationRequest.getParentDirectory().getId(), "creationRequest.getParentDirectory().getId() can't be null or empty");
		Assert.hasLength(creationRequest.getDirectoryName(), "creationRequest.getDirectoryName() can't be null or empty");

		FileDescriptor target = this.filesWithAccessManagementFactory
				.directoryFromId(creationRequest.getParentDirectory().getId())
				.combinePath(creationRequest.getDirectoryName())
				.asFile();

		// création physique du répertoire
		if (!target.mkdir()) {
			throw new HomeServerException("Le répertoire " + target.getFullName() + " n'a pas pu être créé");
		}

		// retour si succès
		return target;
	}

	/**
	 * Retourne la liste des répertoires gérables directement par le filemanager.
	 * @return -
	 * @throws HomeServerException -
	 */
	public List<FileDirectoryDescriptor> getRootDirectories() throws HomeServerException {
		return this.filesWithAccessManagementFactory.getRootDirectories();
	}


	/**
	 * Retourne le détail d'un répertoire.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	public FileDirectoryDescriptor getDirectoryDetail(final String id) {
		Assert.hasLength(id, "id can't be null or empty");
		return this.filesWithAccessManagementFactory.directoryFromId(id,
				FilesFactory.DirectoryParsingOptions.PARSE_FILES,
				FilesFactory.DirectoryParsingOptions.PARSE_DIRECTORIES);
	}


	/**
	 * Supprime un fichier.
	 * @param id -
	 * @return -
	 */
	public FileOperationResult deleteFile(final String id) {
		Assert.hasLength(id, "id can't be null or empty");
		return this.filesWithAccessManagementFactory
				.fileFromId(id)
				.deleteFile();
	}

	/**
	 * Upload d'une liste de fichiers
	 * @param directoryId identifiant du répertoire dans lequel on veut faire la recopie
	 * @param request requete à traiter
	 * @return liste des fichiers copiés sur le serveur
	 * @throws HomeServerException
	 */
	public List<FileDescriptor> uploadFiles(final String directoryId, final HttpServletRequest request) throws HomeServerException {
		Assert.hasLength(directoryId, "id can't be null or empty");

		return UploadFileStream.fromRequest(request)
				.foreach(oneFile -> this.filesWithAccessManagementFactory
						.directoryFromId(directoryId)
						.combinePath(oneFile.getFileName())
						.asFile()
						.copyFrom(oneFile.getInputStream())
						.getInitialFile()
				)
				.collect(Collectors.toList());
	}


	/**
	 * Renomme un fichier.
	 * @param rfd -
	 * @return -
	 */
	public FileOperationResult renameFile(final RenameFileDescriptor rfd) {
		Assert.notNull(rfd, "rfd can't be null or empty");
		Assert.hasLength(rfd.getNewName(), "rfd.newName can't be null or emtpy");
		Assert.notNull(rfd.getOriginalFile(), "rfd.originalFile can't be null or empty");

		return this.filesWithAccessManagementFactory
				.fileFromId(rfd.getOriginalFile().getId())
				.renameFile(rfd.getNewName());
	}

	/**
	 * Permet de télécharger un fichier en écrivant directement dans le flux de retour.
	 * @param base64FileName -
	 * @param response port le flux à modifier.
	 * @throws HomeServerException -
	 */
	public void getFile(final String base64FileName, final HttpServletResponse response) {

		// récupération du fichier
		FileDescriptor toDownload = this.filesWithAccessManagementFactory.fileFromId(base64FileName);

		if (toDownload.getSize() > 0) {
			response.setHeader("Content-Length", "" + toDownload.getSize());
		}

		toDownload.copyTo(Traper.trap(response::getOutputStream));
	}

	public void getFile(Supplier<Optional<String>> fileIdSupplier, final HttpServletResponse httpServletResponse) {
		fileIdSupplier.get()
				.ifPresentOrElse(
						fileID -> this.getFile(fileID, httpServletResponse),
						() -> httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value()));
	}

	/**
	 * Retourne un file descriptor en fonction de l'id
	 * @param base64FileName-
	 * @return -
	 */
	public FileDescriptor getFileDescriptor(final String base64FileName) {
		return this.filesWithAccessManagementFactory.fileFromId(base64FileName);
	}

	/**
	 * Permet de gérer le streaming d'un fichier multimédia (mp3 ou video).
	 * @param base64FileName -
	 * @param request -
	 * @param response -
	 * @throws HomeServerException -
	 */
	public void streamFile(final String base64FileName, final HttpServletRequest request,
			final HttpServletResponse response) throws HomeServerException {

		// décodage pour récupération du fichier
		File toStream = FileAndDirectoryHLP.decodeFile(base64FileName);

		log.debug("Récupération en local du fichier {}", toStream.getAbsolutePath());
			try {
				StreamingFileSender.fromPath(toStream.toPath())
				.with(request)
				.with(response)
				.serveResource();
			} catch (IOException e) {
				throw new StreamingFileSenderException("vautré", e);
			}

	}

}
