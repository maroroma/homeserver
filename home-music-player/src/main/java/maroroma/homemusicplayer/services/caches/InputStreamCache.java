package maroroma.homemusicplayer.services.caches;

import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.PlayList;
import org.springframework.scheduling.annotation.Async;

import java.io.*;

public interface InputStreamCache {
    InputStream getInputStream(TrackEntity trackEntity);

    @Async
    void populate(PlayList playList);

    void cleanOversizedCache();
}
