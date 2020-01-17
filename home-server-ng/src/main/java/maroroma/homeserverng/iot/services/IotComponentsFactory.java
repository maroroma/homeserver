package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.BuzzerIotComponent;
import maroroma.homeserverng.iot.model.IotComponentDescriptor;
import maroroma.homeserverng.iot.model.IotCompontentTypes;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.FluentMap;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Factory pour gérer la production des composant iot d'un point de vue serveur
 */
@Component
public class IotComponentsFactory {

    /**
     * liste de constructeurs pour les implémentations de {@link AbstractIotComponent}
     */
    private final Map<String, Function<IotComponentDescriptor, AbstractIotComponent<?>>> constructors;


    public IotComponentsFactory() {
        // pour l'instant on a que le buzzer
        this.constructors = FluentMap.<String, Function<IotComponentDescriptor, AbstractIotComponent<?>>>create()
                .add(IotCompontentTypes.BUZZER, BuzzerIotComponent::new);
    }

    /**
     * Génère l'instance adéquate d'{@link AbstractIotComponent} en fonction du descriptor en entrée
     * @param componentDescriptor descriptor permettant de produit le component
     * @return l'instance qui va bien ou lève une exception
     */
    public AbstractIotComponent<?> createIotComponent(IotComponentDescriptor componentDescriptor) {
        return Optional
                .ofNullable(this.constructors.get(componentDescriptor.getComponentType()))
                .map(oneConstructor -> oneConstructor.apply(componentDescriptor))
                .orElseThrow(() -> new RuntimeHomeServerException(
                        "can't generate iotComponent for type " + componentDescriptor.getComponentType()));
    }

    public <T extends AbstractIotComponent<T>> T createIotComponent(IotComponentDescriptor componentDescriptor, Class<T> componentClazz) {
        return componentClazz.cast(createIotComponent(componentDescriptor));
    }

    /**
     * Génère un descriptor si le type est connu
     * @param id
     * @param ipAddresss
     * @param componentType
     * @param name
     * @return
     */
    public Optional<IotComponentDescriptor> generateDescriptor(String id,
                                                               String ipAddresss,
                                                               String componentType,
                                                               String name) {
        Assert.hasLength(id, "id can't be null or empty");
        Assert.hasLength(ipAddresss, "ipAddresss can't be null or empty");
        Assert.hasLength(componentType, "componentType can't be null or empty");
        Assert.hasLength(name, "name can't be null or empty");

        return Optional.of(componentType)
                // détermine si le composant est générable
                .filter(this.constructors::containsKey)
                .map(oneComponentType -> IotComponentDescriptor.builder()
                        .id(id)
                        .ipAddress(ipAddresss)
                        .componentType(componentType)
                        .name(name)
                        .build()
                );

    }
}
