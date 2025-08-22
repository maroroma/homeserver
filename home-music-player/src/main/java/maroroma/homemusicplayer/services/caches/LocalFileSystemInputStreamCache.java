package maroroma.homemusicplayer.services.caches;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.FileUtils;
import maroroma.homemusicplayer.tools.PlayList;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.*;

@Slf4j
@SuperBuilder
public class LocalFileSystemInputStreamCache extends AbstractInputStreamCache {

    private final MemoryInputStreamCache memoryInputStreamCache;
    private final String localFileSystemCachePath;
    private final ParameterizedLock parameterizedLock = new ParameterizedLock();


    @Override
    public InputStream getInputStream(TrackEntity trackEntity) {

        log.info("<{}> requested for playing right now", trackEntity.getName());

        // tentative de recopie du fichier en local si inexistant
        var localCacheFileAdapter = this.copyToLocalFile(trackEntity);

        // création d'un inputstream à partir de ce fichier
        return this.memoryInputStreamCache.getInputStream(trackEntity.replaceLibraryItemPath(localCacheFileAdapter));

    }

    /**
     * @return Répertoire de base du cache
     */
    private FileAdapter localFileSystemCacheDirectory() {
        var result = this.filesFactory.getFileFromPath(this.localFileSystemCachePath);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    private FileAdapter generateLocalCacheFileAdapter(FileAdapter trackFileAdapter) {
        return localFileSystemCacheDirectory().combine(FileUtils.convertPathToBase64(trackFileAdapter.getFileName()));
    }

    private FileAdapter copyToLocalFile(TrackEntity trackEntity) {
        // c un peu la fête sur le multithreading, du coup on lock par fichier histoire qu'on est pas une recopie en cours
        // qui renvoie sur un deuxieme appel que le fichier est ok, et le charge en mémoire en mode tout pourri
        synchronized(parameterizedLock.getLock(trackEntity)) {

            // fichier initial
            var trackFileAdapter = this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath());

            // fichier au niveau cache (on utilise l'identifiant)
            var localCacheFileAdapter = generateLocalCacheFileAdapter(trackFileAdapter);

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

            return localCacheFileAdapter;
        }
    }

    @Async
    @Override
    public void populate(PlayList playList) {
        log.info("START POPULATE by teasing {} elements", this.teaseSize);
        // on ne fait plus la copie mémoire sur le teasing, l'écriture en fichier
        // accélère suffisament le process
        playList.teaseNextTracks(this.teaseSize)
                .forEach(this::copyToLocalFile);
        this.cleanOversizedCache();
        log.info("END POPULATE by teasing {} elements", this.teaseSize);
    }

    @Override
    public void cleanOversizedCache() {
        this.memoryInputStreamCache.cleanOversizedCache();

        var localFileCacheDirectory = localFileSystemCacheDirectory();

        var actualCacheSize = localFileCacheDirectory.getFiles().size();

        if (localFileCacheDirectory.getFiles().size() > this.cacheMaxSize) {
            localFileCacheDirectory.getFiles().stream()
                    .sorted(Comparator.comparing(FileAdapter::createFile))
                    .limit(actualCacheSize - this.cacheMaxSize)
                    .forEach(aFileToRemove -> {
                        aFileToRemove.delete();
                        log.info("<{}> removed from localfilecache", FileUtils.convertBase64ToPath(aFileToRemove.getFileName()));
                    });
        } else {
            log.info("localfilecache à <{}> éléments, pas de purge", actualCacheSize);
        }

    }

    @Override
    public void cleanOnStop() {
        this.memoryInputStreamCache.cleanOnStop();
        this.parameterizedLock.clear();
    }

    @Override
    public int getCacheCurrentSize() {
        return this.memoryInputStreamCache.getCacheCurrentSize();
    }
}
