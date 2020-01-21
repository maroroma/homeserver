package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Permet de réaliser une association au long cours entre un identifiant de composant
 * et un nom qui lui a déjà été donné.
 * Permet d'éviter des renommages du moment que le composant se réenregistre
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferedName {
    private String alreadyGivenName;
    private String componentId;
}
