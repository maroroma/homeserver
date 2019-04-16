package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.kodimanager.services.KodiManagerService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTargetDirectoryLoader implements TargetDirectoryLoader {

    @Autowired
    private KodiManagerService kodiManagerService;

    @Override
    public void executeScanOnKodiInstances() {
        this.kodiManagerService.scanAliases(this.getKodiAliases());
    }
}
