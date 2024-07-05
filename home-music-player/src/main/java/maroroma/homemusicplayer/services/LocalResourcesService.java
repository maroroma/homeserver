package maroroma.homemusicplayer.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalResourcesService {
    private final FilesFactory filesFactory;


    public void getThumb(String base64ThumbFileName, HttpServletResponse response) {
        this.getFile(filesFactory.validateBase64PathAsThumbSourceChild(base64ThumbFileName), response);
    }

    public void getFanart(String base64ThumbFileName, HttpServletResponse response) {
        this.getFile(filesFactory.validateBase64PathAsFanartSourceChild(base64ThumbFileName), response);
    }

    private void getFile(final FileAdapter fileToDownload, final HttpServletResponse response) {

        // récupération du fichier
        if (fileToDownload.size() > 0) {
            response.setHeader("Content-Length", "" + fileToDownload.size());
        }

        fileToDownload.copyTo(Traper.trap(response::getOutputStream));
    }

}
