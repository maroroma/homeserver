package maroroma.homeserverng.tools.transmission.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Réponse transmission dont le contenu ne nous intéresse pas.
 */
@Data
@JsonIgnoreProperties(value = {"arguments"})
public class MeaningLessResponse extends BasicResponse {
}
