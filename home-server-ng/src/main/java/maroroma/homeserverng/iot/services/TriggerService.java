package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.IotComponentTypes;
import maroroma.homeserverng.iot.model.TriggerIotComponent;
import maroroma.homeserverng.iot.model.TriggeringDescription;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Centralise la gestion des triggers
 * <br /> dans cette version, on ne gère que l'alarme en point d'entrée unique
 */
@Service
public class TriggerService extends AbstractIotDedicatedService<TriggerIotComponent> {

    private final List<TriggerableService> subServices;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final AlarmManager alarmManager;


    public TriggerService(IotComponentsFactory iotComponentsFactory,
                          @Autowired(required = false) List<TriggerableService> subTriggerableServices,
                          ThreadPoolTaskScheduler threadPoolTaskScheduler, AlarmManager alarmManager) {
        super(TriggerIotComponent.class, iotComponentsFactory, IotComponentTypes.TRIGGER);
        this.subServices = subTriggerableServices;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.alarmManager = alarmManager;
    }

    public void triggers(String triggerId, TriggeringDescription triggeringDescription) throws HomeServerException {
        TriggerIotComponent triggerIotComponent = this.getComponentAs(triggerId).triggers(triggeringDescription);
        this.saveComponent(triggerIotComponent);
    }

    /**
     * Sollicitation de l'alarme (pour l'instant) sur la notification d'un trigger
     * @param triggerId identifiant du trigger qui a été déclenché
     */
    public void triggered(String triggerId) {
        TriggerIotComponent triggerIotComponent = this.getComponentAs(triggerId);
        this.alarmManager.triggered(triggerIotComponent);
    }
}
