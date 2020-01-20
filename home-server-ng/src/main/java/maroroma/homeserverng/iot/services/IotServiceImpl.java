package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.IotComponentDescriptor;
import maroroma.homeserverng.iot.model.MiniSprite;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de gestion des composants IOT
 */
@Service
public class IotServiceImpl {

    /**
     * Repository pour la gestion des IotComponents enregistrés.
     */
    @InjectNanoRepository(
            file = @Property("homeserver.iot.components.store"),
            persistedType = IotComponentDescriptor.class,
            idField = "id")
    private NanoRepository iotComponentsRepo;

    /**
     * Repo pour la gestion des sprites
     */
    @InjectNanoRepository(
            file = @Property("homeserver.iot.sprites.store"),
            persistedType = MiniSprite.class,
            idField = "name")
    private NanoRepository iotSpritesRepo;

    private final IotComponentsFactory iotComponentsFactory;

    public IotServiceImpl(IotComponentsFactory iotComponentsFactory) {
        this.iotComponentsFactory = iotComponentsFactory;
    }


    /**
     * Permet d'enregistrer un composant IOT auprès du serveur
     * Dans l'idéal, est appelé directement par le dit composant, qui s'inscrira tout seul lors de son démarrage
     * Dans l'absolu, ça laisse la possibilité de définir des composants manuellement via l'api ou l'ihm
     * @param id -
     * @param ipAddress -
     * @param componentType -
     * @param name -
     * @return true si l'inscription a réussi
     */
    public boolean registerComponent(String id, String ipAddress, String componentType, String name) {
        Optional<IotComponentDescriptor> iotComponentDescriptor = iotComponentsFactory
                .generateDescriptor(id, ipAddress, componentType, name);

        if (!iotComponentDescriptor.isPresent()) {
            return false;
        } else {
            // si le composant est déjà connu
            if (iotComponentsRepo.exists(id)) {
                IotComponentDescriptor existing = iotComponentsRepo.findByIdMandatory(id);
                    return Traper.trapToBoolean(()  -> iotComponentsRepo.update(iotComponentDescriptor.get()));
            }
            // si inconnu ou si le type a changé
            return Traper.trapToBoolean(() -> iotComponentsRepo.save(iotComponentDescriptor.get()));
        }
    }

    /**
     * Retourne la liste de l'ensemble des IotComponents connus du serveur
     * @return -
     */
    public List<AbstractIotComponent<?>> getAllIotComponents() {
        return this.iotComponentsRepo.<IotComponentDescriptor>getAll().stream()
                .map(this.iotComponentsFactory::createIotComponent)
                .parallel()
                .map(AbstractIotComponent::updateStatus)
                .collect(Collectors.toList());
    }

    /**
     * Retourne l'ensemble des sprites persistés dans l'application
     * @return -
     */
    public List<MiniSprite> getAllSprites() {
        return this.iotSpritesRepo.getAll();
    }

    /**
     * Création d'un nouveau sprite
     * @param miniSprite sprite à enregistrer
     * @return liste de sprites à jour
     * @throws HomeServerException -
     */
    public List<MiniSprite> createNewMiniSprite(MiniSprite miniSprite) throws HomeServerException {
        this.iotSpritesRepo.save(miniSprite);
        return this.getAllSprites();
    }

    /**
     * Mise à jour du sprite donné en entrée
     * @param miniSprite minisprite à mettre à jour
     * @return liste de sprite mis à jour
     * @throws HomeServerException -
     */
    public List<MiniSprite> updateMiniSprite(MiniSprite miniSprite) throws HomeServerException {
        return this.iotSpritesRepo.update(miniSprite);
    }

    /**
     * Suppression du sprite donné
     * @param id identifiant du sprite à supprimer
     * @return liste de sprites mise à jour
     * @throws HomeServerException -
     */
    public List<MiniSprite> deleteSprite(String id) throws HomeServerException {
        return this.iotSpritesRepo.delete(id);
    }

    /**
     * Permet de désincrire un composant de la base dédiée
     * @param id identifiant du composant à supprimer
     * @return true si l'opération est un succès
     */
    public boolean removeComponent(String id) {
        return Traper.trapToBoolean(() -> this.iotComponentsRepo.delete(id));
    }
}
