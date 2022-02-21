package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.notifyer.services.CommonNotificatonTypes;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Centralisation de la capture du cycle de vie spring (démarrage et arrêt)
 */
@Service
public class SpringApplicationEventsListeners {

    /**
     * Pour l'émission de notifications
     */
    private final NotifyerContainer notifyerContainer;

    /**
     * Pour la gestion persistée du status de l'application
     */
    private final ServerStatusHolderImpl serverStatusHolder;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    public SpringApplicationEventsListeners(NotifyerContainer notifyerContainer,
                                            ServerStatusHolderImpl serverStatusHolder) {
        this.notifyerContainer = notifyerContainer;
        this.serverStatusHolder = serverStatusHolder;
    }

    @EventListener
    public void handlerApplicationStart(ContextRefreshedEvent startEvent) {

        serverStatusHolder.applicationIsRunning();

        Date startupDate = new Date(startEvent.getTimestamp());

        notifyerContainer.notify(NotificationEvent.builder()
                .creationDate(startupDate)
                .eventType(CommonNotificatonTypes.STARTUP)
                .title("Démarrage du serveur")
                .message("Le serveur vient de démarrer à la date suivante : " + simpleDateFormat.format(startupDate))
                .build());
    }
}
