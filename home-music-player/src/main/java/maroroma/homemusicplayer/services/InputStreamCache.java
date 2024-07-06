package maroroma.homemusicplayer.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.PlayList;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class InputStreamCache {

    private final Map<String, TimestampedInputStream> innerCache = new ConcurrentHashMap<>();

    private final FilesFactory filesFactory;

    private final int teaseSize;
    private final int cacheMaxSize;

    public InputStreamCache(FilesFactory filesFactory,
                            @Value("${musicplayer.caches.inpustream.tease}") int teaseSize,
                            @Value("${musicplayer.caches.inpustream.max-size}")int cacheMaxSize) {
        this.filesFactory = filesFactory;
        this.teaseSize = teaseSize;
        this.cacheMaxSize = cacheMaxSize;
    }

    public InputStream getInputStream(TrackEntity trackEntity) {
        return this.getInputStream(this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath()));
    }

    private InputStream getInputStream(FileAdapter fileAdapter) {
        return this.innerCache.compute(fileAdapter.pathAsBase64(), (key, existingInputStream) -> {
            if (existingInputStream != null) {
                return Traper.trapOr(existingInputStream::reset, () -> existingInputStream);
            } else {
                return TimestampedInputStream.init(fileAdapter);
            }
        }).getInputStream();
    }

    @Async
    public void populate(PlayList playList) {
        playList.teaseNextTracks(this.teaseSize).stream()
                .map(aTrack -> this.filesFactory.getFileFromBase64Path(aTrack.getLibraryItemPath()))
                .forEach(this::populate);

        if (this.innerCache.size() > this.cacheMaxSize) {
            log.info("cache full : {} to remove", this.innerCache.size() - this.cacheMaxSize);
            var itemKeysToRemove = this.innerCache.entrySet().stream()
                    .sorted(Comparator.comparing(entry -> entry.getValue().getLocalDateTime()))
                    .limit(this.innerCache.size() - this.cacheMaxSize)
                    .map(Map.Entry::getKey)
                    .toList();

            itemKeysToRemove
                    .forEach(aKeyToRemove -> {
                        var removedItem = this.innerCache.remove(aKeyToRemove);
                        log.info("removed from cache -> {}", removedItem.readableItemName);
                    });

            log.info("cache cleaned : {} items remaining", this.innerCache.size());

        }
    }

    private void populate(FileAdapter fileAdapter) {
        this.innerCache.computeIfAbsent(fileAdapter.pathAsBase64(), key -> TimestampedInputStream.init(fileAdapter));
    }

    @AllArgsConstructor
    private static class TimestampedInputStream {

        private final String readableItemName;

        @Getter
        private final InputStream inputStream;
        @Getter
        private LocalDateTime localDateTime;

        static TimestampedInputStream init(FileAdapter inputStream) {
            var start = System.currentTimeMillis();
            var memoryInputStream = inputStream.getInMemoryInputStream();
            InputStreamCache.log.info("added in {} ms in cache -> {}", System.currentTimeMillis() - start, inputStream.getFileName());
            return new TimestampedInputStream(inputStream.getFileName(), memoryInputStream, LocalDateTime.now());
        }

        TimestampedInputStream reset() {
            InputStreamCache.log.info("{} refreshed", this.readableItemName);
            this.localDateTime = LocalDateTime.now();
            Traper.trapToBoolean(inputStream::reset);
            return this;
        }
    }

}
