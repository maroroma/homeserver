package maroroma.homeserverng.network.services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
import maroroma.homeserverng.tools.helpers.multicast.MulticastAgregator;
import maroroma.homeserverng.tools.helpers.multicast.MulticastHLP;
import maroroma.homeserverng.tools.helpers.multicast.MulticastResult;
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
		
		List<ServerDescriptor> returnValue = new ArrayList<>();

		MulticastHLP multiCaster = MulticastHLP.create();

		
		// pour l'entiereté de l'intervale, construction de chaque ip possible
		for (int i = scanParam.getMinValue(); i < scanParam.getMaxValue(); i++) {
			String ipToTest = scanParam.getIpFragment() + i;
			
			//on délègue au multicaster les différents appels.
			multiCaster.prepareFunction(ipToTest, new Callable<ServerDescriptor>() {

				@Override
				public ServerDescriptor call() throws Exception {
					try {
						if (InetAddress.getByName(ipToTest).isReachable(scanParam.getTimeout())) {
							return ServerDescriptor.builder().ip(ipToTest).build();
						} else {
							log.info(ipToTest + " unreachable");
						}
					} catch (IOException e) {
						log.debug(ipToTest + " n'est pas atteignable");
					} 
					throw new HomeServerException("Serveur" + ipToTest + "injoignable");
				}
			});
		}
		
		try {
			multiCaster.emit(new MulticastAgregator() {
				
				@Override
				public void accept(final MulticastResult result) {
					log.info("Traitement de " + result.getTotalCount() 
						+ " tests terminé (" + result.getSuccessCount() 
						+ "/" + result.getTotalCount() + " en succès)");
					returnValue.addAll(result.getResults());
				}
			}).await(scanParam.getMaxAwaitTime());
		} catch (InterruptedException e) {
			throw new HomeServerException("Erreur rencontrée lors du scan du réseau", e);
		}

		return returnValue;

	}

}
