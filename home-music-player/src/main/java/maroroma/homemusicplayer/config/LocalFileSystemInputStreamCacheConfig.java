package maroroma.homemusicplayer.config;

import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.caches.LocalFileSystemInputStreamCache;
import maroroma.homemusicplayer.services.caches.MemoryInputStreamCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "musicplayer.caches.localcache.enable")
@Configuration
@Slf4j
public class LocalFileSystemInputStreamCacheConfig {

    @Bean
    LocalFileSystemInputStreamCache localFileSystemInputStreamCache(
            FilesFactory filesFactory,
            @Value("${musicplayer.caches.inpustream.tease}") int teaseSize,
            @Value("${musicplayer.caches.inpustream.max-size}")int memoryCacheMaxSize,
            @Value("${musicplayer.caches.localcache.max-size}")int localCacheMaxSize,
            @Value("${musicplayer.caches.localcache.directory}") String localCachePath) {
        log.info("Activation du LocalFileSystemInputStreamCache");
        var memoryCache = MemoryInputStreamCache.builder()
                .cacheMaxSize(memoryCacheMaxSize)
                .filesFactory(filesFactory)
                .teaseSize(teaseSize)
                .build();

        return LocalFileSystemInputStreamCache.builder()
                .memoryInputStreamCache(memoryCache)
                .filesFactory(filesFactory)
                .teaseSize(teaseSize)
                .cacheMaxSize(localCacheMaxSize)
                .localFileSystemCachePath(localCachePath)
                .build();
    }

}
