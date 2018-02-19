package maroroma.homeserverng.tools.streaming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.Data;
import maroroma.homeserverng.tools.helpers.CustomCollectors;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Classe utilitaire pour déterminer les fragmens de fichier à renvoyer ou à écrire dans le flux.
 * @author rlevexie
 *
 */
/**
 * @author rlevexie
 *
 */
@Data
public class StreamingRange {

	/**
	 * Taille du buffer par défaut.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 20480; // ..bytes = 20KB.


	/**
	 * Démarrage du fragment.
	 */
	private long startIndex;
	/**
	 * Fin du fragment.
	 */
	private long endIndex;
	
	/**
	 * Taille du fragment.
	 */
	private long length;
	
	/**
	 * Taille totale du fichier.
	 */
	private long fileSize;
	
	/**
	 * Le fragment correspond-il au fichier complet ?
	 */
	private boolean isFullRange;

	/**
	 * Construct a byte range.
	 * @param start Start of the byte range.
	 * @param end End of the byte range.
	 * @param total Total length of the byte source.
	 */
	public StreamingRange(final long start, final long end, final long total) {
		this.startIndex = start;
		this.endIndex = end;
		this.length = end - start + 1;
		this.fileSize = total;
	}

	/**
	 * convertit en long à partir du sous contenu d'une entete.
	 * @param value -
	 * @param beginIndex -
	 * @param endIndex - 
	 * @return -
	 */
	public static long sublong(final String value, final int beginIndex, final int endIndex) {
		String substring = value.substring(beginIndex, endIndex);
		return (substring.length() > 0) ? Long.parseLong(substring) : -1;
	}



	/**
	 * Permet de copier depuis un inputstream vers un outputstream pour le fragment demandé.
	 * @param input -
	 * @param output -
	 * @param inputSize -
	 * @param start -
	 * @param length -
	 * @throws IOException -
	 */
	public static void copy(final InputStream input, final OutputStream output, final long inputSize,
			final long start, final long length) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int read;

		if (inputSize == length) {
			// Write full range.
			while ((read = input.read(buffer)) > 0) {
				output.write(buffer, 0, read);
				output.flush();
			}
		} else {
			input.skip(start);
			long toRead = length;

			while ((read = input.read(buffer)) > 0) {
				if ((toRead -= read) > 0) {
					output.write(buffer, 0, read);
					output.flush();
				} else {
					output.write(buffer, 0, (int) toRead + read);
					output.flush();
					break;
				}
			}
		}
	}

	
	/**
	 * Recopie le contenu de l'inputstream vers l'outputstream en fonction des propriétés de ce range.
	 * @param input -
	 * @param output -
	 * @throws IOException -
	 */
	public void copy(final InputStream input, final OutputStream output) throws IOException {
		StreamingRange.copy(input, output, this.getFileSize(), this.getStartIndex(), this.getEndIndex());
	}

	/**
	 * Génère un range couvrant la taille complète d'un fichier.
	 * @param length -
	 * @return -
	 */
	public static StreamingRange fullRange(final long length) {
		StreamingRange returnValue = new StreamingRange(0, length - 1, length);
		returnValue.setFullRange(true);
		return returnValue;
	}

	/**
	 * Génère une chaine pour les entetes http pour décrire la taille du fragment.
	 * @return -
	 */
	public String toBytesStringFormat() {
		return String.format("bytes %s-%s/%s", this.startIndex, this.endIndex, this.fileSize);
		//    	return "bytes " + this.start + "-" + this.end + "/" + this.total;
	}

	
	/**
	 * Construit un Range depuis un header au format string.
	 * @param part -
	 * @param length -
	 * @return -
	 */
	public static StreamingRange fromHeaderPart(final String part, final Long length) {
		long start = StreamingRange.sublong(part, 0, part.indexOf("-"));
		long end = StreamingRange.sublong(part, part.indexOf("-") + 1, part.length());

		if (start == -1) {
			start = length - end;
			end = length - 1;
		} else if (end == -1 || end > length - 1) {
			end = length - 1;
		}

		return new StreamingRange(start, end, length);
	}

	/**
	 * Détermine si les dimensions de la part sont invalides.
	 * @return -
	 */
	public boolean isInvalid() {
		return startIndex > endIndex;
	}

	/**
	 * Construit une liste de {@link StreamingRange} à partir du contenu du header http.
	 * @param toStream -
	 * @param request -
	 * @return -
	 * @throws StreamingFileSenderException -
	 */
	public static List<StreamingRange> processRanges(final FileToStream toStream, 
			final HttpServletRequest request) throws StreamingFileSenderException {

		List<StreamingRange> returnValue = new ArrayList<>();

		String range = request.getHeader(HttpHeaders.RANGE);
		if (range != null) {

			// Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				throw new StreamingFileSenderException("Range demandé invalide", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,
						Tuple.from(HttpHeaders.CONTENT_RANGE, "bytes */" + toStream.getLength()));
			}

			String ifRange = request.getHeader(HttpHeaders.IF_RANGE);
			if (ifRange != null && !ifRange.equals(toStream.getFileName())) {
				try {
					long ifRangeTime = request.getDateHeader(HttpHeaders.IF_RANGE); // Throws IAE if invalid.
					if (ifRangeTime != -1) {
						returnValue.add(StreamingRange.fullRange(toStream.getLength()));
					}
				} catch (IllegalArgumentException ignore) {
					returnValue.add(StreamingRange.fullRange(toStream.getLength()));
				}
			}

			// If any valid If-Range header, then process each part of byte range.
			if (returnValue.isEmpty()) {

				Tuple<List<StreamingRange>, List<StreamingRange>> ranges = Arrays
						.stream(range.substring(6).split(","))
						.map(onePart -> StreamingRange.fromHeaderPart(onePart, toStream.getLength()))
						.collect(CustomCollectors.toSplittedList(oneRange -> oneRange.isInvalid()));

				if (ranges.getItem1().size() > 0) {
					throw new StreamingFileSenderException("VAleur Range demandé invalide", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,
							Tuple.from(HttpHeaders.CONTENT_RANGE, "bytes */" + toStream.getLength()));
				} 

				returnValue.addAll(ranges.getItem2());
			}
		}
		
		// si aucun range détecté, on en rajout un par défaut.
		if (returnValue.isEmpty()) {
			returnValue.add((StreamingRange.fullRange(toStream.getLength())));
		}

		return returnValue;
	}


}
