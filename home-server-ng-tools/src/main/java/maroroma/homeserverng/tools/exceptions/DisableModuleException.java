package maroroma.homeserverng.tools.exceptions;

/**
 * Exception levée lors d'un appel à un module désactivé.
 * @author RLEVEXIE
 *
 */
public class DisableModuleException extends HomeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2520454858008660544L;


	/**
	 * Constructeur.
	 * @param moduleID -
	 */
	public DisableModuleException(final String moduleID) {
		super("Le module solicité (" + moduleID 
		+ ") n'est pas activé");
	}
}
