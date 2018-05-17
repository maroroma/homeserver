package maroroma.homeserverng.tools.kodi.requests;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public abstract class AbstractKodiJsonRPCMethod {
	/**
	 * Version du jsonrpc utilisée.
	 */
	private String jsonrpc;
	
	/**
	 * Nom de la méthode.
	 */
	private String method;
	
	/**
	 * Liste des paramètres.
	 */
	private Map<String, Object> params = new HashMap<>();
}
