package maroroma.homemusicplayer.tools;

import maroroma.homemusicplayer.model.files.FileAdapter;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.*;

public abstract class CustomAssert extends Assert {
    private CustomAssert() {

    }

    public static void notAllNull(String message, Object... toBeTested) {
        isFalse(Stream.of(toBeTested)
                .allMatch(Objects::isNull), message);
    }

    public static void notAllNotNull(String message, Object... toBeTested) {
        isFalse(Stream.of(toBeTested)
                .allMatch(Objects::nonNull), message);
    }

    public static void isFalse(boolean actual, String message) {
        isTrue(!actual, message);
    }

    public static void artistIdNotNull(UUID artistId) {
        idNotNull(artistId, "artistId can't be null");
    }

    public static void albumIdNotNull(UUID artistId) {
        idNotNull(artistId, "albumId can't be null");
    }

    public static void trackIdNotNull(UUID trackId) {
        idNotNull(trackId, "trackId can't be null");
    }

    public static void idNotNull(UUID id, String message) {
        notNull(id, message);
    }

    public static void fileExists(FileAdapter fileAdapter) {
        Assert.notNull(fileAdapter, "fileAdapter should not be null");
        Assert.isTrue(fileAdapter.exists(), String.format("file <%s> should exists", fileAdapter.getResolvedPath()));
    }

    public static void isDirectory(FileAdapter fileAdapter) {
        fileExists(fileAdapter);
        Assert.isTrue(fileAdapter.isDirectory(), String.format("file <%s> is not a directory", fileAdapter.getResolvedPath()));
    }
}
