package maroroma.homemusicplayer.tools;

import maroroma.homemusicplayer.model.files.FileAdapter;

import java.util.*;

public class FileUtils {
    private FileUtils() {}

    public static String convertBase64ToPath(String base64Path) {
        return new String(Base64.getDecoder().decode(base64Path));
    }

    public static String convertPathToBase64(String path) {
        return new String(Base64.getEncoder().encode(path.getBytes()));
    }

    public static Optional<String> getExtension(FileAdapter fileAdapter) {
        return getExtension(fileAdapter.getFileName());
    }

    public static Optional<String> getExtension(String fileName) {
        return Optional.ofNullable(fileName)
                .map(aFileName -> aFileName.split("\\."))
                .filter(splitted -> splitted.length > 1)
                .map(splitted -> splitted[splitted.length-1]);
    }


}
