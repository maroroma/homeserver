package maroroma.homeserverng.tools.streaming.input;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Classe utilitaire pour faciliter la manipulation des fichiers en cours d'upload
 */
public class UploadFileStream {

    /**
     * Requête http initiale
     */
    private final HttpServletRequest request;

    private UploadFileStream(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * créée un {@link UploadFileStream}
     * @param request
     * @return
     */
    public static UploadFileStream fromRequest(final HttpServletRequest request) {
        return new UploadFileStream(request);
    }

    /**
     * Pour chaque {@link UploadFile}, va lancer une opération dessus, en les transformant potentiellement.
     * @param action action à réaliser
     * @param <T> type en sortie de l'action
     * @return stream de T
     * @throws HomeServerException -
     */
    public <T> Stream<T> foreach(final Function<UploadFile, T> action) throws HomeServerException {

        List<T> returnValue = new ArrayList<>();

        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterStream = upload.getItemIterator(request);
            while (iterStream.hasNext()) {
                FileItemStream item = iterStream.next();
                if (!item.isFormField()) {
                    returnValue.add(action.apply(new UploadFile(item)));
                }
            }
        } catch(FileUploadException |IOException e) {
            throw new HomeServerException("Erreur rencontrée lors de l'upload d'un fichier", e);
        }

        return returnValue.stream();
    }

    public <T> T first(final Function<UploadFile, T> action) throws HomeServerException {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterStream = upload.getItemIterator(request);
            if(iterStream.hasNext()) {
                FileItemStream item = iterStream.next();
                if (!item.isFormField()) {
                    return action.apply(new UploadFile(item));
                }
            }
        } catch(FileUploadException |IOException e) {
            throw new HomeServerException("Erreur rencontrée lors de l'upload d'un fichier", e);
        }

        throw new HomeServerException("Aucun fichier récupéré");
    }



}
