package maroroma.homeserverng.tools.helpers.multicast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * DTO utilitaire pour le traitement de la finalisation d'un appel multicast.
 * @author RLEVEXIE
 *
 */
public class MulticastResult {
	/**
	 * Liste des résultats ok.
	 */
	private Map<String, Object> listeResultats = new HashMap<String, Object>();
	/**
	 * Liste des résultats ko.
	 */
	private Map<String, Throwable> listeExecptions = new HashMap<String, Throwable>();

	/**
	 * Récupération du résultat d'une fonction ciblée par son identifiant.
	 * @param identifiant identifiant de la fonction dont on veut le résultat
	 * @param <T> type attendu pour la récupération
	 * @return Le type de retour de la fonction.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getResult(final String identifiant) {

		// controle
		Assert.hasLength(identifiant, "identifiant ne peut être vide");

		T returnValue = null;

		// retour du résultat si présent
		if (this.containsResult(identifiant)) {
			returnValue = (T) this.listeResultats.get(identifiant);
		}

		return returnValue;
	}

	/**
	 * Retourne l'ensemble des résultats en succes dans un liste homogene castée en type T.
	 * Attention, l'appe peut exploser si un des éléments n'est pas castable en type T.
	 * @param <T> type attendu pour la récupération
	 * @return liste d'objets de type T issus de la liste de résultats en succès.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getResults() {
		// liste à retourner
		List<T> returnValue =  new ArrayList<T>();
		
		// récupération de toutes les valeurs en succès avec tentative de cast
		for (Object source : this.listeResultats.values()) {
			returnValue.add((T) source);
		}
		
		return returnValue;
	}

	/**
	 * Récupération de l'exception retournée par la fonction ciblée par son identifiant.
	 * @param identifiant identifiant de la fonction dont on veut récupérer le retour en erreur
	 * @param <T> type de l'ex
	 * @return l'exception levée par la fonction d'identifiant donné.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Throwable> T getException(final String identifiant) {

		// controle
		Assert.hasLength(identifiant, "identifiant ne peut être vide");

		T returnValue = null;

		// récup de l'exceptionsi présente
		if (this.containsException(identifiant)) {
			returnValue = (T) this.listeExecptions.get(identifiant);
		}
		return returnValue;
	}

	/**
	 * Détermine si pour la fonction donnée on a un résultat ok.
	 * @param identifiant identifiant de la fonction
	 * @return true si résultat ok présent
	 */
	public boolean containsResult(final String identifiant) {
		Assert.hasLength(identifiant, "identifiant ne peut être vide");
		return this.listeResultats.containsKey(identifiant);
	}

	/**
	 * Détermine si pour la fonction donnée on a un résultat ko.
	 * @param identifiant identifiant de la fonction
	 * @return true si résultat ko présent
	 */
	public boolean containsException(final String identifiant) {
		Assert.hasLength(identifiant, "identifiant ne peut être vide");
		return this.listeExecptions.containsKey(identifiant);
	}

	/**
	 * Nettoyage.
	 */
	public void clear() {
		this.listeResultats.clear();
		this.listeExecptions.clear();
	}

	/**
	 * retourne le nombre de réponse ok.
	 * @return int
	 */
	public int getSuccessCount() {
		return this.listeResultats.size();
	}

	/**
	 * Retourne le nombre de réponse ko.?
	 * @return int
	 */
	public int getErrorCount() {
		return this.listeExecptions.size();
	}

	/**
	 * Retourne le nombre total traité.
	 * @return int
	 */
	public int getTotalCount() {
		return this.getSuccessCount() + this.getErrorCount();
	}

	/**
	 * Ajoute un résultat ok associé à un identifiant de fonction.
	 * @param identifiant identifiant de la fonction
	 * @param result résultat.
	 */
	public void addResult(final String identifiant, final Object result) {
		Assert.hasLength(identifiant, "identifiant ne peut être vide");
		Assert.isTrue(!this.listeResultats.containsKey(identifiant), "Un résultat d'id " + identifiant + " est déjà présent");
		this.listeResultats.put(identifiant, result);
	}

	/**
	 * Ajoute un résultat ko associé à un identifiant de fonction.
	 * @param identifiant identifiant de la fonction
	 * @param result résultat.
	 */
	public void addException(final String identifiant, final Throwable result) {
		Assert.hasLength(identifiant, "identifiant ne peut être vide");
		Assert.isTrue(!this.listeExecptions.containsKey(identifiant), "Une exception d'id " + identifiant + " est déjà présente");
		this.listeExecptions.put(identifiant, result);
	}
}
