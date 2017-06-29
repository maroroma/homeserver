package maroroma.homeserverng.tools.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import maroroma.homeserverng.tools.sse.SSEStreamable;

/**
 * Annotation de déterminer la fréquence d'appel de l'implémentation de la méthode {@link SSEStreamable#process()}.
 * <br /> Elle permet de définir la propriété utilisée en sous main pour la programmation de la tache de scheduling associée.
 * <br /> Cela permet notamment de définir à chaud une nouvelle fréquence.
 * @author rlevexie
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Streamed {

	/**
	 * Annotation donnant la {@link Property} correspondant à la fréquence de streaming demandée.
	 * @return -
	 */
	Property fixedDelay();
	
	/**
	 * Nom de l'event qui sera associé à la récupération des données émises à travers le SSE.
	 * @return -
	 */
	String eventName() default "NC";
	
	/**
	 * Détermine le format des données à envoyer.
	 * <br /> Par défaut à true.
	 * @return -
	 */
	String mediaType();
	
	
}
