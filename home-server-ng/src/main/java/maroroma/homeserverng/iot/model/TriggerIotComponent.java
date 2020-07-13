package maroroma.homeserverng.iot.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Iot de type interrupteur qui est censé déclencher des events.
 */
public class TriggerIotComponent extends AbstractIotComponent<TriggerIotComponent> {

    public static final String TRIGGERS_PROPERTY_NAME = "TRIGGERS";

    public TriggerIotComponent(IotComponentDescriptor componentDescriptor) {
        super(componentDescriptor, "no glyphicon");
    }

    public TriggerIotComponent triggers(TriggeringDescription triggeringDescription) {
        return withPropertyList(TRIGGERS_PROPERTY_NAME, triggeringDescription.serializeAsString());
    }

    public Optional<List<TriggeringDescription>> triggeringDescription() {
        return this.propertyList(TRIGGERS_PROPERTY_NAME)
                .map(list -> list.stream()
                        .map(TriggeringDescription::fromSerializedProperty).collect(Collectors.toList()));
    }
}
