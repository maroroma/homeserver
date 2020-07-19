package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.IotComponentDescriptor;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.repositories.NanoRepository;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractIotDedicatedService<T extends AbstractIotComponent<T>> {

    /**
     * Repository pour la gestion des IotComponents enregistrés.
     */
    @InjectNanoRepository(
            file = @Property("homeserver.iot.components.store"),
            persistedType = IotComponentDescriptor.class,
            idField = "id")
    private NanoRepository iotComponentsRepo;

    private final Class<T> iotComponentClazz;
    private final IotComponentsFactory iotComponentsFactory;
    private final String componentType;

    protected AbstractIotDedicatedService(Class<T> iotComponentClazz, IotComponentsFactory iotComponentsFactory, String componentType) {
        this.iotComponentClazz = iotComponentClazz;
        this.iotComponentsFactory = iotComponentsFactory;
        this.componentType = componentType;
    }

    protected T getComponentAs(String id) {
        return this.iotComponentsRepo.<IotComponentDescriptor>findById(id)
                .filter(this::typeMatch)
                .map(oneCandidate -> this.iotComponentsFactory.<T>createIotComponent(oneCandidate, iotComponentClazz))
                .orElseThrow(() -> new RuntimeHomeServerException(""));
    }

    public List<T> getAllComponents() {
        return this.iotComponentsRepo.<IotComponentDescriptor>getAll()
                .stream()
                .filter(this::typeMatch)
                .map(oneCandidate -> this.iotComponentsFactory.<T>createIotComponent(oneCandidate, iotComponentClazz))
                .collect(Collectors.toList());

    }


    protected boolean containsAndMatch(String id) {
        return this.iotComponentsRepo.<IotComponentDescriptor>findById(id)
                .filter(this::typeMatch)
                .map(finded -> true)
                .orElse(false);
    }

    private boolean typeMatch(IotComponentDescriptor iotComponentDescriptor) {
        return  componentType.equals(iotComponentDescriptor.getComponentType());
    }

    protected void saveComponent(AbstractIotComponent<T> toBeSaved) throws HomeServerException {
        this.iotComponentsRepo.save(toBeSaved.getComponentDescriptor());
    }

    /**
     * Permet de ne lancer l'appel au composant que si son status est ok
     *
     * @param id     identifiant du composant
     * @param action action à réaliser sur le composant récupérer et actif
     */
    protected void protectedCall(String id, Consumer<T> action) {
        T requestedComponent = getComponentAs(id);
        if (requestedComponent.updateStatus().isAvailable()) {
            action.accept(requestedComponent);
        }
    }
}
