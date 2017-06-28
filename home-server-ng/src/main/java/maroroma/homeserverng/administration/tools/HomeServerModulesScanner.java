package maroroma.homeserverng.administration.tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.config.HomeServerModuleActivationStatus;
import maroroma.homeserverng.tools.config.HomeServerModuleHandler;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.BeanDefinitionsHelper;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Classe utilitaire pour le chargement et la sauvegarde de la configuration des modules.
 * @author RLEVEXIE
 *
 */
@Component
@Log4j2
public class HomeServerModulesScanner {

	/**
	 * Nom du fichier de configuration des plugins.
	 */
	private static final String FILE_PLUGINS_JSON = "plugins.json";


	/**
	 * Répertoire de configuration principal.
	 */
	@Value("${homeserver.config.dir}")
	private String mainConfigDir;

	/**
	 * Permet d'effacer la configuration actuelle.
	 */
	public void clear() {
		// récupération de la liste de plugin déjà présente
		File pluginConfiguration = new File(this.mainConfigDir, FILE_PLUGINS_JSON);
		if (pluginConfiguration.exists()) {
			pluginConfiguration.delete();
		}
	}

	/**
	 * Retourne la liste des modules en mergeant les modules déjà configurés et les déclarations
	 * de modules par annotation.
	 * @return -
	 * @throws HomeServerException -
	 */
	public Map<String, HomeServerModuleHandler> getAndUpdateModuleList() throws HomeServerException {

		// récupération de la liste de plugin déjà présente dans le classpath
		Map<String, HomeServerModuleHandler> returnValue = this.scanModules();

		// récupération des endspoints associés aux modules
		scanEndpoints(returnValue);


		// chargement du status d'activation
		this.loadEnableStatuses(returnValue);

		return returnValue;

	}

	public void loadEnableStatuses(final Map<String, HomeServerModuleHandler> returnValue) throws HomeServerException {
		// récupération de la configuration d'activation des plugins
		File pluginConfiguration = new File(this.mainConfigDir, FILE_PLUGINS_JSON);

		if (pluginConfiguration.exists()) {
			log.debug("Le fichier de configuration [" + pluginConfiguration.getAbsolutePath() + "] existe bien");
			ObjectMapper deserializer = new ObjectMapper();
			try {

				// récupération de la liste
				List<HomeServerModuleActivationStatus> deserializedModules = deserializer.readValue(pluginConfiguration,
						deserializer.getTypeFactory().constructCollectionType(List.class, HomeServerModuleActivationStatus.class));

				log.info(deserializedModules.size() + " plugins déjà présents");
				
				deserializedModules.forEach(status -> {
					if (returnValue.containsKey(status.getId())) {
						returnValue.get(status.getId()).setEnabled(status.isEnabled());
					}
				});

				
			} catch (IOException e) {
				throw new HomeServerException("Erreur survenue lors du chargement de la liste d'état des plugins", e);
			}
		} else {
			log.warn("Aucun fichier de configuration [" + pluginConfiguration.getAbsolutePath() + "] trouvé");
		}
	}

	/**
	 * Permet de récupérer les endpoints associés aux différents modules.
	 * @param returnValue -
	 */
	private void scanEndpoints(final Map<String, HomeServerModuleHandler> returnValue) {
		// chargement des controllers rest
		List<BeanDefinition> beanDefinitionsForHomeServerPlugin = BeanDefinitionsHelper.findBeans(HomeServerRestController.class);

		for (BeanDefinition	bd : beanDefinitionsForHomeServerPlugin) {
			try {

				// récupération de la classe
				Class<?> restControllerClazz = Class.forName(bd.getBeanClassName());

				// récupération du module descriptor rattaché, pour la mise à jour des endpoints
				HomeServerModuleDescriptor moduleDescriptiotnAnnotation = BeanDefinitionsHelper.getModuleDescriptor(restControllerClazz);

				if (returnValue.containsKey(moduleDescriptiotnAnnotation.moduleId())) {

					final HomeServerModuleHandler moduleHandler = returnValue.get(moduleDescriptiotnAnnotation.moduleId());

					// nettoyage des endpoints pour forcer la mise à jour
					// et ne pas se retrouver avec des endpoints datés.
					moduleHandler.clearEndpoints();


					// pour chacune des méthodes, test si annotation RequestMapping présente sur méthode
					// et récupération des informations
					ReflectionUtils.doWithMethods(restControllerClazz, new MethodCallback() {
						@Override
						public void doWith(final Method methodToTest) throws IllegalArgumentException, IllegalAccessException {
							if (methodToTest.isAnnotationPresent(RequestMapping.class)) {
								RequestMapping requestDescription = methodToTest.getAnnotation(RequestMapping.class);
								moduleHandler.addEndpoint(requestDescription.value());
							}
						}
					});
				}

			} catch (ClassNotFoundException e) {
				log.warn("impossible de charger la définition du plugin java pour [" + bd.getBeanClassName() + "]");
			}

		}
	}

	/**
	 * Sauvegarde de la configuration des modules.
	 * @param returnValue -
	 * @throws HomeServerException -
	 */
	public void saveModulesConfig(final Map<String, HomeServerModuleHandler> returnValue)
			throws HomeServerException {
		// récupération de la liste de plugin déjà présente
		File pluginConfiguration = new File(this.mainConfigDir, FILE_PLUGINS_JSON);
		this.saveModulesConfig(pluginConfiguration, returnValue);
	}


	/**
	 * Sauvegarde de la configuration des modules.
	 * @param pluginConfiguration -
	 * @param returnValue -
	 * @throws HomeServerException -
	 */
	public void saveModulesConfig(final File pluginConfiguration, final Map<String, HomeServerModuleHandler> returnValue)
			throws HomeServerException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(pluginConfiguration, returnValue
					.values().stream()
					.map(handler -> new HomeServerModuleActivationStatus(handler.getModuleId(), handler.isEnabled()))
					.collect(Collectors.toList()));
		} catch (IOException e) {
			throw new HomeServerException("Exception rencontrée lors de la sauvegarde du fichier de configuration des plugins", e);
		}
	}


	/**
	 * Chargement des modules non gérés par la configuration.
	 * @return -
	 * @throws HomeServerException 
	 */
	private Map<String, HomeServerModuleHandler> scanModules() throws HomeServerException {

		Map<String, HomeServerModuleHandler> returnValue = new HashMap<>();

		List<BeanDefinition> beanDefinitionsForHomeServerPlugin = BeanDefinitionsHelper.findBeans(HomeServerModuleDescriptor.class);

		// scan et listing des classes du package maroroma portant l'annotation HomeServerPlugin
		for (BeanDefinition	bd : beanDefinitionsForHomeServerPlugin) {

			// récupération de l'annotation
			HomeServerModuleDescriptor moduleDescriptiotnAnnotation = BeanDefinitionsHelper.getAnnotation(bd, HomeServerModuleDescriptor.class);

			returnValue.put(moduleDescriptiotnAnnotation.moduleId(),
					new HomeServerModuleHandler(moduleDescriptiotnAnnotation));

		}

		return returnValue;
	}

}
