package maroroma.homeserverng.filemanager.services;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ExtensionFromUrlGenerator {

    private static final  String[] EXTENSIONS = {"gif","png","txt","jpg","jpeg", "bmp"};


    public Optional<String> generateExtensionFromUrl(String anUrl) {
        return Stream.of(EXTENSIONS)
                .filter(availableExtension -> anUrl.toLowerCase().contains(availableExtension.toLowerCase()))
                .findFirst();
    }
}
