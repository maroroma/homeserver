package maroroma.homeserverng.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPluginPropertiesManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.config.PropertySetterListener;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;

/**
 * {@link BeanPostProcessor} et {@link HomeServerPluginPropertiesManager} permettant de gérer les {@link HomeServerPropertyHolder} :
 * <br /> - leur injection
 * <br /> - leur mise à jour dynamique.
 * @author RLEVEXIE
 *
 */
@Log4j2
public class HomeServerPluginPropertiesPostProcessor implements BeanPostProcessor, HomeServerPluginPropertiesManager {



	/**
	 * Ensemble des {@link HomeServerPropertiesSource} à utiliser pour le binding.
	 */
	private List<HomeServerPropertiesSource> propertiesSource;




	/**
	 * Ensemble des {@link HomeServerPropertiesSource} à utiliser pour le binding.
	 * @return -
	 */
	public List<HomeServerPropertiesSource> getPropertiesSource() {
		return propertiesSource;
	}

	/**
	 * Constructeur.
	 * @param sources liste des {@link HomeServerPropertiesSource} utilisées pour le binding.
	 */
	public HomeServerPluginPropertiesPostProcessor(final List<HomeServerPropertiesSource> sources) {
		super();
		this.propertiesSource = sources;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object postProcessAfterInitialization(final Object beanToPostProcess, final String beanName) throws BeansException {
		// après l'init des bean, on détecte les méthodes qui doivent être appelées sur une modification de propriété
		bindPropertiesToRefreshMethods(beanToPostProcess);
		return beanToPostProcess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object beanToPostProcess, final String beanName) throws BeansException {

		// avant l'init des beans on initialise les propriétés dynamiques
		bindFieldsToProperties(beanToPostProcess);

		return beanToPostProcess;
	}

	/**
	 * Permet d'abonner à une modification d'une propriété une méthode annotée {@link PropertyRefreshHandlers}.
	 * @param beanToPostProcess bean à scanner.
	 */
	private void bindPropertiesToRefreshMethods(final Object beanToPostProcess) {

		// détermine si le bean est un proxy
		final boolean isProxified = Advised.class.isAssignableFrom(beanToPostProcess.getClass());
		

		// récupération de la classe concrete si proxy
		Class<?> concreteClass = isProxified ? ((Advised) beanToPostProcess).getTargetSource().getTargetClass() : beanToPostProcess.getClass();

		
		// scan des méthodes
		ReflectionUtils.doWithMethods(concreteClass, new MethodCallback() {

			@Override
			public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
				
				// si annotation présente
				if (method.isAnnotationPresent(PropertyRefreshHandlers.class)) {
					
					// propriétés  à écoute
					String[] propertyNamesForRefresh = method.getAnnotation(PropertyRefreshHandlers.class).value();
					Method toInvokeMethod = method;
					
					// récupération de la méthode à appeler en cas de mofication (prise en compte du proxy)
					try {
						if (isProxified) {
							toInvokeMethod = beanToPostProcess.getClass().getMethod(method.getName());
						}
					} catch (NoSuchMethodException | SecurityException e) {
						log.warn("problème lors de la récupération de la méthode à executer", e);
					}
					
					final Method finalMethod = toInvokeMethod;
					
					// on rend la méthode accessible quoi qu'il arrive
					ReflectionUtils.makeAccessible(finalMethod);
					
					for (String propertyName : propertyNamesForRefresh) {
						HomeServerPropertyHolder propertyHolder = getPropertyHolder(propertyName);
						Assert.notNull(propertyHolder,  "La propriété [" + propertyName + "] n'a pas été trouvée comme source de mise à jour");
						propertyHolder.addSetEventHandler(new PropertySetterListener() {

							@Override
							public void onSetProperty(final HomeServerPropertyHolder homeServerPropertyHolder) {
								try {
									// invocation de la méthode lors de la modification de la propriété
									finalMethod.invoke(beanToPostProcess);
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									log.warn("problème lors de l'abonnement à la modification de la propriété", e);
								}
							}
						});
					}
					
				}
			}
		});
	}

	/**
	 * Permet d'associer un field annoté avec {@link Property} avec une {@link HomeServerPropertyHolder}.
	 * @param beanToPostProcess -
	 */
	private void bindFieldsToProperties(final Object beanToPostProcess) {
		final List<Field> fieldsToPopulate = new ArrayList<>();

		// récupération de la liste des propriétés annotées HomeServerProperty
		ReflectionUtils.doWithFields(beanToPostProcess.getClass(), new FieldCallback() {

			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if (field.isAnnotationPresent(Property.class)) {
					fieldsToPopulate.add(field);
				}
			}
		});

		// sur l'ensemble des champs récupérés, binding et
		for (Field field : fieldsToPopulate) {

			Property propertyDescriptor = field.getAnnotation(Property.class);
			HomeServerPropertyHolder property = getPropertyHolder(propertyDescriptor.value());


			try {
				Assert.isTrue(property != null, "Aucune properties [" + propertyDescriptor.value() + "] trouvée");
				ReflectionUtils.makeAccessible(field);
				field.set(beanToPostProcess, property);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new InvalidPropertyException(beanToPostProcess.getClass(), field.getName(),
						"Probleme lors du binding de la propriété [" + propertyDescriptor.value() + "]", e);
			}

		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HomeServerPropertyHolder> getProperties() throws HomeServerException {
		List<HomeServerPropertyHolder> returnValue = new ArrayList<>();

		for (HomeServerPropertiesSource propertySource : this.getPropertiesSource()) {
			returnValue.addAll(propertySource.getProperties());
		}

		return returnValue;
	}


	/**
	 * {@inheritDoc}
	 * @return 
	 */
	@Override
	public void updateProperty(final String id, final HomeServerPropertyHolder newValue) {
		HomeServerPropertyHolder toUpdate = this.getPropertyHolder(id);
		
		Assert.notNull(toUpdate, "La propriété [" + id + "] n'a pas été trouvée");
		
		Assert.isTrue(!toUpdate.isReadOnly(), "La propriété [" + id + "] est en lecture seule");
		
		toUpdate.setValue(newValue.getValue());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public HomeServerPropertyHolder getPropertyHolder(final String propertyName) {

		HomeServerPropertyHolder returnValue = null;

		if (this.propertiesSource != null) {
			for (HomeServerPropertiesSource abstractHomeServerPropertiesSource : propertiesSource) {
				if (abstractHomeServerPropertiesSource.containsProperty(propertyName)) {
					returnValue = abstractHomeServerPropertiesSource.getProperty(propertyName);
				}
			}
		}

		return returnValue;
	}

}
