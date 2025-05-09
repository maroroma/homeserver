package maroroma.homemusicplayer.services.caches;

import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import maroroma.homemusicplayer.services.FilesFactory;

@SuperBuilder
@RequiredArgsConstructor
public abstract class AbstractInputStreamCache implements InputStreamCache {
    protected final FilesFactory filesFactory;
    protected final int teaseSize;
    protected final int cacheMaxSize;

}
