package maroroma.homeserverng.tools.streaming;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

/**
 * Classe utilitaire pour gérer le streaming d'un fichier.
 * Permet de gérer les headers, qui par analyse permet de renvoyer de fragments du fichier.
 * @author rlevexie
 *
 */
@Log4j2
@Builder
@AllArgsConstructor
public class StreamingFileSender {
    
    /**
     * Emplacement du fichier.
     */
    private Path filepath;
    
    /**
     * Requete http.
     */
    private HttpServletRequest request;
    
    /**
     * Réponse http.
     */
    private HttpServletResponse response;
    
    /**
     * Constructeur.
     */
    private StreamingFileSender() {
    }

    /**
     * Builder, permet de setter le path du fichier à streamer.
     * @param path -
     * @return -
     */
    public static StreamingFileSender fromPath(final Path path) {
        return new StreamingFileSender().setFilepath(path);
    }
    
    /**
     * Builder - Permet de setter le fichier à streamer.
     * @param file -
     * @return -
     */
    public static StreamingFileSender fromFile(final File file) {
        return new StreamingFileSender().setFilepath(file.toPath());
    }

    /**
     * Builder - Permet de setter le fichier à streamer.
     * @param uri -
     * @return -
     */
    public static StreamingFileSender fromURIString(final String uri) {
        return new StreamingFileSender().setFilepath(Paths.get(uri));
    }

    /**
     * ..
     * @param path -
     * @return -
     */
    private StreamingFileSender setFilepath(final Path path) {
        this.filepath = path;
        return this;
    } 
    
    /**
     * Builder - requete http.
     * @param httpRequest -
     * @return -
     */
    public StreamingFileSender with(final HttpServletRequest httpRequest) {
        request = httpRequest;
        return this;
    }
    
    /**
     * Builder - permet de setter la réponse http.
     * @param httpResponse -
     * @return -
     */
    public StreamingFileSender with(final HttpServletResponse httpResponse) {
        response = httpResponse;
        return this;
    }
    
    /**
     * Traitement de la réponse.
     * @throws StreamingFileSenderException -
     * @throws IOException - 
     */
    public void serveResource() 
    		throws StreamingFileSenderException, IOException {
    	
    	// retour direct
        if (response == null || request == null) {
            return;
        }

        // création du DTO pour le fichier à streamer.
        // le constructeur réalise les opérations de controles en interne.
        FileToStream fileToStream = new FileToStream(filepath);
        

        // Validate request headers for caching ---------------------------------------------------

        StreamingHttpUtils.validateHeadersForCaching(this.request, fileToStream);

        // Validate request headers for resume ----------------------------------------------------

        StreamingHttpUtils.validateHeadersForResume(this.request, fileToStream);

        // Validate and process range -------------------------------------------------------------

        // Prepare some variables. The full Range represents the complete file.
        // Validate and process Range and If-Range headers.
        List<StreamingRange> ranges = StreamingRange.processRanges(fileToStream, this.request);

        // Prepare and initialize response --------------------------------------------------------

        // récupération du contentType
        String contentType = StreamingHttpUtils.getContent(fileToStream.getFileName());
        
        // Get content type by file name and set content disposition.
        String disposition = StreamingHttpUtils.getDisposition(contentType, request);
        log.debug("Content-Type : {}", contentType);
        
        
        // lancement de l'écriture
        StreamingResponseWriter.builder()
        	.contentType(contentType)
        	.disposition(disposition)
        	.fileToStream(fileToStream)
        	.response(response)
        	.ranges(ranges)
        	.build().write();
    }
}
