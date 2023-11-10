package maroroma.homeserverng.filemanager.services;

import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import org.springframework.util.Base64Utils;

import java.util.*;

public interface FilesFactory {

    static String convertIdToPath(String id) {
        return new String(Base64Utils.decodeFromString(id));
    }

    default FileDescriptor fileFromId(String id) {
        return fileFromPath(convertIdToPath(id));
    }
    default FileDescriptor fileFromProperty(HomeServerPropertyHolder homeServerPropertyHolder) {
        return fileFromPath(homeServerPropertyHolder.getResolvedValue());
    }


    FileDescriptor fileFromPath(String path);


    default FileDirectoryDescriptor directoryFromId(String id, DirectoryParsingOptions... directoryParsingOptions) {
        return directoryFromPath(convertIdToPath(id), directoryParsingOptions);
    }

    default FileDirectoryDescriptor directoryFromProperty(HomeServerPropertyHolder homeServerPropertyHolder, DirectoryParsingOptions... directoryParsingOptions) {
        return directoryFromPath(homeServerPropertyHolder.getResolvedValue(), directoryParsingOptions);
    }

    FileDirectoryDescriptor directoryFromPath(String path, DirectoryParsingOptions... directoryParsingOptions);


    enum DirectoryParsingOptions {
        PARSE_FILES,
        PARSE_DIRECTORIES;


        static boolean shouldParseFiles(DirectoryParsingOptions... options) {
            return List.of(options).contains(PARSE_FILES);
        }

        static boolean shouldParseDirectories(DirectoryParsingOptions... options) {
            return List.of(options).contains(PARSE_DIRECTORIES);
        }



    }

}
