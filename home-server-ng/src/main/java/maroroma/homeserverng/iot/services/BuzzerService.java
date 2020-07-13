package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.BuzzRequest;
import maroroma.homeserverng.iot.model.BuzzerIotComponent;
import maroroma.homeserverng.iot.model.IotComponentTypes;
import maroroma.homeserverng.iot.model.MiniSprite;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BuzzerService extends AbstractIotDedicatedService<BuzzerIotComponent> {

    @Property("homeserver.iot.buzzer.buzztime")
    private HomeServerPropertyHolder buzzTime;

    /**
     * Repo pour la gestion des sprites
     */
    @InjectNanoRepository(
            file = @Property("homeserver.iot.sprites.store"),
            persistedType = MiniSprite.class,
            idField = "name")
    private NanoRepository iotSpritesRepo;

    public BuzzerService(IotComponentsFactory iotComponentsFactory) {
        super(BuzzerIotComponent.class, iotComponentsFactory, IotComponentTypes.BUZZER);
    }

    @Async
    public void buzz(BuzzRequest buzzRequest) {
        this.protectedCall(buzzRequest.getId(), buzzer -> {
            buzzer.buzzOn(convertSpriteToString(buzzRequest.getLedTemplate()));
            try {
                Thread.sleep(buzzTime.asInt());
            } catch (InterruptedException e) {

            }
            buzzer.buzzOff();
        });
    }

    /**
     * Convertit le tableau de pixel en liste d'entier séparer par des -, le tout au format string, que l'on
     * envoie au component
     * <br /> Le "-" vaut mieux que le ";", qui n'est pas gérer comme il faut coté iotcomponent
     * @param spriteId
     * @return
     */
    String convertSpriteToString(String spriteId) {
        MiniSprite toSerialize = this.iotSpritesRepo.findByIdMandatory(spriteId);
        return toSerialize.getLines().stream()
                .map(oneLine -> oneLine.stream()
                        .map(onePixel -> onePixel.isOn() ? "1" : "0")
                        .collect(Collectors.joining()))
                .map(oneStringWithBit -> Integer.parseInt(oneStringWithBit, 2))
                .map(Object::toString)
                .collect(Collectors.joining("-"));
    }
}
