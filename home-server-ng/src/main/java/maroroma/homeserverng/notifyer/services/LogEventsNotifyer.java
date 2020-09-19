package maroroma.homeserverng.notifyer.services;

import maroroma.homeserverng.administration.model.AllLogEvents;
import maroroma.homeserverng.notifyer.model.PersistantNotification;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;

/**
 * Permet d'écrire dans un nano repo les notifications reçues
 */
@Service
public class LogEventsNotifyer implements Notifyer {

    public static final String HOMESERVER_ADMINISTRATION_LOGEVENTS_STORE = "homeserver.administration.logevents.store";

    @InjectNanoRepository(
            file = @Property(HOMESERVER_ADMINISTRATION_LOGEVENTS_STORE),
            persistedType = PersistantNotification.class)
    private NanoRepository logEventsRepo;


    @Override
    public void notify(NotificationEvent notification) throws HomeServerException {
        this.logEventsRepo.save(PersistantNotification.of(notification));
    }

    public AllLogEvents getAllLogEvents() {
        return AllLogEvents.builder()
                .repoId(LogEventsNotifyer.HOMESERVER_ADMINISTRATION_LOGEVENTS_STORE)
                .persistantNotifications(logEventsRepo.getAll())
                .build();
    }


}
