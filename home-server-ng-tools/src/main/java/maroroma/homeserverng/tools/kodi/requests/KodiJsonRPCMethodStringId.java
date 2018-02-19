package maroroma.homeserverng.tools.kodi.requests;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
public class KodiJsonRPCMethodStringId extends AbstractKodiJsonRPCMethod {

	private String id;

	@Builder
	public KodiJsonRPCMethodStringId(String jsonrpc, String method, Map<String, Object> params, String id) {
		super(jsonrpc, method, params);
		this.id = id;
	}
	
	
	
}
