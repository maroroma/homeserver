package maroroma.homeserverng.iot.services;

public interface TriggerableService {
    boolean containsAndMatch(String id);
    void trigger(String componentToTrigger);
}
