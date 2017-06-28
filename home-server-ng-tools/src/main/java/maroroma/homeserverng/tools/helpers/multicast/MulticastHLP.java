package maroroma.homeserverng.tools.helpers.multicast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.Synchronized;

import org.springframework.util.Assert;


/**
 * Helper pour la gestion d'appel en mode asynchrones simultanés.
 * <br /> Ce helper permet :
 * <br /> - de préparer des appels pour des méthodes initialements synchrones. 
 * Le multicaster réalisera de lui même des lancement asynchrones sur ces méthodes
 * <br /> - de préparer des appels pour des méthodes initialements asynchrones. 
 * Le multicaster lancera un appel simple (cas des méthodes annotées @Async)
 * <br /> --Le traitement des résultats peut être réalisés à postériori, avec une séparation claire entre
 * les appels qui sont passés et les appels ko.
 * @author RLEVEXIE
 *
 */
public class MulticastHLP {

	/**
	 * Temps d'attente par défault.
	 */
	private static final int DEFAULT_TIMEOUT = 5000;

	/**
	 * Lock pour la mise à jour des retours des fonctions appelées en asynchrones.
	 */
	private final Object onFunctionCompleteLock = new Object();

	/**
	 * Liste des fonctions à appeler.
	 * <br /> cette liste correspond à la liste des fonctions dont le multicaster s'occupe de gérer
	 * l'asynchronisme.
	 */
	Map<String, MulticastRunnable> listeFonctions = new HashMap<String, MulticastRunnable>();

	
	/**
	 * Agregator pour le traitement de la réponse finale.
	 */
	private MulticastAgregator agregator = null;

	/**
	 * Permet de stocker l'ensemble des retours des appels asynchrones.
	 * <br /> est ensuite descendu à l'agregator pour traitement.
	 */
	private MulticastResult result = null;

	/**
	 * Permet de réaliser une attente synchrone pour les méthodes 
	 * utilisées par le multicaster qui sont déjà asynchrones.
	 */
	private CountDownLatch cdlAsyncFunctions = null;

	/**
	 * Executor pour les threads multiples.
	 */
	ExecutorService executor = null;

	/**
	 * Permet de créer un {@link MulticastHLP}.
	 * @return {@link MulticastHLP}
	 */
	public static MulticastHLP create() {
		return new MulticastHLP();
	}

	/**
	 * Constructeur protected (plus clean pour le fluent builder).
	 */
	protected MulticastHLP() {
		// init du result
		this.result = new MulticastResult();
		// init de la liste des fonctions.
		this.listeFonctions = new HashMap<String, MulticastRunnable>();
	}

	/**
	 * Ajoute le résultat pour la fonction donnée dans le result du multicast.
	 * <br /> attention, cette méthode n'est pas threadsafe
	 * @param idFunction identifiant de la fonction
	 * @param success retour de la fonction en succès
	 */
	protected void addResultAndCheck(final String idFunction, final Object success) {
		this.result.addResult(idFunction, success);
		checkAndComplete();
	}

	/**
	 * Ajoute l'exception pour la fonction donnée dans le result du multicast.
	 * <br /> attention, cette méthode n'est pas threadsafe
	 * @param idFunction identifiant de la fonction
	 * @param exception retour de la fonction en erreur
	 */
	protected void addExceptionAndCheck(final String idFunction, final Throwable exception) {
		this.result.addException(idFunction, exception);
		checkAndComplete();
	}

	/**
	 * Permet de mettre à jour le result avec le résultat d'une fonction complétée avec succès.
	 * @param idFunction identifiant de la fonction
	 * @param success retour de la fonction en succès
	 */
	// lock pour la mise à jour par des threads multiples de l'objet de résultat
	@Synchronized("onFunctionCompleteLock")
	protected void increaseSuccess(final String idFunction, final Object success) {
		addResultAndCheck(idFunction, success);
	}




