package maroroma.homeserverng.administration.model;

/**
 * Permet de déterminer le status d'éxécution du server.
 * <br /> Principalement utilisé dans les cinématiques de redémarrage lié à
 * l'installation d'un plugin.
 * @author rlevexie
 *
 */
public enum HomeServerRunningStatus {

	/**
	 * Le server n'est pas encore chargé.
	 */
	STARTING,
	/**
	 * Status nominal, le serveur est en cours d'éxécution.
	 */
	RUNNING,
	/**
	 * Le server est en attente d'arrêt.
	 * il sera normalment redémarré par son starter.
	 */
	STOPPING
	
}
