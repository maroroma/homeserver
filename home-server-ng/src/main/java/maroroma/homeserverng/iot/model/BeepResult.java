package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeepResult {

    private IotComponentDescriptor siren;
    private boolean beepResult;


}
