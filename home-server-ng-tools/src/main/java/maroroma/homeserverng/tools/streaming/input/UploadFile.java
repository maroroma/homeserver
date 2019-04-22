package maroroma.homeserverng.tools.streaming.input;

import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import org.apache.commons.fileupload.FileItemStream;

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
}
