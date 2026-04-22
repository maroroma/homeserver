package maroroma.homemusicplayer.services;

import maroroma.homemusicplayer.model.files.FileAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

@Service
public class UploadResourcesService {

    private final FilesFactory filesFactory;
    private final List<String> supportedThumbNames;
    private final List<String> supportedFanartNames;

    public UploadResourcesService(FilesFactory filesFactory,
                                  @Value("${musicplayer.localresources.images.thumbs.supported-names}") List<String> supportedThumbNames,
                                  @Value("${musicplayer.localresources.images.fanart.supported-names}") List<String> supportedFanartNames) {
        this.filesFactory = filesFactory;
        this.supportedThumbNames = supportedThumbNames;
        this.supportedFanartNames = supportedFanartNames;
    }


    public FileAdapter uploadThumb(FileAdapter targetDirectory, String imageAsBase64) {
        return uploadImageAsBase64(targetDirectory, this.supportedThumbNames, imageAsBase64);
    }

    public FileAdapter uploadFanart(FileAdapter targetDirectory, String imageAsBase64) {
        return uploadImageAsBase64(targetDirectory, this.supportedFanartNames, imageAsBase64);
    }


    private FileAdapter uploadImageAsBase64(FileAdapter targetDirectory,
                                           List<String> availableNames,
                                           String imageAsBase64) {
        var fileToCreate = targetDirectory
                .combine(imageToUploadName(availableNames));

        var data = Base64.getDecoder().decode(imageAsBase64.split(",")[1]);

        return fileToCreate.copyFrom(new ByteArrayInputStream(data));
    }

    private String imageToUploadName(List<String> supportedNames) {
        return supportedNames.get(0) + ".png";
    }
}
