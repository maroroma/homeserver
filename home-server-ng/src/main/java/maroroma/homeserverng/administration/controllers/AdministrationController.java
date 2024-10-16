package maroroma.homeserverng.administration.controllers;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import maroroma.homeserverng.administration.AdministrationModuleDescriptor;
import maroroma.homeserverng.administration.model.AllLogEvents;
import maroroma.homeserverng.administration.model.HomeServerStatus;
import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.model.TaskCancelRequest;
import maroroma.homeserverng.administration.model.UploadPropertiesResponse;
import maroroma.homeserverng.administration.services.AdministrationService;
import maroroma.homeserverng.administration.services.ServerStatusHolderImpl;
import maroroma.homeserverng.administration.services.TasksManager;
import maroroma.homeserverng.notifyer.services.LogEventsNotifyer;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.cache.CacheDescriptor;
import maroroma.homeserverng.tools.cache.CacheKeyDescriptor;
import maroroma.homeserverng.tools.config.HomeServerModuleActivationStatus;
import maroroma.homeserverng.tools.config.HomeServerModuleHandler;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.repositories.NanoRepositoryDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * Controller pour la gestion de l'administration.
 * @author rlevexie
 *
 */
@HomeServerRestController(moduleDescriptor = AdministrationModuleDescriptor.class)
public class AdministrationController {

	/**
	 * Service sous jacent.
	 */
	@Autowired
	private AdministrationService serviceAdministration;

	@Autowired
	private LogEventsNotifyer logEventsNotifyer;
	
	/**
	 * Service pour la gestion du status du server.
	 */
	@Autowired
	private ServerStatusHolderImpl statusHolder;

	@Autowired
	private TasksManager tasksManager;

	@GetMapping("${homeserver.api.path:}/administration/tasks")
	public ResponseEntity<List<Task>> getCurrentTasks() {
		return ResponseEntity.ok(this.tasksManager.getCurrentTasks());
	}

	@PostMapping("${homeserver.api.path:}/administration/tasks")
	public ResponseEntity<Boolean> cancelTask(@RequestBody TaskCancelRequest cancelRequest) throws HomeServerException {
		return ResponseEntity.ok(this.tasksManager.cancelOneTask(cancelRequest));
	}

