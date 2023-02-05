package maroroma.homeserverng.filemanager.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.administration.services.SecurityManagerImpl;
import maroroma.homeserverng.filemanager.model.DirectoryCreationRequest;
import maroroma.homeserverng.filemanager.model.RenameFileDescriptor;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDescriptorFactory;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.FileOperationResult;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.streaming.input.UploadFileStream;
import maroroma.homeserverng.tools.streaming.ouput.StreamingFileSender;
import maroroma.homeserverng.tools.streaming.ouput.StreamingFileSenderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour la gestion des fichiers.
 * @author rlevexie
 *
 */
@Service
@Slf4j
public class FileManagerServiceImpl {

	/**
	 * Liste des répertoires racines gérables par le filemanager.
	 */
	@Property("homeserver.filemanager.rootdirectories")
	private HomeServerPropertyHolder rootDirectoriesList;

	/**
	 * Gestion de la securité
	 */
	@Autowired
	private SecurityManagerImpl securityManager;

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

		FileDescriptor target = FileDescriptorFactory
				.fromId(creationRequest.getParentDirectory().getId())
				.withSecurityManager(this.securityManager)
				.combinePath(creationRequest.getDirectoryName())
				.fileDescriptor();

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
		return this.rootDirectoriesList
				.asFileDescriptorFactories()
				.stream()
				.map(factory -> factory.withSecurityManager(this.securityManager))
				.map(factory -> factory.fileDirectoryDescriptor(false, false))
				.collect(Collectors.toList());
	}


	/**
	 * Retourne le détail d'un répertoire.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	public FileDirectoryDescriptor getDirectoryDetail(final String id) throws HomeServerException {
		return FileDescriptorFactory
				.fromId(id)
				.withSecurityManager(this.securityManager)
				.fileDirectoryDescriptor(true, true);
	}


	/**
	 * Supprime un fichier.
	 * @param id -
	 * @return -
	 */
	public FileOperationResult deleteFile(final String id) {
		Assert.hasLength(id, "id can't be null or empty");
		return FileDescriptorFactory.fromId(id)
				.withSecurityManager(this.securityManager)
				.fileDescriptor()
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
				.foreach(oneFile -> FileDescriptorFactory
						.fromId(directoryId)
						.withSecurityManager(this.securityManager)
						.combinePath(oneFile.getFileName())
						.fileDescriptor()
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

		return FileDescriptorFactory
				.fromId(rfd.getOriginalFile().getId())
				.withSecurityManager(this.securityManager)
				.fileDescriptor()
				.renameFile(rfd.getNewName());
	}

	/**
	 * Permet de controller la position du fichier par rapport à l'arborescence autorisée.
	 * Controlle beaucoup trop simple.
	 * @param fd -
	 */
	private void validateAuthorizedPath(final File fd) {
		Assert.isTrue(this.rootDirectoriesList.asStringList().stream()
				.anyMatch(rootPath -> FileAndDirectoryHLP.isParentOf(rootPath, fd)),
				"Le fichier ne fait pas partie des répertoires gérables");
	}


	/**
	 * Permet de télécharger un fichier en écrivant directement dans le flux de retour.
	 * @param base64FileName -
	 * @param response port le flux à modifier.
	 * @throws HomeServerException -
	 */
	public void getFile(final String base64FileName, final HttpServletResponse response) {

		// récupération du fichier
		FileDescriptor toDownload = FileDescriptorFactory.fromId(base64FileName)
				.withSecurityManager(this.securityManager)
				.fileDescriptor();

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
		File file = FileAndDirectoryHLP.decodeFile(base64FileName);

		// validation de son existence
		Assert.isValidFile(file);

		// validation de son accès
		this.validateAuthorizedPath(file);

		return new FileDescriptor(file);
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
