package maroroma.homeserverng.iot.model;

import java.util.function.Predicate;

public final class IotComponentTypes {
    public final static String BUZZER = "BUZZER";
    public final static String TRIGGER = "TRIGGER";
    public final static String SIREN = "SIREN";

    public static Predicate<IotComponentDescriptor> isDescriptorOfType(String componentType) {
        return iotComponentDescriptor -> componentType.equals(iotComponentDescriptor.getComponentType());
    }

    public static Predicate<AbstractIotComponent<?>> isComponentOfType(String componentType) {
        return iotComponent -> isDescriptorOfType(componentType).test(iotComponent.getComponentDescriptor());
    }

    public static Predicate<AbstractIotComponent<?>> isComponentOfType(Predicate<IotComponentDescriptor> descriptorPredicate) {
        return iotComponent -> descriptorPredicate.test(iotComponent.getComponentDescriptor());
    }

    public static Predicate<IotComponentDescriptor> isDescriptorBuzzer() {
        return isDescriptorOfType(IotComponentTypes.BUZZER);
    }

    public static Predicate<AbstractIotComponent<?>> isComponentBuzzer() {
        return isComponentOfType(isDescriptorBuzzer());
    }

    public static Predicate<IotComponentDescriptor> isDescriptorSiren() {
        return isDescriptorOfType(IotComponentTypes.SIREN);
    }

    public static Predicate<AbstractIotComponent<?>> isComponentSiren() {
        return isComponentOfType(isDescriptorSiren());
    }

    public static Predicate<IotComponentDescriptor> isDescriptorTrigger() {
        return isDescriptorOfType(IotComponentTypes.TRIGGER);
    }

    public static Predicate<AbstractIotComponent<?>> isComponentTrigger() {
        return isComponentOfType(isDescriptorTrigger());
    }
}
