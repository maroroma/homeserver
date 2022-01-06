package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

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

    public SpringApplicationEventsListeners(NotifyerContainer notifyerContainer,
                                            ServerStatusHolderImpl serverStatusHolder) {
        this.notifyerContainer = notifyerContainer;
        this.serverStatusHolder = serverStatusHolder;
    }

    @EventListener
    public void handlerApplicationStart(ContextRefreshedEvent startEvent) {

        serverStatusHolder.applicationIsRunning();

        notifyerContainer.notify(NotificationEvent.builder()
                .creationDate(new Date())
                .title("Démarrage du serveur")
                .message("Le serveur homeserver vient de démarrer")
                .build());
    }
}
