package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UTilisé pour la sauvegarde des composants en base
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IotComponentDescriptor {
    private String id;
    private String componentType;
    private String ipAddress;
    private String name;
}
