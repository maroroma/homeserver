package maroroma.homeserverng.tools.kodi.requests;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Dto représentant un appel à une méthode JSonRPC.
 * <br />
 * Peut être sérialisé une fois construit pour l'émission vers le serveur.
 * @author RLEVEXIE
 *
 */
@Data
public class KodiJsonRPCMethodIntId extends AbstractKodiJsonRPCMethod {

	/**
	 * Id de l'appel.
	 */
	private int id;

	public KodiJsonRPCMethodIntId() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Builder
	public KodiJsonRPCMethodIntId(final String jsonrpc, final String method, final Map<String, Object> params, final int id) {
		super(jsonrpc, method, params);
		this.id = id;
		// TODO Auto-generated constructor stub
	}
	
	
}
