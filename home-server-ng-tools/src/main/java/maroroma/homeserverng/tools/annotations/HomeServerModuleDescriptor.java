package maroroma.homeserverng.tools.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * annotation pour décrire un module dans un point unique.
 * @author RLEVEXIE
 *
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HomeServerModuleDescriptor {
	/**
	 * Identifiant du module.
	 * @return -
	 */
	String moduleId();
	/**
	 * Description du module.
	 * @return -
	 */
	String moduleDescription();
	/**
	 * Nom affiché pour le module.
	 * @return -
	 */
	String displayName();
	/**
	 * Css appliquée pour menu.
	 * @return -
	 */
	String cssMenu();
	/**
	 * Code présent coté serveur pour le module.
	 * @return -
	 */
	boolean hasServerSide();
	/**
	 * Code présent coté ihm pour le module.
	 * @return -
	 */
	boolean hasClientSide();
	
	/**
	 * fichier de properties correspondant.
	 * @return -
	 */
	String propertiesFile() default "";
	
	/**
	 * Détermine si le module peut être modifié.
	 * @return -
	 */
	boolean isReadOnly() default false;
}
