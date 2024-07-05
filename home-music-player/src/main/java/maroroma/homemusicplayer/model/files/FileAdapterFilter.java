package maroroma.homemusicplayer.model.files;

import maroroma.homemusicplayer.tools.FileUtils;
import maroroma.homemusicplayer.tools.StreamUtils;

import java.util.*;
import java.util.function.*;

public interface FileAdapterFilter extends Predicate<FileAdapter> {

    static FileAdapterFilter nameStartingBy(List<String> acceptedNames) {
        return aFileAdapter -> StreamUtils.of(acceptedNames)
                .map(String::toLowerCase)
                .anyMatch(anAcceptedName -> aFileAdapter.getFileName().toLowerCase().startsWith(anAcceptedName));
    }

    static FileAdapterFilter extensionIn(List<String> acceptedExtensions) {
        return aFileAdapter -> StreamUtils.of(acceptedExtensions)
                .map(String::toLowerCase)
                .anyMatch(anAcceptedExtension -> FileUtils.getExtension(aFileAdapter)
                        .map(fileExtension -> fileExtension.equals(anAcceptedExtension))
                        .orElse(false));
    }

    static FileAdapterFilter isDirectory() {
        return FileAdapter::isDirectory;
    }

    static FileAdapterFilter base64PathNotIn(List<String> excludedBase64Paths) {
        return aFileAdapter -> !excludedBase64Paths.contains(aFileAdapter.pathAsBase64());
    }
}
