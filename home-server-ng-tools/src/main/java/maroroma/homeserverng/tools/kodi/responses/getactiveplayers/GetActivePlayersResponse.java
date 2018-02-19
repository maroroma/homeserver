package maroroma.homeserverng.tools.kodi.responses.getactiveplayers;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetActivePlayersResponse {
//	"id": 1,
//    "jsonrpc": "2.0",
//    "result": [
//        {
//            "playerid": 0,
//            "type": "audio"
//        }
//    ]
	
	private int id;
	private String jsonrpc;
	List<ActivePlayer> result;
}
