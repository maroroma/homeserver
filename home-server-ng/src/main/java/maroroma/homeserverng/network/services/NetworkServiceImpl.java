package maroroma.homeserverng.network.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.network.model.ServerDescriptor;
import maroroma.homeserverng.network.model.UrlScanParameter;
import maroroma.homeserverng.network.tools.UrlScanParameterHLP;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation de gestion du réseau
 * @author rlevexie
 *
 */
@Repository
@Log4j2
public class NetworkServiceImpl {

	/**
	 * Repository json pour la liste des serveurs.
	 */
	@InjectNanoRepository(
			file = @Property("homeserver.network.servers.store"),
			persistedType = ServerDescriptor.class,
			idField = "nom")
	private NanoRepository repository;

	/**
	 * Adresse ip locale.
	 * Les implémentations de la jdk variant, ça évite de se taper de la boucle locale.
	 */
	@Property("homeserver.network.ip")
	private HomeServerPropertyHolder myIP;

	/**
	 * Helper pour la construction d'un {@link UrlScanParameter}.
	 */
	@Autowired
	private UrlScanParameterHLP urlScanHLP;

	/**
	 * Retourne la liste des serveurs déclarés.
	 * @return -
	 * @throws HomeServerException  -
	 */
	public List<ServerDescriptor> getServerDescriptors() throws HomeServerException {

		return this.repository.getAll();
	}

	/**
	 * Enregistre la description d'un nouveau serveur.
	 * @param serverDescriptor -
	 * @return -
	 * @throws HomeServerException -
	 */
	public List<ServerDescriptor> saveServerDescriptor(final ServerDescriptor serverDescriptor) throws HomeServerException {
		return this.repository.save(serverDescriptor);
	}

	/**
	 * Supprime un serveur.
	 * @param id -
	 * @return -
	 * @throws HomeServerException
	 */
	public List<ServerDescriptor> deleteServerDescriptor(final String id) throws HomeServerException {
		return this.repository.delete(id);
	}

	/**
	 * Retourne si le serveur est joignable.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	public boolean getServerStatus(final String id) throws HomeServerException {
		ServerDescriptor sd = this.repository.find(id);
		boolean returnValue = true;

		try {
			sd.test();
		} catch (ResourceAccessException rae) {
			returnValue = false;
			log.warn("Serveur inaccessible", rae);
		}
		return returnValue;
	}

	/**
	 * Permet de mettre à jour un serveur.
	 * @param newServer nouvelle valeur pour le serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	public List<ServerDescriptor> updateServer(final ServerDescriptor newServer) throws HomeServerException {
		return this.repository.update(newServer);
	}


	/**
	 * Permet de récupérer la liste des serveurs disponibles sur le réseau, pour faciliter
	 * l'ajout de serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	public List<ServerDescriptor> getAvailableServer() throws HomeServerException {

		UrlScanParameter scanParam = this.urlScanHLP.buildParameter();
		
		return scanParam.getAddressesToTest().parallelStream()
		.map(ipToTest -> {
			try {
				if (InetAddress.getByName(ipToTest).isReachable(scanParam.getTimeout())) {
					return ServerDescriptor.reachable(ipToTest);
				} else {
					log.info(ipToTest + " unreachable");
				}
			} catch (IOException e) {
				log.debug(ipToTest + " n'est pas atteignable");
			}
			return ServerDescriptor.unreachable();
		})
		.filter(oneServer -> oneServer.isReachable())
		.collect(Collectors.toList());

	}

	/**
	 * Retourne l'adresse IP du serveur, ou un truc bidon sinon
	 * @return -
	 */
	public String getMyIPAddress() {
		return this.myIP.getResolvedValue();
	}

	/**
	 * Retourne le hostname de la machine
	 * @return
	 */
	public String getHostName() {
		String hostName = "NC";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn("Impossible de déterminer le nom de l'host");
		}

		return hostName;
	}

}
