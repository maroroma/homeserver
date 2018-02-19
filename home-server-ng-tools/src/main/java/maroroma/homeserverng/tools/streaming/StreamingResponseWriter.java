package maroroma.homeserverng.tools.streaming;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

/**
 * Classe utilitaire pour la recopie du fichier à streamer dans le flux de sortie de la {@link HttpServletResponse}.
 * @author rlevexie
 *
 */
@Builder
@Log4j2
public class StreamingResponseWriter {


	/**
	 * .
	 */
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	/**
	 * Liste des ranges pour la recopie.
	 */
	private List<StreamingRange> ranges;
	/**
	 * Reponse http pour récupérer le flux dans lequel on écrit.
	 */
	private HttpServletResponse response;
	/**
	 * Fichier à streamer.
	 */
	private FileToStream fileToStream;
	/**
	 * header http pour le content.
	 */
	private String contentType;
	/**
	 * Header http pour la disposition.
	 */
	private String disposition;

	/**
	 * Ecriture du fichier dans la réponse http, selon les parts en entrée.
	 * @throws StreamingFileSenderException -
	 */
	public void write() throws StreamingFileSenderException {
		
		// validation après build
		Assert.notEmpty(ranges, "ranges can't be null or empty");
		Assert.notNull(response, "response can't be null");
		Assert.notNull(fileToStream, "fileToStream can't be null");
		Assert.hasLength(contentType, "contentType can't be null or empty");
		Assert.hasLength(disposition, "disposition can't be null or empty");
		
		
		// Initialize response.
		StreamingHttpUtils.initializeResponse(this.fileToStream, this.contentType, this.disposition, this.response);

		// Send requested file (part(s)) to client ------------------------------------------------

		if (ranges.size() == 1) {
			this.writeOneFragment();
		} else {
			this.writeMultipleFragment();
		}
	}
	
	/**
	 * OPération d'écriture pour un fragment unique (fichier complet ou fragment).
	 * @throws StreamingFileSenderException -
	 */
	private void writeOneFragment() throws StreamingFileSenderException {
		
		// récupération du range (full ou partiel)
		StreamingRange range = this.ranges.get(0);
		
		// headers communs
		this.response.setContentType(this.contentType);
		response.setHeader(HttpHeaders.CONTENT_RANGE, range.toBytesStringFormat());
		response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(range.getLength()));
		
		// détermination du header partiel
		if (range.isFullRange()) {
			log.info("Return full file");
		} else {
			log.info("Return 1 part of file : from ({}) to ({})", range.getStartIndex(), range.getEndIndex());
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
		}
		
		// écriture
		this.protectedWriteOperation((input, output) -> range.copy(input, output));
	}
	
	/**
	 * Opération d'écriture pour des fragments multiples.
	 * @throws StreamingFileSenderException -
	 */
	private void writeMultipleFragment() throws StreamingFileSenderException {
		// Return multiple parts of file.
		response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
		response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.


		// opération d'écriture
		this.protectedWriteOperation((input, output) -> {
			// Copy multi part range.
			for (StreamingRange r : ranges) {
				log.info("Return multi part of file : from ({}) to ({})", r.getStartIndex(), r.getEndIndex());
				// Add multipart boundary and header fields for every range.
				output.println();
				output.println("--" + MULTIPART_BOUNDARY);
				output.println("Content-Type: " + contentType);
				output.println("Content-Range: bytes " + r.getStartIndex() + "-" + r.getEndIndex() + "/" + r.getFileSize());

				// Copy single part range of multi part range.
				StreamingRange.copy(input, output, fileToStream.getLength(), r.getStartIndex(), r.getLength());
			}

			// End with multipart boundary.
			output.println();
			output.println("--" + MULTIPART_BOUNDARY + "--");
		});
		
		

		
	}
	
	/**
	 * Méthode d'écriture permettant de protéger toute opération automatiquement. 
	 * Les opérations d'écritures sont déléguée dans le {@link StreamingWriteOperation}.
	 * @param writeOperation -
	 * @throws StreamingFileSenderException -
	 */
	private void protectedWriteOperation(final StreamingWriteOperation writeOperation) throws StreamingFileSenderException {
		
		try (InputStream input = new BufferedInputStream(Files.newInputStream(this.fileToStream.getPath()));
				ServletOutputStream output = response.getOutputStream()) {
			
			writeOperation.write(input, output);
			
		} catch (IOException e) {
			String msg = "Erreur rencontrée lors de l'écriture du fichier dans le flux de sortie";
			log.warn(msg, e);
			throw new StreamingFileSenderException(msg,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
