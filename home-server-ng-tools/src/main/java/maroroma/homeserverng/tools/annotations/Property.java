package maroroma.homeserverng.tools.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation à placer dans les property holder pour gérer la lecture et l'écriture automatique des propriétés.
 * @author rlevexie
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {

	/**
	 * nom de la propriété.
	 * @return -
	 */
	String value() default "NC";
	
}
