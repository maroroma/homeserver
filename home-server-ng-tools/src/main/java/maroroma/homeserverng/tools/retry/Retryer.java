package maroroma.homeserverng.tools.retry;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Classe utilitaire permettant de rejouer plusieurs fois des appels, en modifiant sur exception les paramètres d'entrée initiaux.
 * <br /> {@link Retryer#tryTo(Function)} permet de créer le retryer
 * <br /> {@link Retryer#thenRetry(int)} permet d'indiquer le nombre d'essai de la dernière description d'essai précisée
 * <br /> {@link Retryer#thenRetryWith(Function, int)} permet d'ajouter dans la stack 
 * une nouvelle action à mener en cas d'erreur, et le nombre de fois qu'elle doit être exécutée
 * <br /> {@link Retryer#onExceptionDo(Class, BiConsumer)} permet de spécifier une action si une erreur est rencontrée
 * <br /> {@link Retryer#onExceptionTransformInput(Class, BiFunction)} permet de modifier l'input initial du retryer avant de lancer un nouvel essai.
 * @author RLEVEXIE
 *
 * @param <T> entrée de l'appel
 * @param <U> sortie de l'appel
 */
@Slf4j
public final class Retryer<T, U> {

	/**
	 * Liste de l'ensemble des essais {@link Function} à lancer.
	 */
	private List<Function<T, U>> tries;
	
	/**
	 * Association entre des types d'exception et une action associée.
	 */
	private Map<Class<? 
			extends Exception>, BiConsumer<T, Exception>> exceptionDoers;
	
	/**
	 * Association entre des types d'exceptions et une transformation de l'input initial du retryer.
	 */
	private Map<Class<? 
			extends Exception>, BiFunction<T, Exception, T>> exceptionInputTransformers;
	
	
	/**
	 * Constructeur.
	 * @param main fonction initiale à appeler lors du premier essai.
	 */
	private Retryer(final Function<T, U> main) {
		Assert.notNull(main, "main can't be null");
		this.tries = new ArrayList<>();
		this.exceptionDoers = new HashMap<>();
		this.exceptionInputTransformers = new HashMap<>();
		this.tries.add(main);
	}
	
	/**
	 * Rajoute nbRetry essais identique au dernier essai configuré.
	 * @param nbRetry nombre d'essai à rajouter
	 * @return -
	 */
	public Retryer<T, U> thenRetry(final int nbRetry) {
		for (int i = 0; i < nbRetry; i++) {
			this.tries.add(this.tries.get(this.tries.size() - 1));
		}
		return this;
	}
	
	/**
	 * Rajoute nbRetry essai pour la nouvelle fonction retry.
	 * @param retry fonction à utiliser pour un nouvel essai, dont le nombre d'appel est fixé ci-dessous
	 * @param nbRetry nombre d'essais associés à la nouvelle fonction.
	 * @return -
	 */
	public Retryer<T, U> thenRetryWith(final Function<T, U> retry, final int nbRetry) {
		for (int i = 0; i < nbRetry; i++) {
			this.tries.add(retry);
		}
		return this;
	}
	
	/**
	 * Permet de spécifier une action lors d'une exception d'un certain type. <br /> L'action peut modifier l'input initial, 
	 * mais pas le substituer, même si les modifications ne sont pas conseillées.
	 * @param error type d'erreur à gérer
	 * @param exceptionAction action à réaliser sur la réception de l'erreur
	 * @param <E> type d'exception géré
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <E extends Exception> Retryer<T, U> onExceptionDo(final Class<E> error, final BiConsumer<T, E> exceptionAction) {
		this.exceptionDoers.put(error, (BiConsumer<T, Exception>) exceptionAction);
		return this;
	}
	
	/**
	 * Permet de spécifier une transformation de l'input initial sur la réception d'une erreur d'un certain type.
	 * @param error -
	 * @param exceptionTransformer fonction reconstruisant l'input initial
	 * @param <E> -
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <E extends Exception> Retryer<T, U> onExceptionTransformInput(final Class<E> error, final BiFunction<T, E, T> exceptionTransformer) {
		this.exceptionInputTransformers.put(error,  (BiFunction<T, Exception, T>) exceptionTransformer);
		return this;
	}
	
	/**
	 * Création du Retryer pour les types attendus, avec la fonction initiale donnée.
	 * @param toTry fonction à executer en haut de la pile.
	 * @param <T> type en entrée
	 * @param <U> type en sortie
	 * @return -
	 */
	public static <T, U> Retryer<T, U> tryTo(final Function<T, U> toTry) {
		return new Retryer<>(toTry);
	}
	
	public static <T, U> Retryer<HttpEntity<T>, ResponseEntity<U>> tryWithRestTemplate(final Function<HttpEntity<T>, ResponseEntity<U>> toTry) {
		return new Retryer<>(toTry);
	}

	/**
	 * Lancement des appels, dont l'appel principale, ces différents essais, et des fonctions intermédiaire.
	 * @param input -
	 * @return la sortie
	 * @throws HomeServerException -
	 */
	public U apply(final T input) throws HomeServerException {
		
		// permet de savoir si un appel est passé
		boolean success = false;
		
		// valeur à retournée suite à un appel en succès
		U returnValue = null;
		
		// dernière exception relevée
		Exception lastException = null;
		
		// input, potentiellement l'initial, puis le transformé au besoin
		T chainedInput = input;
		
		// pour chacun des essais programmés
		for (int i = 0; i < this.tries.size() && !success; i++) {
			try {
				// appel de l'essai en cours
				returnValue = this.tries.get(i).apply(chainedInput);
				// l'appel est tagué en succès
				success = true;
			} catch (Exception e) {
				// si erreur, on store la dernière exception
				lastException = e;
				log.warn("essai n° " + i + "en echec");
				
				// action typée par exception
				if (this.exceptionDoers.containsKey(e.getClass())) {
					this.exceptionDoers.get(e.getClass()).accept(input, lastException);
				}
				
				// transformation potentielle de l'input initial
				if (this.exceptionInputTransformers.containsKey(e.getClass())) {
					chainedInput = this.exceptionInputTransformers.get(e.getClass()).apply(chainedInput, e);
				}
				
				success = false;
			}
		}
		
		
		// si aucun retour en succès, on lève la dernière exception rencontrée
		if (!success) {
			throw new HomeServerException("Tous les essais ont échoués", lastException);
		}
		
		return returnValue;
	}
}
