package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActivePlayersResponse {

    private List<GetActivePlayersResult> result;

//    {
//        "id": 1,
//            "jsonrpc": "2.0",
//            "result": [
//        {
//            "playerid": 0,
//                "type": "audio"
//        }
//    ]
//    }
}