	/**
	 * Retourne les modules applicatifs activés.
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/administration/enabledmodules")
	public ResponseEntity<List<HomeServerModuleHandler>> getEnabledModules() {
		return ResponseEntity.ok(this.serviceAdministration.getEnabledHomeServerModules());
	}
	
	/**
	 * Retourne la version courante de l'application.
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/administration/version")
	public ResponseEntity<String> getVersion() {
		return ResponseEntity.ok(this.statusHolder.getStatus().getVersion());
	}
	
	/**
	 * Retourne les modules applicatifs.
	 * @return -
	 * @throws HomeServerException 
	 */
	@GetMapping("${homeserver.api.path:}/administration/modules")
	public ResponseEntity<List<HomeServerModuleHandler>> getModules() throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.getAllHomeServerModules());
	}
	
	/**
	 * Réinitialise la liste des modules.
	 * @return la nouvelle liste des modules.
	 * @throws HomeServerException 
	 */
	@DeleteMapping(value = "${homeserver.api.path:}/administration/modules")
	public ResponseEntity<List<HomeServerModuleHandler>> refreshModules() throws HomeServerException {
		this.serviceAdministration.reloadModules();
		return ResponseEntity.ok(this.serviceAdministration.getAllHomeServerModules());
	}

	/**
	 * Permet de mettre à la jour status d'activation d'un des modules.
	 * @param id id du module à mettre à jour.
	 * @param newVersion nouveau status.
	 * @return le module mise à jour
	 * @throws HomeServerException 
	 */
	@RequestMapping(value = "${homeserver.api.path:}/administration/module/{id}", method = {RequestMethod.PATCH})
	public ResponseEntity<HomeServerModuleHandler> updateModuleStatus(@PathVariable("id") final String id,
			@RequestBody final HomeServerModuleHandler newVersion) throws HomeServerException {
		return ResponseEntity.ok(
				this.serviceAdministration.updateModuleStatus(id, newVersion.isEnabled()));
	}
	
	
	/**
	 * Permet de mettre à la jour status d'activation de plusieurs modules.
	 * @param statuses nouveau status.
	 * @return le module mise à jour
	 * @throws HomeServerException 
	 */
	@RequestMapping(value = "${homeserver.api.path:}/administration/modules", method = {RequestMethod.PATCH})
	public ResponseEntity<List<HomeServerModuleHandler>> updateModuleStatuses(@RequestBody final List<HomeServerModuleActivationStatus> statuses)
			throws HomeServerException {
		return ResponseEntity.ok(
				this.serviceAdministration.updateModuleStatus(statuses));
	}
	
	/**
	 * Permet de lancer une demande d'arrêt au server.
	 * @return nouveau status.
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "${homeserver.api.path:}/administration/server/stop", method = { RequestMethod.PATCH })
	public ResponseEntity<HomeServerStatus> stop() 
			throws HomeServerException {
		return ResponseEntity.ok(
		this.serviceAdministration.stop());
	}
	
	/**
	 * Permet de récupérer une mini stat sur le status du serveur.
	 * @throws HomeServerException -
	 * @return -
	 */
	@GetMapping("${homeserver.api.path:}/administration/server/status")
	public ResponseEntity<HomeServerStatus> serverSTatus() throws HomeServerException {
		return ResponseEntity.ok(
		this.statusHolder.getStatus());
	}
	
	/**
	 * Listing de l'ensemble des properties du server.
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/administration/configs")
	public ResponseEntity<List<HomeServerPropertyHolder>> listProperties() throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.getProperties());
	}

	/**
	 * Retourne la propriété demandée.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	@GetMapping("${homeserver.api.path:}/administration/configs/{id:.+}")
	public ResponseEntity<HomeServerPropertyHolder> getProperty(@PathVariable("id") String id) throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.getProperty(id));
	}
	
	/**
	 * Retourne l'ensemble des propriétés pour un export.
	 * @return -
	 * @throws HomeServerException -
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/configs/export", method = {RequestMethod.GET}, produces = { MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<byte[]> exportProperties() throws HomeServerException, JsonGenerationException, JsonMappingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(baos, this.serviceAdministration.getProperties());
		return ResponseEntity.ok(baos.toByteArray());
	}
	
	/**
	 * Permet d'uploader un plugin sur le server et de l'installer.
	 * @param file fichier à uploader
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "${homeserver.api.path:}/administration/configs/import", method = { RequestMethod.POST })
	ResponseEntity<UploadPropertiesResponse> uploadProperties(@RequestBody final MultipartFile file) throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.uploadProperties(file));
	}
	
	/**
	 * Mise à jour d'une propriété du server.
	 * @param id identifiant de la propriété
	 * @param newValue propriété dans son nouvel état
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(value = "${homeserver.api.path:}/administration/config/{id:.+}", method = {RequestMethod.PATCH})
	public ResponseEntity<HomeServerPropertyHolder> updateProperty(@PathVariable("id") final String id,
			@RequestBody final HomeServerPropertyHolder newValue) throws HomeServerException {
		return ResponseEntity.ok(
		this.serviceAdministration.updateProperty(id, newValue));
	}
	
	/**
	 * Retourne l'ensemble des {@link NanoRepositoryDescriptor} du serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping("${homeserver.api.path:}/administration/repos")
	public ResponseEntity<List<NanoRepositoryDescriptor>> getNanoRepositoriesDescriptor() throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.getNanoRepositoriesDescriptor());
	}
	
	/**
	 * PErmet d'exporter le contenu d'un repository.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/repo/{id}/export", method = {RequestMethod.GET}, produces = { MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<byte[]> exportRepository(@PathVariable("id") final String id) throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.exportRepository(id));
	}
	
	/**
	 * PErmet d'importer un repository.
	 * @param id -
	 * @param file -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/repo/{id}/import", method = {RequestMethod.POST})
	public void importRepository(@PathVariable("id") final String id,
			@RequestBody final MultipartFile file) throws HomeServerException {
		this.serviceAdministration.importRepository(id, file);
	}

	@DeleteMapping(path = "${homeserver.api.path:}/administration/repo/{id}")
	public ResponseEntity<Boolean> deleteRepository(@PathVariable("id") final String id) throws HomeServerException {

		this.serviceAdministration.clearRepository(id);

		return ResponseEntity.ok(true);
	}

	/**
	 * Permet d'exporter l'ensemble des propriétés du serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/export/all", method = {RequestMethod.GET}, produces = { MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<byte[]> exportAllConfiguration() throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.exportAllConfiguration());
	}
	
	/**
	 * REtourne l'ensemble des caches.
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/caches")
	public ResponseEntity<List<CacheDescriptor>> getAllCaches() throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.getCaches());
	}
	
	/**
	 * REtourne les clefs d'un cache donné.
	 * @param cacheName -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/caches/{cacheName}/keys")
	public ResponseEntity<List<CacheKeyDescriptor>> getCacheKeys(final @PathVariable("cacheName") String cacheName) throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.getCacheKeys(cacheName));
	}
	
	/**
	 * Vide un cache donné.
	 * @param cacheName -
	 * @return -
	 * @throws HomeServerException -
	 */
	@RequestMapping(path = "${homeserver.api.path:}/administration/caches/{cacheName}/keys", method = {RequestMethod.DELETE})
	public ResponseEntity<List<CacheKeyDescriptor>> clearCache(final @PathVariable("cacheName") String cacheName) throws HomeServerException {
		return ResponseEntity.ok(this.serviceAdministration.clearCache(cacheName));
	}

	/**
	 * Retourne l'ensembles des logs de notifications portés par l'application
	 * @return -
	 */
	@GetMapping(path="${homeserver.api.path:}/administration/logEvents")
	public ResponseEntity<AllLogEvents> getAllLogEvents() {
        return ResponseEntity.ok(this.logEventsNotifyer.getAllLogEvents());
	}
	
	
}
