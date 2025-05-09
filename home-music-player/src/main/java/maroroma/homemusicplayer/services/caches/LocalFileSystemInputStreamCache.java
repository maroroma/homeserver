package maroroma.homemusicplayer.services.caches;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.PlayList;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.*;

@Slf4j
@SuperBuilder
public class LocalFileSystemInputStreamCache extends AbstractInputStreamCache {

    private final MemoryInputStreamCache memoryInputStreamCache;
    private final String localFileSystemCachePath;


    @Override
    public InputStream getInputStream(TrackEntity trackEntity) {
        var localCacheFileAdapter = this.copyToLocalFile(trackEntity);

        return this.memoryInputStreamCache.getInputStream(trackEntity.replaceLibraryItemPath(localCacheFileAdapter));

    }

    private FileAdapter localFileSystemCacheDirectory() {
        var result = this.filesFactory.getFileFromPath(this.localFileSystemCachePath);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    private FileAdapter copyToLocalFile(TrackEntity trackEntity) {
        var localFileCacheDirectory = localFileSystemCacheDirectory();

        var trackFileAdapter = this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath());

        var localCacheFileAdapter = localFileCacheDirectory.combine(trackEntity.getId().toString() + "." + trackFileAdapter.getExtension());

        if (!localCacheFileAdapter.exists()) {
            var start = System.currentTimeMillis();
            trackFileAdapter.createFile();
            trackFileAdapter.copyTo(localCacheFileAdapter);
            log.info("added in {} ms in localfilecache -> {}", System.currentTimeMillis() - start, localCacheFileAdapter.getFileName());
        } else {
            log.info("{} already in localfilecache",  localCacheFileAdapter.getFileName());
        }

        return localCacheFileAdapter;
    }

    @Async
    @Override
    public void populate(PlayList playList) {

        playList.teaseNextTracks(this.teaseSize)
                .stream()
                .map(aTrackEntity -> aTrackEntity.replaceLibraryItemPath(this.copyToLocalFile(aTrackEntity)))
                .forEach(this::getInputStream);

        this.memoryInputStreamCache.cleanOversizedCache();
        this.cleanOversizedCache();

    }

    @Override
    public void cleanOversizedCache() {
        var localFileCacheDirectory = localFileSystemCacheDirectory();

        var actualCacheSize = localFileCacheDirectory.getFiles().size();


        if (localFileCacheDirectory.getFiles().size() > this.cacheMaxSize) {
            localFileCacheDirectory.getFiles().stream()
                    .sorted(Comparator.comparing(FileAdapter::createFile))
                    .limit(actualCacheSize - this.cacheMaxSize)
                    .forEach(aFileToRemove -> {
                        aFileToRemove.delete();
                        log.info("removed from localfilecache -> {}", aFileToRemove.getFileName());
                    });
        } else {
            log.info("localfilecache à {} éléments, pas de purge", actualCacheSize);
        }

    }
}
