package maroroma.homeserverng.tools.files;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.security.SimpleUser;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

/**
 * Implémentation de l'adapteur pour des fichiers smb
 */
public class SmbFileDescriptorAdapter extends AbstractFileDescriptorAdapter {

    private final SmbFile smbFile;

    private final SambaUserSupplier sambaUserSupplier;




    SmbFileDescriptorAdapter(SmbFileDescriptorAdapter parent, SmbFile smbFile) {
        this.smbFile = smbFile;
        this.sambaUserSupplier = parent.sambaUserSupplier;
    }

    public SmbFileDescriptorAdapter(String smbFile, SambaUserSupplier sambaUserSupplier) {
        this.sambaUserSupplier = sambaUserSupplier;
        this.smbFile = Traper.trap(() -> new SmbFile(smbFile, mapSmbUser()));
    }

    private NtlmPasswordAuthentication mapSmbUser() {
        return new NtlmPasswordAuthentication("",
                this.sambaUserSupplier.generateSambaUser().getLogin(),
                this.sambaUserSupplier.generateSambaUser().getPassword());
    }

    @Override
    public String getName() {
        if (this.smbFile.getName().endsWith("/")) {
            return this.smbFile.getName().substring(0, this.smbFile.getName().length() -1 );
        } else {
            return this.smbFile.getName();
        }
    }

    @Override
    public String getFullName() {
        return this.smbFile.getCanonicalPath();
    }

    @Override
    public String getParent() {
        return this.smbFile.getParent();
    }

    @Override
    public long size() {
        if(this.isFile()) {
            return Traper.trap(this.smbFile::length);
        } else {
            return 0;
        }
    }

    @Override
    public boolean exists() {
        return Traper.trap(this.smbFile::exists);
    }

    @Override
    protected Stream<AbstractFileDescriptorAdapter> listAllFileAdapters() {
        return Traper.trap(() -> Stream
                    .of(this.smbFile.listFiles())
                    .map(aListedSambaFile -> new SmbFileDescriptorAdapter(this, aListedSambaFile)));
    }

    @Override
    protected boolean simpleDelete() {
        return Traper.trapToBoolean(this.smbFile::delete);
    }

    @Override
    protected boolean moveByRename(FileDescriptor target) {
        SmbFile dest = Traper.trap(() -> new SmbFile(target.getFullName(), mapSmbUser()));
        return Traper.trapToBoolean(() -> this.smbFile.renameTo(dest));
    }

    @Override
    public boolean isFile() {
        return Traper.trap(this.smbFile::isFile);
    }

    @Override
    public boolean isDirectory() {
        return Traper.trap(this.smbFile::isDirectory);
    }

    @Override
    public boolean mkdir() {
        return Traper.trapToBoolean(this.smbFile::mkdir);
    }

    @Override
    public boolean mkdirs() {
        return Traper.trapToBoolean(this.smbFile::mkdirs);
    }

    @Override
    public boolean rename(String newName) {
        SmbFile dest = Traper.trap(() -> new SmbFile(this.getParent(), newName, mapSmbUser()));
        return Traper.trapToBoolean(() -> this.smbFile.renameTo(dest));
    }

    @Override
    public OutputStream getOutputStream() {
        return Traper.trap(() ->  new SmbFileOutputStream(this.smbFile));
    }

    @Override
    public InputStream getInputStream() {
        return Traper.trap(() ->  new SmbFileInputStream(this.smbFile));
    }

    @Override
    public Path toPath() {
        return Path.of(this.getFullName());
    }

    @Override
    public AbstractFileDescriptorAdapter combinePath(String path) {
        return null;
    }

    @Override
    public FileDescriptorPath toFileDescriptorPath() {
        return new SmbFileDescriptorPath(this);
    }


    public interface SambaUserSupplier {
        SimpleUser generateSambaUser();
    }
}
