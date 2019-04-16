package maroroma.homeserverng.kodimanager.services;

import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.kodi.model.KodiResponse;
import maroroma.homeserverng.tools.kodi.model.KodiSource;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class KodiManagerServiceTest {

    HomeServerPropertyHolder urls;
    HomeServerPropertyHolder displayTime;
    KodiManagerService kodiManagerService;

    @Before
    public void setup() {
        urls = new HomeServerPropertyHolder();
        urls.setValue("http://192.168.0.51/jsonrpc,http://192.168.0.18:8080/jsonrpc");
        displayTime = new HomeServerPropertyHolder();
        displayTime.setValue("10000");

        kodiManagerService  = new KodiManagerService();
        kodiManagerService.kodiInstancesUrls = urls;
    }

    @Test
    public void pingInstances() {
        List<KodiResponse<Boolean>> statuses = kodiManagerService.pingInstances();
    }

    @Test
    public void getSources() {
        List<KodiResponse<List<KodiSource>>> wrappedKodiResponses = kodiManagerService.getKodiSources();
    }
}