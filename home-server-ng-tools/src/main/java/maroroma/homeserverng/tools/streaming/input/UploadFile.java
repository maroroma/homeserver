package maroroma.homeserverng.tools.streaming.input;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.helpers.FileAndDirectoryHLP;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.apache.commons.fileupload.FileItemStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Encapsule les {@link FileItemStream} pour leur manipulation
 */
public class UploadFile {

    private final FileItemStream fileItemStream;

    UploadFile(FileItemStream item) {
        this.fileItemStream = item;
    }

    public String getFileName() {
        return this.fileItemStream.getName();
    }

    public InputStream getInputStream() {
        try {
            return this.fileItemStream.openStream();
        } catch (IOException e) {
            throw new RuntimeHomeServerException("Impossible de récupérer le flux du fichier uploadé" + this.getFileName());
        }
    }

    /**
     * Recopie le stream d'entrée de la requete pour le fichier à uploader dans le répertoire donné.
     * @param directoryTarget répertoire cible
     * @return -
     */
    public FileDescriptor copyTo(final FileDescriptor directoryTarget) {
        File finalFile = new File(directoryTarget.createFile(), this.fileItemStream.getName());
        try {
            FileAndDirectoryHLP.copyInputStreamToFile(fileItemStream.openStream(), finalFile);
            return new FileDescriptor(finalFile);
        } catch (IOException|HomeServerException e) {
            throw new RuntimeHomeServerException(e);
        }
    }

}
