package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.*;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * Composant dédié à la gestion de l'alarme
 * <br />
 * <ul>
 *     <li>gère un status d'alarme activée ou non</li>
 *     <li>gère l'émission d'un mail si déclenché</li>
 *     <li>gère un code de désactivation</li>
 * </ul>
 */
@Component
public class AlarmManager extends AbstractIotDedicatedService<SirenIotComponent> {

    /**
     * L'alarme est-elle activée ?
     */
    private boolean armed = false;

    /**
     * Code de l'alarme
     */
    @Property("homeserver.iot.alarm.code")
    HomeServerPropertyHolder alarmeCode;

    @Property("homeserver.iot.alarm.siren.delay")
    HomeServerPropertyHolder sirenDelay;

    ScheduledFuture<?> scheduledSiren;

    private final SimpleDateFormat sdf;

    /**
     * Gestion de l'émission des notifications
     */
    private final NotifyerContainer notifyerContainer;

    private final ThreadPoolTaskScheduler iotTaskScheduler;

    public AlarmManager(NotifyerContainer notifyerContainer, ThreadPoolTaskScheduler iotTaskScheduler, IotComponentsFactory iotComponentsFactory) {
        super(SirenIotComponent.class, iotComponentsFactory, IotComponentTypes.SIREN);
        this.notifyerContainer = notifyerContainer;
        this.iotTaskScheduler = iotTaskScheduler;
        this.sdf = new SimpleDateFormat("kk:mm:ss dd/MM/yyyy");
    }

    /**
     * Indique si l'alarme est activée
     * @return -
     */
    public boolean isArmed() {
        return this.armed;
    }

    /**
     * Active l'alarme, puis émet les notifications
     * @return -
     */
    private boolean arm() {

        this.armed = true;


        Date activationTime = new Date();

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .creationDate(activationTime)
                .title("Alarme activée")
                .message(String.format("Alarme activée à %s", sdf.format(activationTime)))
                .build();

        this.notifyerContainer.notify(notificationEvent);

        return this.armed;
    }

    /**
     * Désactive l'alarme pour le code donné.
     * <br /> Si le code est ok, l'alarme est coupée
     * <br /> Dans tous les cas une notification est émise
     * @param code code à valider pour la désactivation de l'alarme
     * @return -
     */
    private boolean disable(String code) {

        Date desactivationTime = new Date();

        NotificationEvent.NotificationEventBuilder notificationEventBuilder = NotificationEvent.builder();

        if (this.alarmeCode.getResolvedValue().equals(code)) {

            // chgt de status en premier
            this.armed = false;


            // préparation notification
            notificationEventBuilder = notificationEventBuilder
                    .creationDate(desactivationTime)
                    .title("Alarme désactivée")
                    .message(String.format("Alarme désactivée à %s", sdf.format(desactivationTime)));

            // annulation de la sirène si elle était programmée
            unscheduleSirenAndShutSirens();

        } else {
            notificationEventBuilder = notificationEventBuilder
                    .creationDate(desactivationTime)
                    .title("Tentative de désactivation de l'alarme")
                    .message(String.format("Tentative infructueuse de désactivation de l'alarme à %s", sdf.format(desactivationTime)));
        }


        this.notifyerContainer.notify(notificationEventBuilder.build());

        return !this.armed;
    }



    public boolean changeStatus(AlarmChangeStatusRequest alarmChangeStatusRequest) {
        if (alarmChangeStatusRequest.isEnable()) {
            return this.arm();
        } else {
            return this.disable(alarmChangeStatusRequest.getCode());
        }
    }

    /**
     * déclenche l'alarme si elle est activée, pour un triggger donné
     * @param trigger trigger déclenché
     */
    public void triggered(TriggerIotComponent trigger) {
        if (this.armed) {

            // en premier lieu on notifie

            Date detectionTime = new Date();

            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .creationDate(detectionTime)
                    .title("Activité inhabituelle dans la maison")
                    .message(String.format("Une activité inhabituelle a été détectée à %s via le trigger %s", sdf.format(detectionTime), trigger.name()))
                    .build();

            this.notifyerContainer.notify(notificationEvent);

            // après on schedule selon le délai indiqué
            // une fois celui-ci écoulé, on déclenche l'ensemble des sirènes
            // du coup le scheduled peut être annulé au niveau désactivation de l'alarme
            this.scheduledSiren = this.iotTaskScheduler.schedule(this::makeSirensCry, Instant.now().plusMillis(this.sirenDelay.asInt()));
        }
    }

    private void makeSirensCry() {
        Date beepTime = new Date();

        Map<Boolean, List<BeepResult>> beepResults = this.getAllComponents()
                .parallelStream()
                .map(SirenIotComponent::beeeeeeep)
                .collect(Collectors.partitioningBy(BeepResult::isBeepResult));


        NotificationEvent.NotificationEventBuilder builder = NotificationEvent.builder()
                .creationDate(beepTime);


        if (!beepResults.get(Boolean.TRUE).isEmpty()) {
            builder.title("Sirène(s) ko")
                    .message(String.format("Les sirènes suivantes n'ont pu être déclenchées : [%s]", beepResults.get(Boolean.TRUE)
                            .stream()
                            .map(beepResult -> beepResult.getSiren().getName())
                            .collect(Collectors.joining())));
        } else {
            builder.title("Toutes les sirènes ont été déclenchées")
                    .message("Toutes les sirènes sont actuellement en train de sonner");
        }

        this.notifyerContainer.notify(builder.build());
    }

    private void unscheduleSirenAndShutSirens() {
        if (this.scheduledSiren != null) {
            this.scheduledSiren.cancel(true);
            this.scheduledSiren = null;

            this.getAllComponents()
                    .parallelStream()
                    .forEach(SirenIotComponent::unbeeeeeeep);
        }
    }

}
