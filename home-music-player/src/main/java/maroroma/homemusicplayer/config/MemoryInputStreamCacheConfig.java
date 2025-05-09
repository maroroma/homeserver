package maroroma.homemusicplayer.config;

import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.caches.LocalFileSystemInputStreamCache;
import maroroma.homemusicplayer.services.caches.MemoryInputStreamCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@ConditionalOnMissingBean(LocalFileSystemInputStreamCache.class)
//@Configuration
@Slf4j
public class MemoryInputStreamCacheConfig {

    //@Bean
    MemoryInputStreamCache memoryInputStreamCache(
            FilesFactory filesFactory,
            @Value("${musicplayer.caches.inpustream.tease}") int teaseSize,
            @Value("${musicplayer.caches.inpustream.max-size}")int cacheMaxSize) {
        log.info("Activation du MemoryInputStreamCache");
        return MemoryInputStreamCache.builder()
                .cacheMaxSize(cacheMaxSize)
                .filesFactory(filesFactory)
                .teaseSize(teaseSize)
                .build();
    }

}
