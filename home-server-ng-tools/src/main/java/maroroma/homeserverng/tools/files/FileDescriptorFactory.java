package maroroma.homeserverng.tools.files;

import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.util.Base64Utils;

public class FileDescriptorFactory {

    private SecurityManager securityManager;
    private String path;

    private FileDescriptorFactory(String path) {
        this.path = path;
    }

    public static FileDescriptorFactory fromId(String id) {
        return fromPath(new String(Base64Utils.decodeFromString(id)));
    }

    public static FileDescriptorFactory fromPath(String path) {
        return new FileDescriptorFactory(path);
    }

    public FileDescriptorFactory withSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
        return this;
    }

    public FileDescriptorFactory combinePath(String pathFragment) {
        this.path = this.path + "/" + pathFragment;
        return this;
    }

    public FileDescriptor fileDescriptor() {
        return new FileDescriptor(resolveAdapter());
    }

    private AbstractFileDescriptorAdapter resolveAdapter() {
        Assert.hasLength(this.path, "path must be fullfiled");
        AbstractFileDescriptorAdapter fileDescriptorAdapter = null;
        if (path.startsWith("smb://")) {
            fileDescriptorAdapter = new SmbFileDescriptorAdapter(path, this.securityManager);
        } else {
            fileDescriptorAdapter = new LocalFileDescriptorAdapter(path, this.securityManager);
        }
        return fileDescriptorAdapter;
    }

    public FileDirectoryDescriptor fileDirectoryDescriptor(final boolean parseFiles, final boolean parseDirectories) {
        return new FileDirectoryDescriptor(resolveAdapter(), parseFiles, parseDirectories);
    }
}
