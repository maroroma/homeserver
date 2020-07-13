package maroroma.homeserverng.iot.model;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

@Data
@Builder
public class TriggeringDescription {
    private String target;
    private Integer delay;

    public static TriggeringDescription fromSerializedProperty(String raw) {
        String[] rawList = raw.split("=");
        return TriggeringDescription.builder()
                .target(rawList[0])
                .delay(Integer.parseInt(rawList[1]))
                .build();
    }

    public String serializeAsString() {
        return String.join("=", Arrays.asList(this.target, this.delay.toString()));
    }
}
