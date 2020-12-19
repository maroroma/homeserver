package maroroma.homeserverng.administration.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.administration.model.HomeServerRunningStatus;
import maroroma.homeserverng.administration.model.HomeServerStatus;
import maroroma.homeserverng.network.services.NetworkServiceImpl;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.DriveUtils;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Implémentation du holder du status du server.
 *
 * @author rlevexie
 */
@Service
@Log4j2
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
     * pour l'accès au notifier sans se cogner des références circulaires
     */
    private final ApplicationContext applicationContext;

    /**
     * Pour les informations réseau.
     */
    private final NetworkServiceImpl networkService;

    public ServerStatusHolderImpl(@Value("${homeserver.version}") String homeServerVersion,
                                  ApplicationContext applicationContext,
                                  NetworkServiceImpl networkService) {
        this.homeServerVersion = homeServerVersion;
        this.applicationContext = applicationContext;
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
     * Permet de détecter une init du contexte spring et de passer
     * le status du server à {@link HomeServerRunningStatus}.RUNNING.
     */
    @PostConstruct
    protected void postInit() {
        log.info("init du contexte spring");
        this.setStatus(HomeServerRunningStatus.RUNNING);
        this.startupTime = LocalDateTime.now();

        // émission de notification
        this.applicationContext.getBean(NotifyerContainer.class)
                .notify(NotificationEvent.builder()
                .creationDate(new Date())
                .title("Démarrage du serveur")
                .message("Le serveur homeserver vient de démarrer")
                .build());
    }
}
