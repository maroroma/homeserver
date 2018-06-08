package maroroma.homeserverng.network.controllers;

import maroroma.homeserverng.network.NetworkModuleDescriptor;
import maroroma.homeserverng.network.model.ServerDescriptor;
import maroroma.homeserverng.network.services.NetworkServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Controler rest pour la gestion des serveurs du réseau local.
 * @author RLEVEXIE
 *
 */
@HomeServerRestController(moduleDescriptor = NetworkModuleDescriptor.class)
public class NetworkController {

	/**
	 * Repo pour l'accès.
	 */
	@Autowired
	private NetworkServiceImpl networkRepository;
	
	
	/**
	 * retourne la liste des serveurs "scannés" du réseau local.
	 * @return liste des {@link ServerDescriptor}
	 * @throws HomeServerException -
	 */
	@RequestMapping("/network/availableservers")
	public ResponseEntity<List<ServerDescriptor>> getAvailablesServers() throws HomeServerException {
		return ResponseEntity.ok(this.networkRepository.getAvailableServer());
	}

	/**
	 * retourne la liste des serveurs "déclarés" du réseau local.
	 * @return liste des {@link ServerDescriptor}
	 * @throws HomeServerException -
	 */
	@RequestMapping("/network/servers")
	public ResponseEntity<List<ServerDescriptor>> getServers() throws HomeServerException {
		return ResponseEntity.ok(this.networkRepository.getServerDescriptors());
	}
	
	/**
	 * Enregistre un nouveau serveur descriptor.
	 * @param server -
	 * @return liste des {@link ServerDescriptor}
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/network/server", method = {RequestMethod.POST })
	public ResponseEntity<List<ServerDescriptor>> saveServer(@RequestBody final ServerDescriptor server) throws HomeServerException {
		return ResponseEntity.ok(this.networkRepository.saveServerDescriptor(server));
	}
	
	/**
	 * Mise à jour d'un server.
	 * @param id -
	 * @param newServer -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/network/server/{id}", method = {RequestMethod.PATCH })
	public ResponseEntity<List<ServerDescriptor>> updateServer(@PathVariable("id") final String id,
			@RequestBody final ServerDescriptor newServer) throws HomeServerException {
		return ResponseEntity.ok(this.networkRepository.updateServer(newServer));
	}
	
	/**
	 * Supprime un serveur descriptor.
	 * @param id -
	 * @return liste des {@link ServerDescriptor}
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/network/server/{id}", method = {RequestMethod.DELETE })
	public ResponseEntity<List<ServerDescriptor>> deleteServer(@PathVariable("id") final String id) throws HomeServerException {
		
		return ResponseEntity.ok(this.networkRepository.deleteServerDescriptor(id));
	}
	
	/**
	 * Détermine si le serveur est joignable.
	 * @param id -
	 * @return true si accessible.
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "/network/server/{id}/status")
	public ResponseEntity<Boolean> getServerStatus(@PathVariable("id") final String id) throws HomeServerException {
		return ResponseEntity.ok(this.networkRepository.getServerStatus(id));
	}
	
	
}
