package maroroma.homeserverng.tools.files;

import maroroma.homeserverng.tools.exceptions.Traper;

import java.net.*;

public class SmbFileDescriptorPath implements FileDescriptorPath {

    private final String rawPath;

    public SmbFileDescriptorPath(SmbFileDescriptorAdapter fileDescriptor) {
        this.rawPath = fileDescriptor.getFullName();
    }

    @Override
    public String resolvePathAsString() {
        return Traper.trapOr(() -> new URI(this.rawPath).toString(), () -> "WTF");
    }
}
