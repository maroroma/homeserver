package maroroma.homeserverng.administration.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.administration.model.HomeServerRunningStatus;
import maroroma.homeserverng.administration.model.HomeServerStatus;
import maroroma.homeserverng.network.services.NetworkServiceImpl;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.DriveUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implémentation du holder du status du server.
 *
 * @author rlevexie
 */
@Service
@Slf4j
public class ServerStatusHolderImpl {

    /**
     * Status courant du server.
     */
    private HomeServerRunningStatus status = HomeServerRunningStatus.STARTING;

    /**
     * Heure de démarrage du serveur.
     */
    private LocalDateTime startupTime;

    /**
     * Version courante de l'application.
     */
    private final String homeServerVersion;

    /**
     * Liste des montages unix.
     */
    @Property("homeserver.administrations.drives")
    private HomeServerPropertyHolder unixDrives;

    /**
     * Pour les informations réseau.
     */
    private final NetworkServiceImpl networkService;

    public ServerStatusHolderImpl(@Value("${homeserver.version}") String homeServerVersion,
                                  NetworkServiceImpl networkService) {
        this.homeServerVersion = homeServerVersion;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    public HomeServerStatus getStatus() {

        return HomeServerStatus.builder()
                .startUpTime(this.startupTime)
                .ipAddress(this.networkService.getMyIPAddress())
                .hostName(this.networkService.getHostName())
                .operatingSystem(System.getProperty("os.name"))
                .drives(DriveUtils.getDrives(this.unixDrives.asFileList()))
                .version(this.homeServerVersion).build();
    }

    /**
     * {@inheritDoc}
     */
    public void setStatus(final HomeServerRunningStatus newstatus) {
        log.info("mise à jour du status du server : ["
                + this.status
                + "] vers ["
                + newstatus + "]"
        );
        this.status = newstatus;

    }

    /**
     * Mise à jour du status et de la date de démarrage suite au démarrage de l'application
     */
    public void applicationIsRunning() {
        this.setStatus(HomeServerRunningStatus.RUNNING);
        this.startupTime = LocalDateTime.now();
    }
}
