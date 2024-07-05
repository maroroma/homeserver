package maroroma.homemusicplayer.services.mappers.files;

import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.api.SimpleFile;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FileAdapterMapper {

    public SimpleFile map(FileAdapter fileAdapter) {
        return SimpleFile.builder()
                .name(fileAdapter.getFileName())
                .base64Path(fileAdapter.pathAsBase64())
                .build();
    }

    public List<SimpleFile> map(List<FileAdapter> fileAdapters) {
        return StreamUtils.of(fileAdapters).map(this::map).toList();
    }

}
