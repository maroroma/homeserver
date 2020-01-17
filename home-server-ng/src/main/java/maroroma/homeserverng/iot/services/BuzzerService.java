package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.iot.model.BuzzRequest;
import maroroma.homeserverng.iot.model.BuzzerIotComponent;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BuzzerService extends AbstractIotDedicatedService<BuzzerIotComponent> {

    @Property("homeserver.iot.buzzer.buzztime")
    private HomeServerPropertyHolder buzzTime;

    public BuzzerService(IotComponentsFactory iotComponentsFactory) {
        super(BuzzerIotComponent.class, iotComponentsFactory);
    }

    @Async
    public void buzz(BuzzRequest buzzRequest) {
        this.protectedCall(buzzRequest.getId(), buzzer -> {
            buzzer.buzzOn(buzzRequest.getLedTemplate());
            try {
                Thread.sleep(buzzTime.asInt());
            } catch (InterruptedException e) {

            }
            buzzer.buzzOff();
        });
    }
}
