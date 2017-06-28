package maroroma.homeserverng.administration.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import maroroma.homeserverng.administration.model.HomeServerRunningStatus;
import maroroma.homeserverng.administration.model.UploadPropertiesResponse;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.cache.CacheDescriptor;
import maroroma.homeserverng.tools.cache.CacheKeyDescriptor;
import maroroma.homeserverng.tools.config.HomeServerModuleActivationStatus;
import maroroma.homeserverng.tools.config.HomeServerModuleHandler;
import maroroma.homeserverng.tools.config.HomeServerModuleManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.repositories.NanoRepositoryDescriptor;

/**
 * Définition du service d'administration.
 * @author rlevexie
 *
 */
public interface AdministrationService extends HomeServerModuleManager {
	
	/**
	 * Permet d'initialiser au besoin le fichier de configuration des plugins.
	 * <br /> on en profite pour scanner les plugins disponibles dans l'application web statique.
	 * @throws HomeServerException 
	 */
	void initPluginList() throws HomeServerException;
	
	/**
	 * Permet de lister les properties des plugins.
	 * @return l'ensemble des propriétés des plugins.
	 * @throws HomeServerException -
	 */
	List<HomeServerPropertyHolder> getProperties() throws HomeServerException;

	/**
	 * Permet de mettre à jour une propriété d'identifiant donné.
	 * @param id identifiant de la propriété à mettre à jour
	 * @param newValue propriété  portant la nouvelle valeur.
	 * @return -
	 * @throws HomeServerException -
	 */
	HomeServerPropertyHolder updateProperty(String id, HomeServerPropertyHolder newValue) throws HomeServerException;

	/**
	 * Mise à jour à partir d'un fichier des propriétés du serveur.
	 * @param file fichier json
	 * @return -
	 * @throws HomeServerException -
	 */
	UploadPropertiesResponse uploadProperties(MultipartFile file) throws HomeServerException;
	
	/**
	 * Permet de mettre à jour le status d'activation d'un module donné.
	 * @param id identifiant du module
	 * @param enabled nouveau status.
	 * @return -
	 * @throws HomeServerException -
	 */
	HomeServerModuleHandler updateModuleStatus(String id, boolean enabled) throws HomeServerException;
	
	/**
	 * Permet de mettre à jour un ensemble de status pour différents plugins.
	 * @param statuses -
	 * @return -
	 * @throws HomeServerException -
	 */
	List<HomeServerModuleHandler> updateModuleStatus(List<HomeServerModuleActivationStatus> statuses) throws HomeServerException;

	/**
	 * Force le rechargement des modules applicatifs.
	 * @throws HomeServerException -
	 */
	void reloadModules() throws HomeServerException;

	/**
	 * Retourne le status courant du server.
	 * @return -
	 */
	HomeServerRunningStatus getServerStatus();

	/**
	 * Permet d'arrêter l'application.
	 * @return -
	 * @throws HomeServerException -
	 */
	HomeServerRunningStatus stop() throws HomeServerException;

	/**
	 * Retourne le module pour le descriptor donné.
	 * @param descriptor -
	 * @return -
	 * @throws HomeServerException -
	 */
	HomeServerModuleHandler getHomeServerModule(HomeServerModuleDescriptor descriptor) throws HomeServerException;
	
	/**
	 * Retourne la version courante de l'application.
	 * @return -
	 */
	String getHomeServerVersion();

	/**
	 * Retourne la liste des {@link NanoRepositoryDescriptor} du serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<NanoRepositoryDescriptor> getNanoRepositoriesDescriptor() throws HomeServerException;
	
	/**
	 * Permet de récupérer un repository pour le téléchargement.
	 * @param repoId -
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] exportRepository(String repoId) throws HomeServerException;

	/**
	 * Import d'un repository.
	 * @param id -
	 * @param file -
	 * @throws HomeServerException -
	 */
	void importRepository(String id, MultipartFile file) throws HomeServerException;

	/**
	 * Permet d'exporter l'ensemble des configuration serveur sous la forme d'un fichier.
	 * @return -
	 * @throws HomeServerException -
	 */
	byte[] exportAllConfiguration()throws HomeServerException;
	
	/**
	 * Retourne l'ensemble des caches portés par l'application.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<CacheDescriptor> getCaches() throws HomeServerException;
	
	/**
	 * Retourne l'ensemble des clefs pour un cache donné.
	 * @param cacheName -
	 * @return -
	 * @throws HomeServerException -
	 */
	List<CacheKeyDescriptor> getCacheKeys(String cacheName) throws HomeServerException;

	/**
	 * Vide le cache donné.
	 * @param cacheName -
	 * @return -
	 * @throws HomeServerException -
	 */
	List<CacheKeyDescriptor> clearCache(String cacheName) throws HomeServerException;
	
}
