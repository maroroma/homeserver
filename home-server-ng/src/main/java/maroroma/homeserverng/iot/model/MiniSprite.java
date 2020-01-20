package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * modele pour la description d'un sprite, avec l'état de ses pixels
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniSprite {
    private String name;
    private String description;
    private List<List<MiniPixel>> lines;
}
