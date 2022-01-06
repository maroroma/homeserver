package maroroma.homeserverng.tools.files;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO pour la description des fichiers présents
 *  dans le répertoire de tri de la seedbox.
 * @author RLEVEXIE
 *
 */
@Data
@NoArgsConstructor
@Slf4j
public class FileDescriptor {

	/**
	 * Nom du fichier.
	 */
	private String name;
	
	/**
	 * Chemin complet du fichier.
	 */
	private String fullName;

	/**
	 * Accessibilité du fichier en tant que ressource http.
	 */
	@Deprecated
	private String httpRessource;
	
	/**
	 * Encodage du nom de fichier pour facilité les échange http.
	 */
	private String id;
	
	/**
	 * Taille estimée du fichier.
	 */
	private long size;

	/**
	 * Le {@link FileDescriptor} est-il un fichier ?
	 */
	private boolean isFile;

	/**
	 * Le {@link FileDescriptor} est-il un répertoire ?
	 */
	private boolean isDirectory;

	@JsonIgnore
	private AbstractFileDescriptorAdapter adapter;
	/**
	 * Constructeur.
	 * @param fileName -
	 * @param fullFileName -
	 */
	@Deprecated
	public FileDescriptor(final String fileName, final String fullFileName) {
		this.name = fileName;
		this.fullName = fullFileName;
		this.id = Base64Utils.encodeToString(this.fullName.getBytes());
	}
	
	/**
	 * Constructeur.
	 * @param file -
	 */
	@Deprecated
	public FileDescriptor(final File file) {
		this(file.getName(), file.getAbsolutePath());
		try {
			// pour les répertoire, ça pète. autant éviter la gestion d'exception
			if(file.isFile()) {
				this.size = Files.size(file.toPath());
			}
		} catch (IOException e) {
			log.warn("Problème rencontré lors de la récupération de la taille du fichier {}", file.getAbsolutePath());
		}
	}

	/**
	 * Constructeur de type clonage.
	 * @param source -
	 */
	protected FileDescriptor(final FileDescriptor source) {
		this.adapter = source.adapter;
		this.fullName = source.fullName;
		this.name = source.name;
		this.size = source.size;
		this.id = source.id;
		this.isFile = source.isFile;
		this.isDirectory = source.isDirectory;
	}

	public FileDescriptor(AbstractFileDescriptorAdapter adapter) {
		this.adapter = adapter;
		this.fullName = adapter.getFullName();
		this.name = adapter.getName();
		this.size = adapter.size();
		this.id = adapter.getId();
		this.isFile = adapter.isFile();
		this.isDirectory = adapter.isDirectory();
	}

	/**
	 * Produit un {@link File} à partir du nom complet du fichier.
	 * @return -
	 */
	@Deprecated
	public File createFile() {
		return new File(this.getFullName());
	}

	/**
	 * Permert de supprimer le fichier donné.
	 * @return true si la suppression est ok.
	 */
	public FileOperationResult deleteFile() {
		return FileOperationResult.builder()
				.completed(this.adapter.delete())
				.initialFile(this)
				.build();
	}

	/**
	 * Permet de renommer le fichier donné.
	 * @param newName nouveau nom
	 * @return true si le renommage est ok.
	 */
	public FileOperationResult renameFile(final String newName) {
		return FileOperationResult.builder()
				.completed(this.adapter.rename(newName))
				.initialFile(this)
				.build();
	}

	/**
	 * Déplacement de fichier
	 * @param target cible pour le déplacement
	 * @return -
	 */
	public FileOperationResult moveFile(final FileDescriptor target) {
		return FileOperationResult.builder()
				.initialFile(this)
				.completed(this.getAdapter().moveTo(target))
				.build();
	}

	/**
	 * Ne liste que les fichiers
	 * @return -
	 */
	public List<FileDescriptor> listFilesOnly() {
		return this.listFiles(FileDescriptorFilter.fileFilter());
	}

	/**
	 * Ne liste que les sous réertoires
	 * @return -
	 */
	public List<FileDescriptor> listDirectoriesOnly() {
		return this.listFiles(FileDescriptorFilter.directoryFilter());
	}

	/**
	 * Liste les fichiers de ce répertoire selon le filtre donné
	 * @param fileDescriptorFilter -
	 * @return -
	 */
	public List<FileDescriptor> listFiles(FileDescriptorFilter fileDescriptorFilter) {
		return this.adapter.listAllFileDescriptors()
				.filter(fileDescriptorFilter)
				.collect(Collectors.toList());
	}

	/**
	 * Crée le répertoire
	 * @return -
	 */
	public boolean mkdir() {
		return this.adapter.mkdir();
	}

	public boolean mkdirs() {
		return this.adapter.mkdirs();
	}

	@JsonIgnore
	public String getParent() {
		return this.adapter.getParent();
	}

	@JsonIgnore
	public OutputStream getOutputStream() {
		return this.adapter.getOutputStream();
	}

	@JsonIgnore
	public InputStream getInputStream() {
		return this.adapter.getInputStream();
	}


	public FileOperationResult copyFrom(FileDescriptor sourceFileDescriptor) {
		return copyFrom(sourceFileDescriptor.getInputStream());
	}

	/**
	 * Copie le contenu du stream en entrée dans le fichier décrit par {@link FileDescriptor}
	 * @param inputStream stream source à copier dans le fichier
	 * @return -
	 */
	public FileOperationResult copyFrom(final InputStream inputStream) {
		return FileOperationResult.builder()
				.initialFile(this)
				.completed(Traper.trapToBoolean(() ->  FileCopyUtils.copy(inputStream, this.getOutputStream())))
				.build();
	}

	/**
	 * Copie le contenu du fichier décrit par ce {@link FileDescriptor} dans le flux de sortie fourni
	 * @param outputStream -
	 * @return -
	 */
	public FileOperationResult copyTo(final OutputStream outputStream) {
		return FileOperationResult.builder()
				.initialFile(this)
				.completed(Traper.trapToBoolean(() -> FileCopyUtils.copy(this.getInputStream(), outputStream)))
				.build();
	}

	public FileOperationResult copyTo(final FileDescriptor target) {
		return copyTo(target.getOutputStream());
	}

	public boolean exists() {
		return this.adapter.exists();
	}
}
