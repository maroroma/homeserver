package maroroma.homemusicplayer.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.PlayList;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class InputStreamCache {

    private final Map<String, TimestampedInputStream> innerCache = new ConcurrentHashMap<>();

    private final FilesFactory filesFactory;

    public InputStream getInputStream(TrackEntity trackEntity) {
        return this.getInputStream(this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath()));
    }

    private InputStream getInputStream(FileAdapter fileAdapter) {
        return this.innerCache.compute(fileAdapter.pathAsBase64(), (key, existingInputStream) -> {
            if (existingInputStream != null) {
                return Traper.trapOr(() -> {
                    existingInputStream.reset();
                    return existingInputStream;
                }, () -> existingInputStream);
            } else {
                return TimestampedInputStream.init(fileAdapter.getInMemoryInputStream());
            }
        }).getInputStream();
    }

    @Async
    public void populate(PlayList playList) {
        playList.teaseNextTracks(5).stream()
                .map(aTrack -> this.filesFactory.getFileFromBase64Path(aTrack.getLibraryItemPath()))
                .forEach(this::populate);

        if (this.innerCache.size() > 10) {

        }
    }

    private void populate(FileAdapter fileAdapter) {
        this.innerCache.computeIfAbsent(fileAdapter.pathAsBase64(), key -> TimestampedInputStream.init(fileAdapter.getInMemoryInputStream()));
    }

    @AllArgsConstructor
    private static class TimestampedInputStream {

        @Getter
        private final InputStream inputStream;
        @Getter
        private LocalDateTime localDateTime;

        static TimestampedInputStream init(InputStream inputStream) {
            return new TimestampedInputStream(inputStream, LocalDateTime.now());
        }

        TimestampedInputStream reset() {
            this.localDateTime = LocalDateTime.now();
            Traper.trapToBoolean(inputStream::reset);
            return this;
        }
    }

}
