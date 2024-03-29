package maroroma.homeserverng.iot.services;


import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.IotComponentDescriptor;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javax.annotation.PostConstruct;


/**
 * Centralise et cache la gestion des status des components iot
 * Sinon le ping est trop lorsque sollicité directement par l'ihm
 */
@Service
@Slf4j
public class UpdateIotComponentStatusService {

    public static final String HOMESERVER_IOT_COMPONENTS_STATUS_REFRESH_FREQUENCY = "homeserver.iot.components.status.refresh.frequency";
    /**
     * Repository pour la gestion des IotComponents enregistrés.
     */
    @InjectNanoRepository(
            file = @Property("homeserver.iot.components.store"),
            persistedType = IotComponentDescriptor.class,
            idField = "id")
    private NanoRepository iotComponentsRepo;

    @Property(HOMESERVER_IOT_COMPONENTS_STATUS_REFRESH_FREQUENCY)
    private HomeServerPropertyHolder refreshIotStatusFrequency;


    private final ThreadPoolTaskScheduler iotTaskScheduler;

    private final IotComponentsFactory iotComponentsFactory;



    private ScheduledFuture<?> scheduledFuture;
    private Map<String, Boolean> componentStatusCache = new ConcurrentHashMap<>();

    public UpdateIotComponentStatusService(ThreadPoolTaskScheduler iotTaskScheduler, IotComponentsFactory iotComponentsFactory) {
        this.iotTaskScheduler = iotTaskScheduler;
        this.iotComponentsFactory = iotComponentsFactory;
    }

    @PostConstruct
    public void startScheduling() {
       this.scheduledFuture = iotTaskScheduler.scheduleAtFixedRate(this::scheduledUpdate, this.refreshIotStatusFrequency.asInt());
    }

    @PropertyRefreshHandlers(HOMESERVER_IOT_COMPONENTS_STATUS_REFRESH_FREQUENCY)
    public void updateScheduling() {
        this.scheduledFuture.cancel(true);

        // on reschedule avec la nouvelle valeur
        this.startScheduling();

    }

    protected void scheduledUpdate() {

        Map<String, Boolean> allComponentStatus = this.iotComponentsRepo.<IotComponentDescriptor>getAll().stream()
                .map(this.iotComponentsFactory::createIotComponent)
                .parallel()
                .map(AbstractIotComponent::updateStatus)
                .collect(Collectors.toMap(oneComponent -> oneComponent.getComponentDescriptor().getId(), AbstractIotComponent::isAvailable));


        this.componentStatusCache.clear();
        this.componentStatusCache.putAll(allComponentStatus);
    }

    public AbstractIotComponent<?> updateStatus(AbstractIotComponent<?> toBeUpdated) {
        toBeUpdated.setAvailable(this.componentStatusCache.getOrDefault(toBeUpdated.getComponentDescriptor().getId(), false));
        return toBeUpdated;
    }
}
