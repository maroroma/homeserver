package maroroma.homeserverng.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.config.HomeServerPluginPropertiesManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.repositories.DefaultPreProcessor;
import maroroma.homeserverng.tools.repositories.NanoRepositoriesManager;
import maroroma.homeserverng.tools.repositories.NanoRepository;

/**
 * POst processor permettant de brancher automatiquement un {@link NanoRepository} sur un bean spring.
 * <br /> Il scanne pour cela tous les champs des beans spring portant l'annotation {@link InjectNanoRepository}.
 * @author rlevexie
 *
 */
@Component
public class NanoRepositoriesPostProcessor implements BeanPostProcessor {

	/**
	 * Permet de récupérer les properties pointées par les annotation {@link InjectNanoRepository}.
	 */
	@Autowired 
	private HomeServerPluginPropertiesManager propertiesManager;
	
	/**
	 * Permet de gérer l'ensemble des {@link NanoRepository}.
	 */
	@Autowired
	private NanoRepositoriesManager repoManager;
	
	/**
	 * Ne fait rien dans cette implémentation.
	 */
	@Override
	public Object postProcessAfterInitialization(final Object beanToPostProcess, final String arg1) throws BeansException {
		return beanToPostProcess;
	}

	/**
	 * Scan des propriétés pour la détection des champs annotés {@link InjectNanoRepository}.
	 * <br /> Injection des champs avec la bonne instance paramétrée à partir des données portées par l'annotation.
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object beanToPostProcess, final String arg1) throws BeansException {
		
		// récupération des champs qui nous intéressent à travers cette liste.
		final List<Field> fieldsToPopulate = new ArrayList<>();

		// récupération de la liste des propriétés annotées HomeServerProperty
		ReflectionUtils.doWithFields(beanToPostProcess.getClass(), new FieldCallback() {

			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				
				// si l'annotation InjectNanoRepository est présente, c'est que l'on a trouvé un candidat.
				if (field.isAnnotationPresent(InjectNanoRepository.class)) {
					fieldsToPopulate.add(field);
				}
			}
		});

		// sur l'ensemble des champs récupérés, création du repo correspondant et injection.
		for (Field field : fieldsToPopulate) {

			
			// récupération de l'annotation.
			InjectNanoRepository repositoryDescriptor = field.getAnnotation(InjectNanoRepository.class);
			
			
			try {
				
				
				// validation du contenu de l'annotation
				validateAnnotation(repositoryDescriptor);
				
				
				// récupération de la propriété fournissant le fichier au repository que l'on va créé
				HomeServerPropertyHolder persistentFileProperty = this.propertiesManager.getPropertyHolder(repositoryDescriptor.file().value());

				// gestion du préprocessor
				Consumer<?> preProcessor = null;
				
				// si preprocessor présent, instantiation
				if (!repositoryDescriptor.preProcessor().equals(DefaultPreProcessor.class)) {
					preProcessor = repositoryDescriptor.preProcessor().newInstance();
				}
				
				
				// on controle que la prop est bien récupérée. Sans Elle on ne peut pas créer le repository.
				Assert.isTrue(persistentFileProperty != null,
						"La propriété [" + repositoryDescriptor.file().value() 
						+ "] pour le repository sur le type [" 
								+ repositoryDescriptor.persistedType() + "] n'a pas été trouvée");
				
				// création dynamique du repo ou récupération du cache
//				NanoRepository repositoryToSet = new NanoRepository(repositoryDescriptor.persistedType(),
//						persistentFileProperty, repositoryDescriptor.idField());
				
				NanoRepository repositoryToSet = null;
				
				// si repo déjà présent, récupération du manager
				if (this.repoManager.containsRepository(repositoryDescriptor.file().value())) {
					repositoryToSet = this.repoManager.getRepository(repositoryDescriptor.file().value());
				} else {
					// sinon création et ajout
					repositoryToSet = new NanoRepository(repositoryDescriptor.persistedType(),
							persistentFileProperty, repositoryDescriptor.idField(), preProcessor);
					this.repoManager.add(repositoryToSet);
				}

				// modification accessibilité du champ pour affectation du repos
				ReflectionUtils.makeAccessible(field);

				
				// affectation du repository
				field.set(beanToPostProcess, repositoryToSet);
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				throw new InvalidPropertyException(beanToPostProcess.getClass(), field.getName(),
						"Probleme lors de la génération du repository", e);
			}

		}
		
		
		return beanToPostProcess;
		
	}

	/**
	 * Permet de valider l'annotation récupérée sur un champ.
	 * @param repositoryDescriptor annotation à valider.
	 */
	private void validateAnnotation(final InjectNanoRepository repositoryDescriptor) {
		Assert.notNull(repositoryDescriptor, "repositoryDescriptor can't be null");
		Assert.notNull(repositoryDescriptor.file(), "repositoryDescriptor.file can't be null");
		Assert.notNull(repositoryDescriptor.persistedType(), "repositoryDescriptor.persistedType can't be null");
		Assert.hasLength(repositoryDescriptor.idField(), "repositoryDescriptor.idField can't be null or empty");
	}

	
	
	
}
