package maroroma.homeserverng.tools.streaming;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Classe utilitaire http pour le streaming.
 * @author rlevexie
 *
 */
public abstract class StreamingHttpUtils {
	
	/**
	 * Disposition par défaut.
	 */
	private static final String DEFAULT_DISPOSITION = "inline";

	/**
	 * Date d'expiration par défaut.
	 */
    public static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
    
    /**
	 * Utilisé pour le temps d'expiration.
	 */
	private static final int ONE_SECOND_FROM_MILLIS = 1000;
	
	/**
     * Returns true if the given accept header accepts the given value.
     * @param acceptHeader The accept header.
     * @param toAccept The value to be accepted.
     * @return True if the given accept header accepts the given value.
     */
    public static boolean accepts(final String acceptHeader, final String toAccept) {
        String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
        Arrays.sort(acceptValues);

        return Arrays.binarySearch(acceptValues, toAccept) > -1
                || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
                || Arrays.binarySearch(acceptValues, "*/*") > -1;
    }

    /**
     * Returns true if the given match header matches the given value.
     * @param matchHeader The match header.
     * @param toMatch The value to be matched.
     * @return True if the given match header matches the given value.
     */
    public static boolean matches(final String matchHeader, final String toMatch) {
        String[] matchValues = matchHeader.split("\\s*,\\s*");
        Arrays.sort(matchValues);
        return Arrays.binarySearch(matchValues, toMatch) > -1
                || Arrays.binarySearch(matchValues, "*") > -1;
    }
    
    /**
     * Permet de réaliser une passe d'initialisation sur les headers de la réponse.
     * @param toStream -
     * @param contentType -
     * @param disposition -
     * @param response -
     */
    public static void initializeResponse(final FileToStream toStream, final String contentType, final String disposition, 
    		final HttpServletResponse response) {
		response.reset();
        response.setBufferSize(StreamingRange.DEFAULT_BUFFER_SIZE);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition + ";filename=\"" + toStream.getFileName() + "\"");
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        response.setHeader(HttpHeaders.ETAG, toStream.getFileName());
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, toStream.getLastModified());
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
	}
    
    /**
     * Permet de valider les headers http en cas de reprise du chargement.
     * @param request -
     * @param fileToStream -
     * @throws StreamingFileSenderException -
     */
    public static void validateHeadersForResume(final HttpServletRequest request,
    		final FileToStream fileToStream) throws StreamingFileSenderException {
		// If-Match header should contain "*" or ETag. If not, then return 412.
        String ifMatch = request.getHeader(HttpHeaders.IF_MATCH);
        if (ifMatch != null && !StreamingHttpUtils.matches(ifMatch, fileToStream.getFileName())) {
            throw new StreamingFileSenderException("Erreur sur le contenu du ifmatch", HttpStatus.PRECONDITION_FAILED);
        }

        // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
        long ifUnmodifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + ONE_SECOND_FROM_MILLIS <= fileToStream.getLastModified()) {
            throw new StreamingFileSenderException("Erreur sur le contenu du If-Unmodified-Since", HttpStatus.PRECONDITION_FAILED);
        }
	}

    /**
     * Permet de valider les headers en cas de mise en cache du fichier.
     * @param request -
     * @param fileToStream -
     * @throws StreamingFileSenderException -
     */
    public static void validateHeadersForCaching(final HttpServletRequest request,
    		final FileToStream fileToStream) throws StreamingFileSenderException {
		// If-None-Match header should contain "*" or ETag. If so, then return 304.
        String ifNoneMatch = request.getHeader(HttpHeaders.IF_NONE_MATCH); 
        if (ifNoneMatch != null && StreamingHttpUtils.matches(ifNoneMatch, fileToStream.getFileName())) {
            throw new StreamingFileSenderException("Erreur sur le contenu du ETAG", HttpStatus.NOT_MODIFIED,
            		Tuple.from(HttpHeaders.ETAG, fileToStream.getFileName()));
        }

        // If-Modified-Since header should be greater than LastModified. If so, then return 304.
        // This header is ignored if any If-None-Match header is specified.
        long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + ONE_SECOND_FROM_MILLIS > fileToStream.getLastModified()) {
            throw new StreamingFileSenderException("Erreur sur le contenu du If-Modified-Since",
            		HttpStatus.NOT_MODIFIED, Tuple.from(HttpHeaders.ETAG, fileToStream.getFileName()));
        }
	}
    
    /**
     * Extrait le content du header.
     * @param filepath -
     * @return -
     */
    public static String getContent(final String filepath) {
    	// TODO : trouvé l'équivalent
    	String contentType = null; //MimeTypeUtils.probeContentType(filepath);
    	
    	if (contentType == null) {
    		contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    	}
    	
    	return contentType;
    }
    
    /**
     * Détermine la disposition en fonction du contenu demandé.
     * @param contentType -
     * @param request -
     * @return -
     */
    public static String getDisposition(final String contentType, final HttpServletRequest request) {
    	String disposition = DEFAULT_DISPOSITION;
    	if (!contentType.startsWith("image")) {
            // Else, expect for images, determine content disposition. If content type is supported by
            // the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
            String accept = request.getHeader(HttpHeaders.ACCEPT);
            disposition = accept != null && StreamingHttpUtils.accepts(accept, contentType) ? DEFAULT_DISPOSITION : "attachment";
        }
    	
    	return disposition;
    }
}
