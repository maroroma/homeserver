package maroroma.homeserverng.scanner.model;

/**
 * Les différents types d'events possibles emis par le service de scanner.
 * @author RLEVEXIE
 *
 */
public enum ScannerEventType {

	/**
	 * Demande de scan reçue.
	 */
	SCAN_REQUESTED("scanRequested"),
	
	/**
	 * Scan en cours.
	 */
	SCAN_RUNNING("scanRunning"),
	
	/**
	 * Erreur lors du scan.
	 */
	SCAN_IN_ERROR("scanInError"),
	
	/**
	 * Image raw prête.
	 */
	SCAN_PNM_READY("scanPnmReady"),
	
	/**
	 * Conversion jpg en cours.
	 */
	SCAN_CONVERSION_RUNNING("scanConversionRunning"),
	
	/**
	 * Scan terminé.
	 */
	SCAN_TERMINATED("scanTerminated");
	
	/**
	 * Nom de l'event associé.
	 */
	private String eventName;
	
	/**
	 * Retourn le nom de l'event associé.
	 * @return -
	 */
	public String getName() {
		return this.eventName;
	}
	
	/**
	 * Constructeur.
	 * @param value -
	 */
	ScannerEventType(final String value) {
		this.eventName = value;
	}
	
}
