package maroroma.homeserverng.tools.files;

import lombok.Getter;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

public abstract class AbstractFileDescriptorAdapter {
    @Getter
    private final SecurityManager securityManager;

    protected AbstractFileDescriptorAdapter(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    protected abstract Stream<AbstractFileDescriptorAdapter> listAllFileAdapters();
    protected abstract boolean simpleDelete();
    protected abstract boolean moveByRename(FileDescriptor target);



    public abstract String getName();
    public abstract String getFullName();
    public abstract String getParent();
    public abstract long size();

    public abstract boolean exists();
    public abstract boolean isFile();
    public abstract boolean isDirectory();
    public abstract boolean mkdir();
    public abstract boolean rename(final String newName);

    public abstract OutputStream getOutputStream();
    public abstract InputStream getInputStream();

    protected String getTechnicalSource() {
        return this.getClass().getName();
    }

    protected boolean hasSameTechnicalSource(final FileDescriptor fileDescriptor) {
        return this.getTechnicalSource().equals(fileDescriptor.getAdapter().getTechnicalSource());
    }

    protected void copyTo(FileDescriptor target) {
        this.copyTo(target.getOutputStream());
    }

    protected void copyTo(OutputStream outputStream) {
        Traper.trap(() -> FileCopyUtils.copy(this.getInputStream(), outputStream));
    }

    protected boolean moveByStreamCopy(FileDescriptor target) {
        this.copyTo(target);
        return this.simpleDelete();
    }

    public boolean delete() {
        if (this.isDirectory()) {
            return this.listAllFileAdapters()
                    .map(AbstractFileDescriptorAdapter::delete)
                    .allMatch(Boolean.TRUE::equals)
                    && this.simpleDelete();
        } else {
            return this.simpleDelete();
        }
    }

    public boolean moveTo(final FileDescriptor target) {
        if (this.hasSameTechnicalSource(target)) {
            return this.moveByRename(target);
        } else {
            return this.moveByStreamCopy(target);
        }
    }



    public String getId() {
        return Base64Utils.encodeToString(this.getFullName().getBytes());
    }

    public Stream<FileDescriptor> listAllFileDescriptors() {
        return this.listAllFileAdapters().map(FileDescriptor::new);
    }



}
