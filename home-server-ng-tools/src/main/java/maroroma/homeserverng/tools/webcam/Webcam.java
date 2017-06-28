package maroroma.homeserverng.tools.webcam;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.sarxos.webcam.WebcamUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;

/**
 * Description d'une webcam logique.
 * <br /> Permet notamment de persister les informations relatives à une webcam déclarées.
 * @author rlevexie
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log4j2
public class Webcam {
	
	/**
	 * Identifiant technique (identifiant de la {@link Webcam#getPhysicalWebcam()}) de la webcam.
	 */
	private String id;
	
	/**
	 * Identifiant de l'agent gérant la webcam.
	 */
	private String agentId;
	
	/**
	 * Nom "lisible" de la webcam.
	 */
	private String name;
	/**
	 * Emplacement de la webcam.
	 */
	private String emplacement;
	
	/**
	 * {@link Viewport} disponibles pour la webcam.
	 */
	private List<Viewport> viewPorts = new ArrayList<>();
	
	/**
	 * La webcam est-elle démarrée par défaut ?
	 */
	private boolean webCamstarted;
	
	/**
	 * La détection de mouvement est-elle démarrée par défaut ?
	 */
	private boolean motionDetectionStarted;
	
	/**
	 * {@link Viewport} sélectionné pour la caméra.
	 */
	private Viewport selectedViewPort;
	
	/**
	 * Fréquence pour la surveillance.
	 */
	private int frequency;
	
	/**
	 * {@link MotionListener}, pour le traitement de la détection de mouvement.
	 */
	@JsonIgnore
	private MotionListener listener;

	/**
	 * Retourne la webcam logique correspondant à cette webcam.
	 * @return -
	 * @throws HomeServerException -
	 */
	@JsonIgnore
	public com.github.sarxos.webcam.Webcam getPhysicalWebcam() throws HomeServerException {
		return PhysicalWebCamHLP.getPhysicalWebcam(this);
	}
	
	/**
	 * Initialisation de la webcam par rapport aux informations persitées.
	 * <br /> cette méthode doit être appelée au démarrage du serveur pour rebasculer la caméra dans son état attendu.
	 * @throws HomeServerException -
	 */
	public void initializeFromParameters() throws HomeServerException {
		
		if (this.isWebCamstarted()) {
			this.startWebCam();
			log.info("[" + this.getId() + "] démarrage auto ok.");
		} else {
			this.stopWebCam();
		}
		
		if (this.isMotionDetectionStarted()) {
			this.startMotionDetection();
			log.info("[" + this.getId() + "] motioncapture auto ok.");
		} else {
			this.stopMotionDetection();
		}
	}
	
	/**
	 * Démarrage de la webcam.
	 * @throws HomeServerException -
	 */
	public void startWebCam() throws HomeServerException {
		if (this.getPhysicalWebcam().isOpen()) {
			this.getPhysicalWebcam().close();
		}
		this.getPhysicalWebcam().setViewSize(WebcamConverter.convertDimension(this.getSelectedViewPort()));
		this.getPhysicalWebcam().open(true);
		this.setWebCamstarted(true);
		log.info("[" + this.getId() + "] démarrée.");
	}
	
	/**
	 * Arrêt de la webcam.
	 * @throws HomeServerException -
	 */
	public void stopWebCam() throws HomeServerException {
		
		this.stopMotionDetection();
			
		this.getPhysicalWebcam().close();
		
		this.setWebCamstarted(false);
		log.info("[" + this.getId() + "] arrêtée.");
	}
	
	/**
	 * Démarrage de la détection de mouvement.
	 * @throws HomeServerException -
	 */
	public void startMotionDetection() throws HomeServerException {
		if (!this.isWebCamstarted()) {
			this.startWebCam();
		}
		
		this.stopMotionDetection();
		
		MotionListener ml = new MotionListener();
		this.setListener(ml);
		ml.start(this);
		
		this.motionDetectionStarted = true;
		
		log.info("[" + this.getId() + "] motion capture démarrée.");
	}
	
	/**
	 * Arrêt de la détection de mouvement.
	 * @throws HomeServerException -
	 */
	public void stopMotionDetection() throws HomeServerException {
		if (this.listener != null) {
			this.listener.stop();
		}
		
		this.motionDetectionStarted = false;
		log.info("[" + this.getId() + "] motion capture arrêtée.");
	}
	
	
	@JsonIgnore
	public byte[] getCapture() throws HomeServerException {
		Assert.isTrue(this.isWebCamstarted(), "webcam is not started");
		
		return WebcamUtils.getImageBytes(this.getPhysicalWebcam(), "PNG");
	}
	
}
