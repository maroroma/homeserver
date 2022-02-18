package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPlayerPropertiesResponse {
    private GetPlayerPropertiesResult result;
}