	/**
	 * Permet de mettre à jour le result avec le résultat d'une fonction complétée avec erreur.
	 * @param idFunction identifiant de la fonction
	 * @param exception retour de la fonction en erreur
	 */
	// lock pour la mise à jour par des threads multiples de l'objet de résultat
	@Synchronized("onFunctionCompleteLock")
	protected void increaseError(final String idFunction, final Throwable exception) {
		addExceptionAndCheck(idFunction, exception);
	}

	/**
	 * Détermine si toutes les taches sont complétées.
	 * @return true si toutes les taches sont complétées.
	 */
	protected boolean isAllExecuted() {
		// on considère que le traitement est terminé
		// si le nombre total de result est égal à la somme
		// des fonctions synchrones et asynchrones)
		return (this.listeFonctions.size()) == result.getTotalCount();
	}

	/**
	 * Détermine si toutes les taches ont été exécutées, et appel l'aggregator le cas échéant.
	 */
	protected void checkAndComplete() {
		if (isAllExecuted()) {
			this.agregator.accept(this.result);
		}
	}

	/**
	 * Permet de valider les arguments d'ajout de fonction.
	 * @param idFunction identifiant de la fonction
	 * @param innerCallable callable
	 */
	protected void validateCreateInternalCallableArguments(
			final String idFunction, final Object innerCallable) {
		// validation de l'id de la fonction
		Assert.hasLength(idFunction, "idFunction ne peut être vide");
		// validation du callable
		Assert.notNull(innerCallable, "innerCallable ne peut être null");
	}

	/**
	 * Permet de créer une implémentation de runnable qui sera utilisée par l'executor lors
	 * du déclenchement des appels des fonctions.
	 * <br /> Cette méthode est appelée pour des fonctions synchrones rendues asynchrones par le multicaster
	 * @param idFunction identifiant de la fonction
	 * @param innerCallable code de la fonction
	 * @return MulticastRunnable
	 */
	protected MulticastRunnable createInternalCallable(final String idFunction, final Callable<?> innerCallable) {
		// validation des arguments
		validateCreateInternalCallableArguments(idFunction, innerCallable);

		// création du runnable pour fonctions synchrones
		return new MulticastRunnable(idFunction, this, innerCallable);
	}

	/**
	 * Permet d'ajouter une nouvelle fonctin à appeler par le multicaster.
	 * <br /> La fonction ajoutée via cette méthode doit être une fonction de base synchrone.
	 * Le multicast se charge de la lancer de manière asynchrone.
	 * @param idFunction identifiant unique de la fonction
	 * @param function callable contenant une fonction synchrone qui sera exécuté
	 * @return this (updated)
	 */
	public MulticastHLP prepareFunction(final String idFunction, final Callable<?> function) {

		// controles des entrées
		Assert.hasLength(idFunction, "idFunction ne peut être vide");
		Assert.isTrue(!this.listeFonctions.containsKey(idFunction), "Une fonction d'id " + idFunction + " est déjà présente");
		Assert.notNull(function, "function ne peut être null");

		// rajout de la fonction dans la liste de fonction synchrone à appeler
		this.listeFonctions.put(idFunction, createInternalCallable(idFunction, function));
		return this;
	}


	/**
	 * Lancement de l'éxécution de l'ensemble des fonctions qui ont été ajoutée via la méthode call.
	 * @param agregator agregator qui traitera l'ensemble des résultats.
	 * @return this (updated)
	 */
	public MulticastHLP emit(final MulticastAgregator agregator) {

		// controle des parametres
		Assert.notNull(agregator, "agregator ne peut être null");
		this.agregator = agregator;

		// réinitialisation des résultats
		this.result.clear();

		// execution des fonctions synchrones
		executeSynchronousFunctions();

		return this;
	}

