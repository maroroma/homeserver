package maroroma.homemusicplayer.services.mp3;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.SynchronizedPlayList;
import maroroma.homemusicplayer.services.caches.ParameterizedLock;
import maroroma.homemusicplayer.tools.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

@Component
@Slf4j
public class InputStreamManager {

    private final SynchronizedPlayList synchronizedPlayList;
    private final FilesFactory filesFactory;
    private final String localFileSystemCachePath;
    private final int cacheMaxSize;
    private final ParameterizedLock parameterizedLock = new ParameterizedLock();
    private final AtomicBoolean isLoadingCurrentTrack = new AtomicBoolean(false);
    private final AtomicBoolean forceStopRequired = new AtomicBoolean(false);

    public InputStreamManager(SynchronizedPlayList synchronizedPlayList,
                              FilesFactory filesFactory,
                              @Value("${musicplayer.caches.localcache.directory}") String localFileSystemCachePath,
                              @Value("${musicplayer.caches.localcache.max-size}") int cacheMaxSize) {
        this.synchronizedPlayList = synchronizedPlayList;
        this.filesFactory = filesFactory;
        this.localFileSystemCachePath = localFileSystemCachePath;
        this.cacheMaxSize = cacheMaxSize;
    }

    public Optional<TrackWithInputStream> resolveCurrentInputStream() {
        return synchronizedPlayList
                .getOptionalCurrentTrack()
                .map(currentTrack -> {
                    try {
                        this.isLoadingCurrentTrack.set(true);
                        return this.copyToLocalFile(currentTrack);
                    } finally {
                        this.isLoadingCurrentTrack.set(false);
                    }
                });
    }

    public boolean isLoadingCurrentTrack() {
        return this.isLoadingCurrentTrack.get();
    }

    public void clearAllLocalFileCache() {
        log.info("Clearing local file cache for musicplayer");
        this.parameterizedLock.clear();
        localFileSystemCacheDirectory()
                .getFiles()
                .forEach(FileAdapter::delete);
        log.info("Local file cache cleared");
    }

    @Scheduled(fixedRate = 10, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    public void cleanOversizedCache() {
        var filesIntoLocalCache = localFileSystemCacheDirectory().getFiles();

        if (filesIntoLocalCache.size() > this.cacheMaxSize) {
            log.info("local file cache is oversized : {} elements vs {} max size", filesIntoLocalCache.size(), this.cacheMaxSize);
            var fileFromCurrentPlayListIntoCache = synchronizedPlayList.fullTrackList()
                    .stream()
                    .map(this::generateLocalCacheFileAdapter)
                    .map(FileAdapter::pathAsBase64)
                    .collect(Collectors.toSet());
            log.info("{} elements will not be cleaned", fileFromCurrentPlayListIntoCache.size());
            filesIntoLocalCache.stream()
                    .filter(fileFromLocalCache -> !fileFromCurrentPlayListIntoCache.contains(fileFromLocalCache.pathAsBase64()))
                    .forEach(FileAdapter::delete);

            log.info("local file cache cleaned, {} remaining elements after cleanup process", localFileSystemCacheDirectory().getFiles().size());
        } else {
            log.info("local file cache is ok : {} elements vs {} max size", filesIntoLocalCache.size(), this.cacheMaxSize);
        }
    }

    @Scheduled(fixedDelay = 1000)
    @Synchronized
    public void populateCache() {
        var tracksToPutInCache = synchronizedPlayList.allTracksButCurrentTrack()
                .stream()
                .filter(aTrack -> !generateLocalCacheFileAdapter(aTrack).exists())
                .toList();
        if (tracksToPutInCache.isEmpty()) {
            return;
        }
        log.info("Updating local file cache with playlist content ({} files)", tracksToPutInCache.size());
        try {
            for (int i = 0; i < tracksToPutInCache.size() && !synchronizedPlayList.isEmpty() && !this.forceStopRequired.get(); i++) {
                this.copyToLocalFile(tracksToPutInCache.get(i));
                log.info("{}/{} track added to cache", (i + 1) , tracksToPutInCache.size());
            }
        } finally {
            if (synchronizedPlayList.isEmpty() || forceStopRequired.get()) {
                log.info("Stop required during cache population, stop current populating cache process");
            }
            this.forceStopRequired.set(false);
        }
        log.info("Local file cache updated  with playlist content ({} files)", tracksToPutInCache.size());
    }

    @EventListener
    @Async
    public void stopPlayListEventListener(final SynchronizedPlayList.StoppedPlayListEvent event) {
        this.forceStopRequired.set(true);
    }

    private TrackWithInputStream copyToLocalFile(TrackEntity trackEntity) {
        // c un peu la fête sur le multithreading, du coup on lock par fichier histoire qu'on est pas une recopie en cours
        // qui renvoie sur un deuxieme appel que le fichier est ok, et le charge en mémoire en mode tout pourri
        synchronized (parameterizedLock.getLock(trackEntity)) {

            // fichier initial
            var trackFileAdapter = this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath());

            // fichier au niveau cache (on utilise l'identifiant)
            var localCacheFileAdapter = generateLocalCacheFileAdapter(trackEntity);

            if (!localCacheFileAdapter.exists()) {
                log.info("<{}> loading into localcache", FileUtils.convertBase64ToPath(localCacheFileAdapter.getFileName()));
                var start = System.currentTimeMillis();
                localCacheFileAdapter.createFile();
                trackFileAdapter.copyTo(localCacheFileAdapter);
                log.info("<{}> added in {} ms in localfilecache",
                        FileUtils.convertBase64ToPath(localCacheFileAdapter.getFileName()),
                        System.currentTimeMillis() - start
                );
            } else {
                log.info("<{}> already in localfilecache", FileUtils.convertBase64ToPath(localCacheFileAdapter.getFileName()));
            }

            return new TrackWithInputStream(localCacheFileAdapter.getInputStream(), trackEntity);
        }
    }

    private FileAdapter generateLocalCacheFileAdapter(TrackEntity trackEntity) {
        // fichier initial
        var trackFileAdapter = this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath());
        return generateLocalCacheFileAdapter(trackFileAdapter);
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

    public record TrackWithInputStream(InputStream inputStream, TrackEntity trackEntity) {
    }

}
