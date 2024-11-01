package maroroma.homeserverng.kiosk.services;

import maroroma.homeserverng.tools.needers.CacheNeed;
import maroroma.homeserverng.tools.needers.CacheNeeder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MeteoCacheNeeder implements CacheNeeder {

    public static final String WEATHER_CODES_CACHE_NAME = "WEATHER_CODES_CACHE";

    @Override
    public CacheNeed getCacheNeeded() {
        return CacheNeed.builder()
                .simpleCaches(List.of(WEATHER_CODES_CACHE_NAME))
                .fileCaches(List.of())
                .twoLevelFileCaches(List.of())
                .build();
    }
}
