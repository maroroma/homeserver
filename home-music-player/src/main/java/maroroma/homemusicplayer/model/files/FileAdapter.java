package maroroma.homemusicplayer.model.files;

import maroroma.homemusicplayer.tools.FileUtils;
import maroroma.homemusicplayer.tools.StreamUtils;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public interface FileAdapter {
    boolean exists();

    String getResolvedPath();

    String getFileName();

    boolean createFile();

    boolean isLowPerformanceFile();

    long createTimeAsMillis();

    boolean delete();

    boolean isDirectory();

    List<FileAdapter> getFiles();

    long size();

    default Stream<FileAdapter> streamFiles() {
        return StreamUtils.of(this.getFiles());
    }

    default String pathAsBase64() {
        return FileUtils.convertPathToBase64(this.getResolvedPath());
    }

    OutputStream getOutputStream();

    InputStream getInputStream();

    void mkdirs();

    default ByteArrayInputStream getInMemoryInputStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.copyTo(byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    FileAdapter combine(String pathFragment);

    boolean isSameFile(FileAdapter anotherFileAdapter);

    default void copyTo(FileAdapter target) {
        this.copyTo(target.getOutputStream());
    }

    default void copyTo(OutputStream outputStream) {
        Traper.trap(() -> FileCopyUtils.copy(this.getInputStream(), outputStream));
    }

    default FileAdapter copyFrom(InputStream inputStream) {
        Traper.trapToBoolean(() ->  FileCopyUtils.copy(inputStream, this.getOutputStream()));
        return this;
    }
    default String getExtension() {
        return FileUtils.getExtension(this).orElse(".mp3");
    }

    default String getFileNameWithoutExtension() {
        return this.getFileName().replace("." + this.getExtension(), "");
    }


}
