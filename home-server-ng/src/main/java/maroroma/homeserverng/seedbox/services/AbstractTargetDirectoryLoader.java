package maroroma.homeserverng.seedbox.services;

import lombok.AccessLevel;
import lombok.Getter;
import maroroma.homeserverng.kodimanager.services.KodiManagerService;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTargetDirectoryLoader implements TargetDirectoryLoader {

    @Autowired
    private KodiManagerService kodiManagerService;

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private SecurityManager securityManager;

    @Override
    public void executeScanOnKodiInstances() {
        this.kodiManagerService.scanAliases(this.getKodiAliases());
    }
}
