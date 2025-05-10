package maroroma.homemusicplayer.model.upload;

import jakarta.servlet.http.HttpServletRequest;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Classe utilitaire pour faciliter la manipulation des fichiers en cours d'upload
 */
public class UploadFileStream {

    /**
     * Requête http initiale
     */
    private final HttpServletRequest request;

    private UploadFileStream(final jakarta.servlet.http.HttpServletRequest request) {
        this.request = request;
    }

    /**
     * créée un {@link UploadFileStream}
     * @param request
     * @return
     */
    public static UploadFileStream fromRequest(final jakarta.servlet.http.HttpServletRequest request) {
        return new UploadFileStream(request);
    }

    /**
     * Pour chaque {@link UploadFile}, va lancer une opération dessus, en les transformant potentiellement.
     * @param action action à réaliser
     * @param <T> type en sortie de l'action
     * @return stream de T
     * @throws MusicPlayerException -
     */
    public <T> Stream<T> foreach(final Function<UploadFile, T> action) {

        List<T> returnValue = new ArrayList<>();

        try {
            var upload = new JakartaServletFileUpload<>();
            var iterStream = upload.getItemIterator(request);
            while (iterStream.hasNext()) {
                var item = iterStream.next();
                if (!item.isFormField()) {
                    returnValue.add(action.apply(new UploadFile(item)));
                }
            }
        } catch(IOException e) {
            throw new MusicPlayerException("Erreur rencontrée lors de l'upload d'un fichier", e);
        }

        return returnValue.stream();
    }





}
