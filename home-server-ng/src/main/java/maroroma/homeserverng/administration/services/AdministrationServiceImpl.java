package maroroma.homeserverng.administration.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.administration.model.FullConfigurationHolder;
import maroroma.homeserverng.administration.model.HomeServerRunningStatus;
import maroroma.homeserverng.administration.model.HomeServerStatus;
import maroroma.homeserverng.administration.model.UploadPropertiesResponse;
import maroroma.homeserverng.administration.tools.HomeServerModulesScanner;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.cache.CacheDescriptor;
import maroroma.homeserverng.tools.cache.CacheKeyDescriptor;
import maroroma.homeserverng.tools.config.HomeServerModuleActivationStatus;
import maroroma.homeserverng.tools.config.HomeServerModuleHandler;
import maroroma.homeserverng.tools.config.HomeServerPluginPropertiesManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.JsonHelper;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.repositories.NanoRepositoriesManager;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import maroroma.homeserverng.tools.repositories.NanoRepositoryDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implémentation du service d'administration.
 * @author rlevexie
 *
 */
@Service
@Slf4j
public class AdministrationServiceImpl implements AdministrationService {

	/**
	 * Service pour la gestion du status du server.
	 */
	@Autowired
	private ServerStatusHolderImpl statusHolder;

//	/**
//	 * Service pour la programmation du redémarrage du server.
//	 */
//	@Autowired
//	private RestarterService restarter;

	/**
	 * Helper pour la gestion des modules.
	 */
	@Autowired
	private HomeServerModulesScanner homeModuleScanner;
	
	/**
	 * Permet de gérer les propriétés des plugins.
	 */
	@Autowired
	private HomeServerPluginPropertiesManager propertiesManager;
	
	/**
	 * Gestionnaire de {@link NanoRepository}.
	 */
	@Autowired
	private NanoRepositoriesManager repoManager;
	
	/**
	 * Service spécialisé dans la manipulaton des caches.
	 */
	@Autowired
	private CacheService cacheService;

	

