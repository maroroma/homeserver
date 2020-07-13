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

@Service
public class TriggerService extends AbstractIotDedicatedService<TriggerIotComponent> {

    private final List<TriggerableService> subServices;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();


    public TriggerService(IotComponentsFactory iotComponentsFactory,
                          @Autowired(required = false) List<TriggerableService> subTriggerableServices,
                          ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        super(TriggerIotComponent.class, iotComponentsFactory, IotComponentTypes.TRIGGER);
        this.subServices = subTriggerableServices;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    public void triggers(String triggerId, TriggeringDescription triggeringDescription) throws HomeServerException {
        TriggerIotComponent triggerIotComponent = this.getComponentAs(triggerId).triggers(triggeringDescription);
        this.saveComponent(triggerIotComponent);
    }

    public void triggered(String triggerId) {
        TriggerIotComponent triggerIotComponent = this.getComponentAs(triggerId);
        triggerIotComponent.triggeringDescription().ifPresent(triggeringDescriptions -> triggeringDescriptions
                .forEach(oneDescription -> this.subServices
                        .stream()
                        .filter(oneSubService -> oneSubService.containsAndMatch(oneDescription.getTarget()))
                        .forEach(oneTriggerable -> oneTriggerable.trigger(oneDescription.getTarget()))));
    }
}
