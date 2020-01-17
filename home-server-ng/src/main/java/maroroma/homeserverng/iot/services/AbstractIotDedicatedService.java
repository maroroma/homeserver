package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.IotComponentDescriptor;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.repositories.NanoRepository;

import java.util.function.Consumer;

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

    protected AbstractIotDedicatedService(Class<T> iotComponentClazz, IotComponentsFactory iotComponentsFactory) {
        this.iotComponentClazz = iotComponentClazz;
        this.iotComponentsFactory = iotComponentsFactory;
    }

    protected T getComponentAs(String id) {
        return this.iotComponentsRepo.<IotComponentDescriptor>findById(id)
                .map(oneCandidate -> this.iotComponentsFactory.<T>createIotComponent(oneCandidate, iotComponentClazz))
                .orElseThrow(() -> new RuntimeHomeServerException(""));
    }

    /**
     * Permet de ne lancer l'appel au composant que si son status est ok
     * @param id identifiant du composant
     * @param action action à réaliser sur le composant récupérer et actif
     */
    protected void protectedCall(String id, Consumer<T> action) {
        T requestedComponent = getComponentAs(id);
        if (requestedComponent.updateStatus().isAvailable()) {
            action.accept(requestedComponent);
        }
    }
}
