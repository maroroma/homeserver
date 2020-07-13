package maroroma.homeserverng.iot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IotProperty {
    private String propertyName;
    private String propertyValue;
}
