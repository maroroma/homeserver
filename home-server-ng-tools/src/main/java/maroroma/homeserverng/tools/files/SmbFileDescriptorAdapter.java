package maroroma.homeserverng.tools.files;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.security.SecurityManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

public class SmbFileDescriptorAdapter extends AbstractFileDescriptorAdapter {

    private final SmbFile smbFile;

    public SmbFileDescriptorAdapter(SmbFile smbFile) {
        super(null);
        this.smbFile = smbFile;
    }

    public SmbFileDescriptorAdapter(String smbFile, SecurityManager securityManager) {
        super(securityManager);
        this.smbFile = Traper.trap(() -> new SmbFile(smbFile, mapSmbUser()));
    }

    private NtlmPasswordAuthentication mapSmbUser() {
        return new NtlmPasswordAuthentication("", this.getSecurityManager().getSambaUser().getLogin(), this.getSecurityManager().getSambaUser().getPassword());
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
                    .map(SmbFileDescriptorAdapter::new));
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
}
