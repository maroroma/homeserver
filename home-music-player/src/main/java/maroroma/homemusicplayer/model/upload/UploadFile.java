package maroroma.homemusicplayer.model.upload;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import org.apache.commons.fileupload2.core.FileItemInput;

import java.io.*;

@RequiredArgsConstructor
public class UploadFile {

    private final FileItemInput fileItemStream;

    public String getFileName() {
        return this.fileItemStream.getName();
    }

    public InputStream getInputStream() {
        try {
            return this.fileItemStream.getInputStream();
        } catch (IOException e) {
            throw new MusicPlayerException("Impossible de récupérer le flux du fichier uploadé" + this.getFileName());
        }
    }
}
