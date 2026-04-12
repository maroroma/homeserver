package maroroma.homemusicplayer.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocalResourcesService {
    private final FilesFactory filesFactory;
    private final AlbumService albumService;


    public void getThumb(String base64ThumbFileName, HttpServletResponse response) {
        this.getFile(filesFactory.validateBase64PathAsThumbSourceChild(base64ThumbFileName), response);
    }

    public void getThumb(UUID albumId, HttpServletResponse response) {
        var album = this.albumService.getAlbum(albumId)
                .orElseThrow(() -> new MusicPlayerException("Album with id " + albumId + " not found"));
        this.getFile(this.filesFactory.getFileFromBase64Path(album.getThumbPath()), response);
    }

    public void getFanart(String base64ThumbFileName, HttpServletResponse response) {
        this.getFile(filesFactory.validateBase64PathAsFanartSourceChild(base64ThumbFileName), response);
    }

    private void getFile(final FileAdapter fileToDownload, final HttpServletResponse response) {

        // récupération du fichier
        if (fileToDownload.size() > 0) {
            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + fileToDownload.size());
        }

        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(Duration.ofDays(5)).getHeaderValue());

        fileToDownload.copyTo(Traper.trap(response::getOutputStream));
    }

}
