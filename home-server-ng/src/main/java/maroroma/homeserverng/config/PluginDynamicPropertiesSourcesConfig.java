package maroroma.homeserverng.config;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.BeanDefinitionsHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean de configuration permettant de charger l'ensemble des fichiers de properties des modules.
 * @author RLEVEXIE
 *
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class PluginDynamicPropertiesSourcesConfig {


	/**
	 * Répertoire de configuration principal.
	 */
	@Value("${homeserver.config.dir}")
	private String mainConfigDir;

	/**
	 * Permet de récupérer l'ensemble des {@link HomeServerModuleDescriptor} trouvés dans l'application.
	 * @return -
	 * @throws HomeServerException -
	 */
	private List<HomeServerModuleDescriptor> getModuleDescriptors() throws HomeServerException {
		List<HomeServerModuleDescriptor> returnValue = new ArrayList<>();
		List<BeanDefinition> bds = BeanDefinitionsHelper.findBeans(HomeServerModuleDescriptor.class);

		for (BeanDefinition beanDefinition : bds) {
			returnValue.add(BeanDefinitionsHelper.getAnnotation(beanDefinition, HomeServerModuleDescriptor.class));
		}
		return returnValue;
	}

	/**
	 * Permet de charger les fichiers de properties associés au différents modules.
	 * <br /> La récupération est maintenant faite en fonction du descriptor des modules. Cela permet de s'assurer que chaque module
	 * a bien son fichier de configuration dès le démarrage de l'application.
	 * @return -
	 * @throws HomeServerException -
	 */
	@Bean
	public List<HomeServerPropertiesSource> pluginsPropertiesSources() throws HomeServerException {
		
		// retour de la fonction
		List<HomeServerPropertiesSource> returnValue = new ArrayList<>();


		// récupération du répertoire de configuration du serveur
		File mainConfigDirectory = new File(this.mainConfigDir);
		Assert.isValidDirectory(mainConfigDirectory);
		log.info("Répertoire de configuration : [" + this.mainConfigDir + "]");
		
		// récupération de la liste des plugins déclarés dans le code
		// on en prend que les modules pour lesquels on a un fichier de properties de décrit
		
		try {
		this.getModuleDescriptors().stream()
			// filtre sur les modules portant bien un fichier de properties
			// sinon on va charger de la merde (genre le répertoire de config...)
			.filter(descriptor -> {
				return descriptor.propertiesFile() != null 
						&& !descriptor.propertiesFile().isEmpty();
				}).forEach(homeServerModuleDescriptor -> {
			
			// fichier de configuration pour le module en cours.
			File specificConfigForModule = new File(mainConfigDirectory, homeServerModuleDescriptor.propertiesFile());
			Assert.isValidFile(specificConfigForModule);

			// création de la source correspondante
			HomeServerPropertiesSource source = new HomeServerPropertiesSource(specificConfigForModule.getAbsolutePath(),
					homeServerModuleDescriptor.moduleId());

			// chargement et gestion de l'errer à travers une runtime
			// pas propre, mais les streams obligent à faire comme cela
			try {
				source.loadPropertiesSource();
			} catch (HomeServerException e) {
				throw new RuntimeHomeServerException(e);
			}
			
			returnValue.add(source);
			log.info("chargement du fichier de propriété [" + specificConfigForModule.getAbsolutePath() + "] ok");
		});
		} catch (RuntimeHomeServerException e) {
			log.error("une erreur est survenue lors du chargement d'un fichier de propriété");
			throw e.getInnerHomeServerException();
		}

		
		
		
		return returnValue;
	}

	/**
	 * Création du post processor permettant d'injecter les {@link HomeServerPropertyHolder} dans le bean demandeurs.
	 * @return -
	 * @throws HomeServerException -
	 */
	@Bean
	public HomeServerPluginPropertiesPostProcessor homeServerPropertiesPostProcessor() throws HomeServerException {
		return new HomeServerPluginPropertiesPostProcessor(pluginsPropertiesSources());
	}

}
