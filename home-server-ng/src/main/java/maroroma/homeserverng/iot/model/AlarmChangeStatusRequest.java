package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Requete de modification du status de l'alarme (active ou non)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmChangeStatusRequest {
    private boolean enable;
    private String code;
}
