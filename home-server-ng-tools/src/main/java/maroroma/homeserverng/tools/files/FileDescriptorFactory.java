package maroroma.homeserverng.tools.files;

import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.util.Base64Utils;

/**
 * Permet de générer un {@link FileDescriptor} en fonction de sa source technique
 */
public class FileDescriptorFactory {

    /**
     * GEstionnaire de sécurité
     */
    private SecurityManager securityManager;

    /**
     * Chemin du fichier à produire
     */
    private String path;

    private FileDescriptorFactory(String path) {
        this.path = path;
    }

    /**
     * Retourne une {@link FileDescriptorFactory} à partir d'un identifiant de fichier
     * @param id -
     * @return -
     */
    public static FileDescriptorFactory fromId(String id) {
        return fromPath(new String(Base64Utils.decodeFromString(id)));
    }

    /**
     * Retourne une {@link FileDescriptorFactory} à partir d'un chemin de fichier
     * @param path -
     * @return -
     */
    public static FileDescriptorFactory fromPath(String path) {
        return new FileDescriptorFactory(path);
    }

    public FileDescriptorFactory withSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
        return this;
    }

    /**
     * Rajoute un fragment de chemin au path initial
     * @param pathFragment -
     * @return -
     */
    public FileDescriptorFactory combinePath(String pathFragment) {
        this.path = this.path + "/" + pathFragment;
        return this;
    }

    /**
     * Retounne un {@link FileDescriptor}
     * @return
     */
    public FileDescriptor fileDescriptor() {
        return new FileDescriptor(resolveAdapter());
    }

    /**
     * Retourne le bon adapteur pour alimenter le {@link FileDescriptor}
     * @return
     */
    private AbstractFileDescriptorAdapter resolveAdapter() {
        Assert.hasLength(this.path, "path must be fullfiled");
        if (path.startsWith("smb://")) {
            return new SmbFileDescriptorAdapter(path, this.securityManager);
        } else {
            return new LocalFileDescriptorAdapter(path, this.securityManager);
        }
    }

    /**
     * Retourne un {@link FileDirectoryDescriptor}
     * @param parseFiles -
     * @param parseDirectories -
     * @return -
     */
    public FileDirectoryDescriptor fileDirectoryDescriptor(final boolean parseFiles, final boolean parseDirectories) {
        return new FileDirectoryDescriptor(resolveAdapter(), parseFiles, parseDirectories);
    }
}
