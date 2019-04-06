package maroroma.homeserverng.tools.streaming.ouput;

import lombok.Getter;
import maroroma.homeserverng.tools.helpers.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Container utilitaire pour le fichier à streamer.
 * @author rlevexie
 *
 */
@Getter
public class FileToStream {

	/**
	 * Taille du fichier.
	 */
	private Long length;
	/**
	 * Nom du fichier.
	 */
	private String fileName;
	/**
	 * dernière date de modification du fichier. 
	 */
	private FileTime lastModifiedObj;
	
	/**
	 * Path original du fichier.
	 */
	private Path path;

	/**
	 * Constructeur.
	 * @param filePath -
	 * @throws StreamingFileSenderException -
	 */
	public FileToStream(final Path filePath) throws StreamingFileSenderException {
		if (!Files.exists(filePath)) {
			throw new StreamingFileSenderException("Fichier " + filePath + " non trouvé", HttpStatus.NOT_FOUND);
		}
		this.path = filePath;
		
		try {
			this.length = Files.size(filePath);
			this.fileName = filePath.getFileName().toString();
			this.lastModifiedObj = Files.getLastModifiedTime(filePath);
		} catch (IOException e) {
			throw new StreamingFileSenderException("Problème lors de la récupération du fichier à streamer", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		this.validate();
	}

	/**
	 * Valide le contenu du {@link FileToStream}.
	 * @throws StreamingFileSenderException -
	 */
	private void validate() throws StreamingFileSenderException {
		if (StringUtils.isEmpty(fileName) || lastModifiedObj == null) {
			throw new StreamingFileSenderException("Filename incohérent ou aucune informations sur la dernière modification",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Retourne la dernière date de modification au format long.
	 * @return -
	 */
	public Long getLastModified() {
		return LocalDateTime.ofInstant(lastModifiedObj.toInstant(),
				ZoneId.of(ZoneOffset.systemDefault().getId())).toEpochSecond(ZoneOffset.UTC);
	}

}
