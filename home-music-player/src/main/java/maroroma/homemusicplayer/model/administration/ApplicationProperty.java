package maroroma.homemusicplayer.model.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationProperty {
    private String propertyName;
    private String propertyValue;
}
