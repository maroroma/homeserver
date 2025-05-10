package maroroma.homemusicplayer.config;

import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.caches.InputStreamCache;
import maroroma.homemusicplayer.services.caches.LocalFileSystemInputStreamCache;
import maroroma.homemusicplayer.services.caches.MemoryInputStreamCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InputStreamCacheConfig {

    @Bean
    InputStreamCache localFileSystemInputStreamCache(
            FilesFactory filesFactory,
            @Value("${musicplayer.caches.localcache.enable}") boolean localCacheEnabled,
            @Value("${musicplayer.caches.inpustream.tease}") int teaseSize,
            @Value("${musicplayer.caches.inpustream.max-size}") int memoryCacheMaxSize,
            @Value("${musicplayer.caches.localcache.max-size}") int localCacheMaxSize,
            @Value("${musicplayer.caches.localcache.directory}") String localCachePath) {

        var memoryCache = MemoryInputStreamCache.builder()
                .cacheMaxSize(memoryCacheMaxSize)
                .filesFactory(filesFactory)
                .teaseSize(teaseSize)
                .build();

        if (localCacheEnabled) {
            log.info("Activation du LocalFileSystemInputStreamCache");
            return LocalFileSystemInputStreamCache.builder()
                    .memoryInputStreamCache(memoryCache)
                    .filesFactory(filesFactory)
                    .teaseSize(teaseSize)
                    .cacheMaxSize(localCacheMaxSize)
                    .localFileSystemCachePath(localCachePath)
                    .build();
        }

        log.info("Activation du MemoryInputStreamCache");
        return memoryCache;


    }

}
