package maroroma.homeserverng.config;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPluginPropertiesManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.config.PropertySetterListener;
import maroroma.homeserverng.tools.config.PropertyValueResolver;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.helpers.Tuple;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link BeanPostProcessor} et {@link HomeServerPluginPropertiesManager} permettant de gérer les {@link HomeServerPropertyHolder} :
 * <br /> - leur injection
 * <br /> - leur mise à jour dynamique.
 * @author RLEVEXIE
 *
 */
@Log4j2
public class HomeServerPluginPropertiesPostProcessor implements BeanPostProcessor, HomeServerPluginPropertiesManager, PropertyValueResolver {



	/**
	 * Ensemble des {@link HomeServerPropertiesSource} à utiliser pour le binding.
	 */
	private List<HomeServerPropertiesSource> propertiesSource;

	/**
	 * PErmet d'accéder aux propriétés de l'applications pour résoudre les variables.
	 */
	@Autowired
	private Environment env;




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
	 * application du processor en tant que resolver sur toutes les datasources.
	 */
	@PostConstruct
	private void postConstruct() {
		// application sur chaque source du resolver
		this.propertiesSource.forEach(onePropSource -> onePropSource.applyResolver(this));
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
		return this.getPropertiesSource()
				.stream()
				.map(oneSource -> oneSource.getProperties())
				.flatMap(properties -> properties.stream())
				.collect(Collectors.toList());
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

		return this.propertiesSource
				.stream()
				.filter(onePropertySource -> onePropertySource.containsProperty(propertyName))
				.findAny()
				.map(source -> source.getProperty(propertyName))
				.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String resolve(final String rawValue) {
		// extraction du nom de propriété du type 
		// blabla${VARIABLE}mlkqsdmlkqsd
		
		
		// récupération des index de débuts.
		List<Tuple<String, String>> matches = 
				maroroma.homeserverng.tools.helpers.StringUtils.findAllPatterns(rawValue, "${*}", false)
				.stream().map(oneMatch -> Tuple.from(oneMatch, oneMatch))
				.collect(Collectors.toList());
		
		
		matches.forEach(oneMatch -> {
			HomeServerPropertyHolder holder = this.getPropertyHolder(oneMatch.getItem1());
			if (holder != null) {
				oneMatch.setItem2(holder.getResolvedValue());
			} else {
				String resolvedValue = this.env.getProperty(oneMatch.getItem1());
				if (StringUtils.hasLength(resolvedValue)) {
					oneMatch.setItem2(resolvedValue);
				}
			}
		});
		
		String returnValue = rawValue;
		
		for (Tuple<String, String> tuple : matches) {
			returnValue = returnValue.replaceAll("\\$\\{" + tuple.getItem1() + "\\}", tuple.getItem2());
		}
		
		// bon pour l'instant les regex c'est pas ça
//		Pattern p = Pattern.compile(".*\\$\\{(.+)\\}.*");
////		Pattern p = Pattern.compile("\\$\\{.*\\}");
////		Pattern p = Pattern.compile("\\$\\{(.+)\\}?");
//		Matcher m = p.matcher(rawValue);
//		while (m.find()) {
//			System.out.println("count" + m.groupCount() +  "result : "  + m.group());
//			for (int i = 0; i < m.groupCount(); i++) {
//				System.out.println(i + " : " + m.group(i));
//				
//			}
//		}
//		Matcher m2 = p.matcher(rawValue);
//		if(m2.matches()) {
//			for (int i = 0; i < m2.groupCount(); i++) {
//				System.out.println("m2 : " + i + " : " + m2.group(i));
//				
//			}
//		}

		return returnValue;
	}

}
