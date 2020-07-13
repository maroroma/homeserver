package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.TriggeringDescription;

import java.util.concurrent.ScheduledFuture;

public interface TriggerableService {
    boolean containsAndMatch(String id);
    void trigger(String componentToTrigger);
}
