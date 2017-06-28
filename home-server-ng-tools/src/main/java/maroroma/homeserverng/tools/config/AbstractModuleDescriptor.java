package maroroma.homeserverng.tools.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;

/**
 * Classe abstraite pour l'utilisation des modules descriptors.
 * Permet d'utiliser des fonctionnalités de controles sur l'activation.
 * Si elle est utilisé comme classe parente, elle permet de réaliser une injection utile du descriptor, à des fins de controle d'activation, etc.
 * <br /> Elle n'est fonctionnelle que si l'annotation {@link HomeServerModuleDescriptor} est présente.
 * @author rlevexie
 *
 */
public abstract class AbstractModuleDescriptor {

	/**
	 * Service utilisé pour controler les {@link HomeServerModuleHandler}.
	 */
	@Autowired
	private HomeServerModuleManager manager;
	
	
	/**
	 * Détermine si le module correspondant est activé.
	 * @return -
	 */
	public boolean isModuleEnabled() {
		Assert.isTrue(this.getClass().isAnnotationPresent(HomeServerModuleDescriptor.class),
				"Annotation HomeServerModuleDescriptor must be present");
		
		HomeServerModuleDescriptor descriptor = this.getClass().getAnnotation(HomeServerModuleDescriptor.class);
		
		return this.manager.isModuleEnabled(descriptor.moduleId());
	}
}
