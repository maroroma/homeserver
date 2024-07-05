package maroroma.homemusicplayer.model.files;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.tools.Traper;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Implémentation de l'adapteur pour des fichiers smb
 */
@Data
@RequiredArgsConstructor
public class SmbFileAdapter implements FileAdapter {

    private final SmbFile smbFile;

    private final SambaUserSupplier sambaUserSupplier;


    SmbFileAdapter(SmbFileAdapter parent, SmbFile smbFile) {
        this.smbFile = smbFile;
        this.sambaUserSupplier = parent.sambaUserSupplier;
    }

    public SmbFileAdapter(String smbFile, SambaUserSupplier sambaUserSupplier) {
        this.sambaUserSupplier = sambaUserSupplier;
        this.smbFile = Traper.trap(() -> new SmbFile(smbFile, mapSmbUser()));
    }

    private NtlmPasswordAuthentication mapSmbUser() {
        return new NtlmPasswordAuthentication("",
                this.sambaUserSupplier.generateSambaUser().getLogin(),
                this.sambaUserSupplier.generateSambaUser().getPassword());
    }

    @Override
    public boolean exists() {
        return Traper.trapToBoolean(this.smbFile::exists);
    }

    @Override
    public String getResolvedPath() {
        return this.smbFile.getCanonicalPath();
    }

    @Override
    public String getFileName() {
        if (this.smbFile.getName().endsWith("/")) {
            return this.smbFile.getName().substring(0, this.smbFile.getName().length() - 1);
        } else {
            return this.smbFile.getName();
        }
    }

    @Override
    public boolean createFile() {
        return Traper.trapToBoolean(this.smbFile::createNewFile);
    }

    @Override
    public boolean isLowPerformanceFile() {
        return true;
    }

    @Override
    public boolean delete() {
        return Traper.trapToBoolean(this.smbFile::delete);

    }

    @Override
    public boolean isDirectory() {
        return Traper.trap(this.smbFile::isDirectory);
    }

    @Override
    public List<FileAdapter> getFiles() {
        return Traper.trap(() -> Stream
                .of(this.smbFile.listFiles())
                .map(aListedSambaFile -> new SmbFileAdapter(this, aListedSambaFile))
                .collect(Collectors.toList()));
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public OutputStream getOutputStream() {
        return Traper.trap(() -> new SmbFileOutputStream(this.smbFile));

    }

    @Override
    public InputStream getInputStream() {
//        return Traper.trap(() -> {
//            var baos = new ByteArrayOutputStream();
//            new SmbFileInputStream(this.smbFile).transferTo(baos);
//            return new ByteArrayInputStream(baos.toByteArray());
//        });

        // à restester, à priori ça peut régler les pb de latences
//        return Traper.trap(() -> new BufferedInputStream(new SmbFileInputStream(this.smbFile)));


        return Traper.trap(() -> new SmbFileInputStream(this.smbFile));
    }

    @Override
    public FileAdapter combine(String pathFragment) {
        return new SmbFileAdapter(this.getResolvedPath() + "/" + pathFragment, this.sambaUserSupplier);
    }

    @Override
    public boolean isSameFile(FileAdapter anotherFileAdapter) {

        if (anotherFileAdapter instanceof SmbFileAdapter anotherFileAdapterAsLocalFileAdapter) {
            return this.smbFile.equals(anotherFileAdapterAsLocalFileAdapter.smbFile);
        }

        return false;
    }


    public interface SambaUserSupplier {
        SmbUser generateSambaUser();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SmbUser {
        private String login;
        private String password;
    }
}
