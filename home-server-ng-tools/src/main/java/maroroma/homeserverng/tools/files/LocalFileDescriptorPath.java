package maroroma.homeserverng.tools.files;

import java.nio.file.*;

public class LocalFileDescriptorPath implements FileDescriptorPath {


    private final String rawPath;

    public LocalFileDescriptorPath(LocalFileDescriptorAdapter fileDescriptor) {
        this.rawPath = fileDescriptor.getFullName();
    }

    @Override
    public String resolvePathAsString() {
        return  Path.of(this.rawPath).toString();
    }
}
