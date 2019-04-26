package maroroma.homeserverng.tools.files;

import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.security.SecurityManager;

import java.io.*;
import java.nio.file.Files;
import java.util.stream.Stream;

public class LocalFileDescriptorAdapter extends AbstractFileDescriptorAdapter {

    private final File file;

    public LocalFileDescriptorAdapter(File file) {
        super(null);
        this.file = file;
    }
    public LocalFileDescriptorAdapter(String path, SecurityManager securityManager) {
        super(securityManager);
        this.file = new File(path);
    }


    @Override
    public String getName() {
        return this.file.getName();
    }

    @Override
    public String getFullName() {
        return this.file.getAbsolutePath();
    }

    @Override
    public String getParent() {
        return this.file.getParent();
    }

    @Override
    public long size() {
        try {
            // pour les répertoire, ça pète. autant éviter la gestion d'exception
            if(file.isFile()) {
                return Files.size(file.toPath());
            } else {
                return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    protected Stream<AbstractFileDescriptorAdapter> listAllFileAdapters() {
        return Stream.of(this.file.listFiles())
                .map(LocalFileDescriptorAdapter::new);
    }

    @Override
    protected boolean simpleDelete() {
        return this.file.delete();
    }

    @Override
    protected boolean moveByRename(FileDescriptor target) {
        File destination = new File(target.getFullName());
        return this.file.renameTo(destination);
    }

    @Override
    public boolean isFile() {
        return this.file.isFile();
    }

    @Override
    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    @Override
    public boolean mkdir() {
        return this.file.mkdir();
    }

    @Override
    public boolean rename(String newName) {
        File dest = new File(this.getParent(), newName);
        return this.file.renameTo(dest);
    }

    @Override
    public OutputStream getOutputStream() {
        return Traper.trap(() -> new FileOutputStream(this.file));
    }

    @Override
    public InputStream getInputStream() {
        return Traper.trap(() -> new FileInputStream(this.file));
    }

    @Override
    public boolean delete() {
        return this.file.delete();
    }


}