	/**
	 * Exécution des fonction synchrones, que le multicaster execute de manière asynchrone.
	 */
	protected void executeSynchronousFunctions() {

		// il faut impérativement que la liste de méthodes à exécuter soit
		// supérieur à 0, sinon la création de l'executor tombe en erreur.
		if (!this.listeFonctions.isEmpty()) {
			
			// création de l'éxécutor pour le lancement des fonctions dans des threads distincts
			this.executor = Executors.newFixedThreadPool(this.listeFonctions.size());

			// pour chacune des fonctions synchrones, lancement de l'éxécution
			for (MulticastRunnable function : this.listeFonctions.values()) {
				this.executor.submit(function);
			}

			// préparation de l'arrêt
			this.executor.shutdown();
		}
	}

	/**
	 * Permet de réaliser une attente active pour la fin des traitements, avec 5 secondes comme timeout par défaut.
	 * @return this (updated)
	 * @throws InterruptedException InterruptedException
	 */
	public MulticastResult await() throws InterruptedException {
		return this.await(DEFAULT_TIMEOUT);
	}

	/**
	 * Permet de réaliser une attente active pour la fin des traitements.
	 * @param timeout timeout avant interruption
	 * @return this (updated)
	 * @throws InterruptedException InterruptedException
	 */
	public MulticastResult await(final long timeout) throws InterruptedException {

		long timeStart = System.currentTimeMillis();
		this.executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		long alreadyCounted = System.currentTimeMillis() - timeStart;

		// si des fonctions asynchrones sont présentes.
		if (this.cdlAsyncFunctions != null) {
			// le timeout doit être décrémenté par le temps passé à attendre les fonctions
			// rendues asynchrones par le multicaster.
			this.cdlAsyncFunctions.await(timeout - alreadyCounted, TimeUnit.MILLISECONDS);
		}
		return this.result;
	}

	/**
	 * Classe interne pour l'éxécution des fonctions ajoutées via la méthode call de la classe {@link MulticastHLP}.
	 * <br /> Cette classe est dédiée à l'execution des méthodes synchrones que le {@link MulticastHLP} rend asynchrone.
	 * @author RLEVEXIE
	 *
	 */
	protected class MulticastRunnable implements Callable<Object> {

		/**
		 * Celui qui lance les appels.
		 */
		protected MulticastHLP initiator;

		/**
		 * Callable portant le code à éxécuter.
		 */
		Callable<?> innerCallable;

		/**
		 * Identifiant unique de la fonction portée par le innerCallable.
		 */
		protected String idFunction;

		/**
		 * Constructeur.
		 * @param id identifiant de la fonction sous jacente.
		 * @param multicastInitiator lanceur
		 */
		MulticastRunnable(final String id, final MulticastHLP multicastInitiator) {
			// controles
			Assert.hasLength(id, "id ne peut être vide");
			Assert.notNull(multicastInitiator, "multicastInitiator ne peut être null");

			// affectation
			this.idFunction = id;
			this.initiator = multicastInitiator;
		}

		/**
		 * constructeur.
		 * @param id identifiant de la fonction sous jacente.
		 * @param multicastInitiator lanceur
		 * @param callable code à éxécuter.
		 */
		MulticastRunnable(final String id, final MulticastHLP multicastInitiator, final Callable<?> callable) {

			this(id, multicastInitiator);

			// controles
			Assert.notNull(callable, "innerCallable ne peut être null");

			this.innerCallable = callable;
		}



		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object call() throws Exception {
			try {
				// lancement de l'appel sur le callable
				// si succes, l'initiator est notifié du succès avec l'identifiant de la fonction ok et le retour de celle-ci
				this.initiator.increaseSuccess(this.idFunction,
						this.innerCallable.call());
			} catch (Exception e) {
				// si erreur, l'initiator est notifié de l'erreur avec
				// l'identifiant de la fonction ko et l'exception levée par celle-ci
				this.initiator.increaseError(this.idFunction, e);
			}

			return null;
		}

	}
}
