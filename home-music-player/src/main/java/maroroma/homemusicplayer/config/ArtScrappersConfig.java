package maroroma.homemusicplayer.config;

import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.LibraryItemArtsScrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ArtScrappersConfig {

    @Value("${musicplayer.localresources.images.thumbs.supported-names}")
    private List<String> supportedThumbNames;
    @Value("${musicplayer.localresources.images.fanart.supported-names}")
    private List<String> supportedFanartNames;

    @Value("${musicplayer.localresources.images.supported-extensions}")
    private List<String> supportedImagesExtensions;

    @Value("${musicplayer.localresources.images.artist.thumbs}")
    private String thumbPath;

    @Value("${musicplayer.localresources.images.artist.fanarts}")
    private String fanartPath;

    public ArtScrappersConfig() {
    }

    @Bean
    public LibraryItemArtsScrapper libraryItemArtsScrapper(FilesFactory filesFactory) {
        return new LibraryItemArtsScrapper(filesFactory,
                supportedThumbNames,
                supportedFanartNames,
                supportedImagesExtensions,
                thumbPath,
                fanartPath);
    }
}
