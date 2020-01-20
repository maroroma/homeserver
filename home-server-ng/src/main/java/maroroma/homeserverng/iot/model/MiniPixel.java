package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modele pour la description d'un pixel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniPixel {
    private boolean on;
}
