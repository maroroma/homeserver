package maroroma.homemusicplayer.model.files;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@Data
@RequiredArgsConstructor
@Builder
public class LocalFileAdapter implements FileAdapter {
    private final File localFile;

    public LocalFileAdapter(String path) {
        this(new File(path));
    }


    @Override
    public boolean exists() {
        return this.localFile.exists();
    }

    @Override
    public String getResolvedPath() {
        return this.localFile.getAbsolutePath();
    }

    @Override
    public String getFileName() {
        return this.localFile.getName();
    }

    @Override
    public boolean createFile() {
        try {
            return this.localFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return Traper.trapToBoolean(this.localFile::createNewFile);
    }

    @Override
    public boolean isLowPerformanceFile() {
        return false;
    }

    @Override
    public boolean delete() {
        return Traper.trapToBoolean(() -> Files.deleteIfExists(this.localFile.toPath()));
    }

    @Override
    public boolean isDirectory() {
        return this.localFile.isDirectory();
    }

    @Override
    public List<FileAdapter> getFiles() {
        Assert.isTrue(this.isDirectory(), "this file must be a directory to list files");
        return Stream.of(Objects.requireNonNull(this.localFile.listFiles()))
                .map(LocalFileAdapter::new)
                .collect(Collectors.toList());
    }

    @Override
    public long size() {
        try {
            // pour les répertoire, ça pète. autant éviter la gestion d'exception
            if(this.localFile.isFile()) {
                return Files.size(this.localFile.toPath());
            } else {
                return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return Traper.trap(() -> new FileOutputStream(this.localFile));
    }

    @Override
    public InputStream getInputStream() {
        return Traper.trap(() -> new FileInputStream(this.localFile));
    }

    @Override
    public FileAdapter combine(String pathFragment) {
        return new LocalFileAdapter(new File(this.localFile, pathFragment));
    }

    @Override
    public boolean isSameFile(FileAdapter anotherFileAdapter) {
        if (anotherFileAdapter instanceof LocalFileAdapter anotherFileAdapterAsLocalFileAdapter) {
            return this.localFile.toPath().equals(anotherFileAdapterAsLocalFileAdapter.localFile.toPath());
        }

        return false;
    }
}
