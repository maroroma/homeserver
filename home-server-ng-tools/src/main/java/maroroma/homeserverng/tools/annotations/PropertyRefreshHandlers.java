package maroroma.homeserverng.tools.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation à placer sur les méthodes qui doivent être appelée en cas de changement de valeur d'une ou plusieurs propriétés.
 * @author rlevexie
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyRefreshHandlers {

	/**
	 * nom des propriétés pour lesquelles un rafraichissement implique un appel de la méthode.
	 * @return -
	 */
	String[] value() default {"NC"};

}
