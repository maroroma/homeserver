package maroroma.homeserverng.iot.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Implémentation d'{@link AbstractIotComponent} pour un buzzer
 */
public class BuzzerIotComponent extends AbstractIotComponent<BuzzerIotComponent> {

    public BuzzerIotComponent(IotComponentDescriptor componentDescriptor) {
        super(componentDescriptor, "glyphicon glyphicon-bullhorn");
    }

    public boolean buzzOn(String ledTemplate) {
        return basicGet(uriBuilder -> uriBuilder
                .pathSegment("buzzer", "on")
                .queryParam("ledTemplate", ledTemplate));
    }

    public boolean buzzOff() {
        return basicGet(uriBuilder -> uriBuilder
                .pathSegment("buzzer", "off"));
    }
}
