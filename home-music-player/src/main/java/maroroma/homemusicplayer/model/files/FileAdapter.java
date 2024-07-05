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

    default InputStream getInMemoryInputStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.copyTo(byteArrayOutputStream);
        System.out.println("file copyed to memory : " + byteArrayOutputStream.size());
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



}