	/**
	 * Liste des plugins du server.
	 */
	private Map<String, HomeServerModuleHandler> pluginList = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HomeServerModuleHandler> getEnabledHomeServerModules() {
		List<HomeServerModuleHandler> returnValue = new ArrayList<>();
		
		this.pluginList.values().forEach(plugin -> {
			if (plugin.isEnabled()) {
				returnValue.add(plugin);
			}
		});
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HomeServerModuleHandler> getAllHomeServerModules() {
		
		List<HomeServerModuleHandler> returnValue = new ArrayList<>(this.pluginList.values());
	
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@PostConstruct
	public void initPluginList() throws HomeServerException {

		log.info("Initialisation de la liste des plugins");

		
		this.pluginList = this.homeModuleScanner.getAndUpdateModuleList();
		

	}

	/**
	 * PErmet d'arrêter le serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	private HomeServerStatus stopServer() throws HomeServerException {
		this.statusHolder.setStatus(HomeServerRunningStatus.STOPPING);
//		this.restarter.scheduleStop();
		return this.statusHolder.getStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HomeServerPropertyHolder> getProperties() throws HomeServerException {
		return this.propertiesManager.getProperties();
	}

	@Override
	public HomeServerPropertyHolder getProperty(String id) throws HomeServerException {
		return this.propertiesManager.getPropertyHolder(id);
	}

	/**
	 * {@inheritDoc}
	 * @return 
	 * @throws HomeServerException 
	 */
	@Override
	public HomeServerPropertyHolder updateProperty(final String id, final HomeServerPropertyHolder newValue) throws HomeServerException {
		this.propertiesManager.updateProperty(id, newValue);
		return this.propertiesManager.getPropertyHolder(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerModuleHandler updateModuleStatus(final String id, final boolean enabled) throws HomeServerException {
		validateModuleRequestForStatusUpdate(id);
		
		
		this.pluginList.get(id).setEnabled(enabled);
		this.homeModuleScanner.saveModulesConfig(this.pluginList);
		
		log.info("Passage du module [" + id + "] au status [" + enabled + "]");
		return this.pluginList.get(id);
	}

	/**
	 * Permet de valider que dans le cadre d'une modification, le plugin existe et n'est pas en lecture seule.
	 * @param id -
	 */
	private void validateModuleRequestForStatusUpdate(final String id) {
		Assert.isTrue(this.pluginList.containsKey(id), "Le module spécifié (" + id + ") est introuvable");
		Assert.isTrue(!this.pluginList.get(id).isReadOnly(), "Le module spécifié (" + id + ") n'est pas modifiable");
	}
	
	@Override
	public List<HomeServerModuleHandler> updateModuleStatus(final List<HomeServerModuleActivationStatus> statuses)
			throws HomeServerException {
		// validation en masse
		Assert.notEmpty(statuses, "La liste de plugins à mettre à jour ne peut être vide ou non null");
		for (HomeServerModuleActivationStatus homeServerModuleActivationStatus : statuses) {
			validateModuleRequestForStatusUpdate(homeServerModuleActivationStatus.getId());
		}
		
		// mise à jour pour chacun des items
		statuses.forEach(newStatus -> this.pluginList.get(newStatus.getId()).setEnabled(newStatus.isEnabled()));
		this.homeModuleScanner.saveModulesConfig(this.pluginList);
		
		return this.getAllHomeServerModules();
	}

	@Override
	public void reloadModules() throws HomeServerException {
		this.homeModuleScanner.clear();
		this.initPluginList();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerStatus stop() throws HomeServerException {
		return this.stopServer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerModuleHandler getHomeServerModule(final HomeServerModuleDescriptor descriptor)
			throws HomeServerException {
		return this.pluginList.get(descriptor.moduleId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UploadPropertiesResponse uploadProperties(final MultipartFile file) throws HomeServerException {
		UploadPropertiesResponse returnValue = new UploadPropertiesResponse();
		Assert.notNull(file, "file can't be null");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<HomeServerPropertyHolder> uploadedProperties = mapper.readValue(file.getBytes(),
					mapper.getTypeFactory().constructCollectionType(List.class, HomeServerPropertyHolder.class));
			
			
			uploadedProperties.forEach(propertyToUpdate -> {
				HomeServerPropertyHolder initialProperty = this.propertiesManager.getPropertyHolder(propertyToUpdate.getId());
				if (initialProperty == null) {
					returnValue.getNotFoundProperties().add(propertyToUpdate);
				} else if (initialProperty.isReadOnly()) {
					returnValue.getReadOnlyProperties().add(propertyToUpdate);
				} else {
					this.propertiesManager.updateProperty(propertyToUpdate.getId(), propertyToUpdate);
					returnValue.getUpdatedProperties().add(propertyToUpdate);
				}
			});
			
			
			
		} catch (IOException e) {
			String msg = "Problème lors de la désérialisation des properties";
			log.warn(msg, e);
			throw new HomeServerException(msg, e);
		}
		
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<NanoRepositoryDescriptor> getNanoRepositoriesDescriptor() throws HomeServerException {
		
		return this.repoManager.getRepositories().stream()
				.map(oneRepo -> oneRepo.generateDescriptor()).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] exportRepository(final String repoId) throws HomeServerException {
		Assert.hasLength(repoId, "repoId can't be null or empty");
		return this.repoManager.exportRepository(repoId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void importRepository(final String id, final MultipartFile file) throws HomeServerException {
		Assert.notNull(file, "file can't be null");
		
		this.repoManager.importRepository(id, file);
		
		
	}

	@Override
	public void clearRepository(String id) throws HomeServerException {
		this.repoManager.clearRepository(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModuleEnabled(final String moduleId) {
		Assert.hasLength(moduleId, "moduleId can't be null or empty");
		return this.pluginList.values().stream().anyMatch(moduleToTest -> {
			return moduleToTest.getModuleId().equals(moduleId) && moduleToTest.isEnabled();
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] exportAllConfiguration() throws HomeServerException {
		FullConfigurationHolder returnValue = new FullConfigurationHolder();
		returnValue.setModules(this.getAllHomeServerModules());
		returnValue.setProperties(this.getProperties());
		
		for (NanoRepository oneRepo : this.repoManager.getRepositories()) {
			returnValue.getBase64Repositories().add(
					Tuple.from(oneRepo.getId(), 
							Base64Utils.encodeToString(this.repoManager.exportRepository(oneRepo.getId()))));
		}
		
		return JsonHelper.serializeJsonToByteArray(returnValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CacheDescriptor> getCaches() throws HomeServerException {
		return this.cacheService.getCaches();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CacheKeyDescriptor> getCacheKeys(final String cacheName) throws HomeServerException {
		return this.cacheService.getCacheKeys(cacheName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CacheKeyDescriptor> clearCache(final String cacheName) throws HomeServerException {
		return this.cacheService.clearCache(cacheName);
	}




}
