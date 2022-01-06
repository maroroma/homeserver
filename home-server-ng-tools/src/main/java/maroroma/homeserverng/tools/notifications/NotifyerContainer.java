package maroroma.homeserverng.tools.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Implémentation de {@link Notifyer} permettant d'aggréget un ensemble de {@link Notifyer}.
 *
 * @author RLEVEXIE
 */
@Component
@Slf4j
public class NotifyerContainer {


    /**
     * Liste de {@link Notifyer}.
     */
    private final List<Notifyer> notifiers;

    public NotifyerContainer(List<Notifyer> notifiers) {
        this.notifiers = notifiers;
    }

    /**
     * {@inheritDoc}
     * <br /> Dans cette implémentation, permet d'émettre la notification vers l'ensemble des
     * {@link Notifyer} trouvés dans le contexte.
     */
    @Async
    public void notify(final NotificationEvent notification) {

        // validation de la notification
        NotificationValidator.validate(notification);

        // pour chacun des notifiers, si présents
        if (!CollectionUtils.isEmpty(this.notifiers)) {
            notifiers.parallelStream().forEach(oneNotifyer -> {
                try {
                    oneNotifyer.notify(notification);
                } catch (Exception e) {
                    log.warn("Erreur rencontrée lors de l'émission d'une notification", e);
                }
            });
        }
    }

}
