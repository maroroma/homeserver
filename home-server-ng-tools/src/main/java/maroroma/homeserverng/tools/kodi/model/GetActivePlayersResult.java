package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActivePlayersResult {
//    {
//            "playerid": 0,
//                "type": "audio"
//        }
        private int playerid;
        private String type;
}
