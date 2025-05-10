package maroroma.homemusicplayer.services.caches;

import maroroma.homemusicplayer.model.library.entities.TrackEntity;

import java.util.*;
import java.util.concurrent.*;

/**
 * PErmet de gérer une collection de locks, pour faire du lock paramétrésiable
 */
public class ParameterizedLock {
    private final Map<String, Object> innerLockSupport = new ConcurrentHashMap<>();


    public Object getLock(TrackEntity trackEntity) {
        return this.innerLockSupport.computeIfAbsent(trackEntity.getLibraryItemPath(), k -> new Object());
    }

    public void clear() {
        this.innerLockSupport.clear();
    }
}
