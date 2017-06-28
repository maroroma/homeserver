package maroroma.homeserverng.tools.jsonrpc;

import java.util.HashMap;
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
@Builder
public class KodiJsonRPCMethod {

	
	/**
	 * Représentation de la version 2 de json rpc.
	 */
	public static final String JSON_RPC_V_2_0 = "2.0";

	/**
	 * Version du jsonrpc utilisée.
	 */
	private String jsonrpc = JSON_RPC_V_2_0;
	
	/**
	 * Nom de la méthode.
	 */
	private String method;
	
	/**
	 * Liste des paramètres.
	 */
	private Map<String, Object> params = new HashMap<>();
	
	/**
	 * Id de l'appel.
	 */
	private int id;
	
}
