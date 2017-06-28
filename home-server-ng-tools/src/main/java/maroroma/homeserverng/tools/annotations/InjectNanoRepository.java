package maroroma.homeserverng.tools.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation permettant d'injecter automatiquement un NanoRepository sur une classe.
 * <br /> On doit fournir pour cela le type d'objet à persiter, la {@link Property} correspondant au fichier de persistence, et l'identifiant
 * de l'objet pour faciliter les recherches (par défaut "id");
 * @author rlevexie
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectNanoRepository {

	/**
	 * Type à persister.
	 * @return -
	 */
	Class<?> persistedType();
	
	/**
	 * Nom du champ servant d'identifiant sur le type à persister. Par défaut "id".
	 * @return -
	 */
	String idField() default "id";
	
	
	/**
	 * Annotation donnant la {@link Property} correspondant au fichier de persistence.
	 * @return -
	 */
	Property file();
	
	
}
