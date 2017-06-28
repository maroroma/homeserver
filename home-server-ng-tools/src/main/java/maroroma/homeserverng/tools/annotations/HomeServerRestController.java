package maroroma.homeserverng.tools.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RestController;

/**
 * Annotation permettant de déclarer un controller pour le homeserver.
 * Permet de lier un {@link RestController} à un descripteur de module.
 * @author RLEVEXIE
 *
 */
@RestController
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HomeServerRestController {


	/**
	 * Descripteur de module associé au controller.
	 * @return -
	 */
	Class<?> moduleDescriptor() default Object.class;
	
}
