package maroroma.homeserverng.tools.helpers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Classe utilitaire pour le parsing des packages et certaines bases pour la reflexion.
 * @author RLEVEXIE
 *
 */
public abstract class BeanDefinitionsHelper {

	/**
	 * Retourne les beans definition pour l'annotation donnée.
	 * @param annotation -
	 * @return -
	 */
	public static List<BeanDefinition> findBeans(final Class<? extends Annotation> annotation) {
		ClassPathScanningCandidateComponentProvider test = new ClassPathScanningCandidateComponentProvider(false);
		test.addIncludeFilter(new AnnotationTypeFilter(annotation));

		return new ArrayList<>(test.findCandidateComponents("maroroma"));	
	}
	
	/**
	 * Retourne l'annotation demandée pour un {@link BeanDefinition}.
	 * @param bean -
	 * @param annotation -
	 * @param <T> type de l'annotation recherchée.
	 * @return -
	 * @throws HomeServerException -
	 */
	public static <T extends Annotation>  T getAnnotation(final BeanDefinition bean, final  Class<T> annotation) throws HomeServerException {
		// récupération de la classe
		Class<?> javaPluginClazz;
		try {
			javaPluginClazz = Class.forName(bean.getBeanClassName());
		} catch (ClassNotFoundException e) {
			throw new HomeServerException("classe non trouvée pour le recherche d'annotation", e);
		}

		// récupération de l'annotation
		return javaPluginClazz.getAnnotation(annotation);

	}
	
	/**
	 * Retourne le {@link HomeServerModuleDescriptor} du service rest en parametre.
	 * @param restController -
	 * @return -
	 */
	public static HomeServerModuleDescriptor getModuleDescriptor(final Class<?> restController) {
		HomeServerModuleDescriptor returnValue = null;
		
		HomeServerRestController restControllerAnnotaiton = restController.getAnnotation(HomeServerRestController.class);
		if (restControllerAnnotaiton != null
				&& restControllerAnnotaiton.moduleDescriptor().isAnnotationPresent(HomeServerModuleDescriptor.class)) {
			returnValue = restControllerAnnotaiton.moduleDescriptor().getAnnotation(HomeServerModuleDescriptor.class);
		}
		
		return returnValue;
	}
	
}
