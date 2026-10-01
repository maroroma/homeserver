package maroroma.homemusicplayer.services.caches;

import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.tools.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TracksCache {

    private final ParameterizedLock parameterizedLock = new ParameterizedLock();
    private final String localFileSystemCachePath;
    private final FilesFactory filesFactory;
    private final int cacheMaxSize;


    public TracksCache(FilesFactory filesFactory,
                       @Value("${musicplayer.caches.localcache.directory}") String localFileSystemCachePath,
                       @Value("${musicplayer.caches.localcache.max-size}") int cacheMaxSize
    ) {
        this.localFileSystemCachePath = localFileSystemCachePath;
        this.filesFactory = filesFactory;
        this.cacheMaxSize = cacheMaxSize;
    }


    public FileAdapter getTrackFileAdapter(TrackEntity trackEntityToLoad) {
        var remoteTrackFileAdapter = this.filesFactory.getFileFromBase64Path(trackEntityToLoad.getLibraryItemPath());
        var localCacheFileAdapter = generateLocalCacheFileAdapter(remoteTrackFileAdapter);

        synchronized (parameterizedLock.getLock(localCacheFileAdapter)) {
            if (!localCacheFileAdapter.exists()) {
                log.info("<{}> loading into localcache", FileUtils.convertBase64ToPath(localCacheFileAdapter.getFileName()));
                var start = System.currentTimeMillis();
                localCacheFileAdapter.createFile();
                remoteTrackFileAdapter.copyTo(localCacheFileAdapter);
                log.info("<{}> added in {} ms in localfilecache",
                        FileUtils.convertBase64ToPath(localCacheFileAdapter.getFileName()),
                        System.currentTimeMillis() - start
                );
            } else {
                log.info("<{}> already in localfilecache", FileUtils.convertBase64ToPath(localCacheFileAdapter.getFileName()));
            }
        }

        return localCacheFileAdapter;
    }

    @Scheduled(fixedRate = 10, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    public void cleanOversizedCache() {
        var filesIntoLocalCache = localFileSystemCacheDirectory().getFiles();

        if (filesIntoLocalCache.size() > this.cacheMaxSize) {
            log.info("local file cache is oversized : {} elements vs {} max size", filesIntoLocalCache.size(), this.cacheMaxSize);

            clearCache(filesIntoLocalCache);

            log.info("local file cache cleaned, {} remaining elements after cleanup process", localFileSystemCacheDirectory().getFiles().size());
        } else {
            log.info("local file cache is ok : {} elements vs {} max size", filesIntoLocalCache.size(), this.cacheMaxSize);
        }
    }

    public void clearCache() {
        clearCache(localFileSystemCacheDirectory().getFiles());
    }

    public void clearCache(List<FileAdapter> filesToDelete) {
        log.info("start cleaning cache");
        filesToDelete
                .forEach(aFileToDelete -> {
                    synchronized (parameterizedLock.getLock(aFileToDelete)) {
                        aFileToDelete.delete();
                    }
                });
        log.info("cache cleaned");
    }

    private FileAdapter generateLocalCacheFileAdapter(FileAdapter trackFileAdapter) {
        // fichier initial
        return localFileSystemCacheDirectory().combine(FileUtils.convertPathToBase64(trackFileAdapter.getFileName()));
    }

    private FileAdapter localFileSystemCacheDirectory() {
        var result = this.filesFactory.getFileFromPath(this.localFileSystemCachePath);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

}
