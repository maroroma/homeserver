package maroroma.homeserverng.network.services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.network.model.ServerDescriptor;
import maroroma.homeserverng.network.model.UrlScanParameter;
import maroroma.homeserverng.network.tools.UrlScanParameterHLP;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.repositories.NanoRepository;

/**
 * Implémentation du {@link NetworkService}.
 * @author rlevexie
 *
 */
@Repository
@Log4j2
public class NetworkServiceImpl implements NetworkService {

	/**
	 * Repository json pour la liste des serveurs.
	 */
	@InjectNanoRepository(
			file = @Property("homeserver.network.servers.store"),
			persistedType = ServerDescriptor.class,
			idField = "nom")
	private NanoRepository repository;

	/**
	 * Helper pour la construction d'un {@link UrlScanParameter}.
	 */
	@Autowired
	private UrlScanParameterHLP urlScanHLP;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ServerDescriptor> getServerDescriptors() throws HomeServerException {

		return this.repository.getAll();
	}

	/**
	 * {@inheritDoc}
	 * @throws HomeServerException -
	 */
	@Override
	public List<ServerDescriptor> saveServerDescriptor(final ServerDescriptor serverDescriptor) throws HomeServerException {
		return this.repository.save(serverDescriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ServerDescriptor> deleteServerDescriptor(final String id) throws HomeServerException {
		return this.repository.delete(id);
	}

	@Override
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
	 * {@inheritDoc}.
	 */
	@Override
	public List<ServerDescriptor> updateServer(final ServerDescriptor newServer) throws HomeServerException {
		return this.repository.update(newServer);
	}

	

	@Override
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

}
