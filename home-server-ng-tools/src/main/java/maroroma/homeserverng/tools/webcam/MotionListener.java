package maroroma.homeserverng.tools.webcam;

import org.springframework.util.Assert;

import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Listener pour la détection de mouvement.
 * @author rlevexie
 *
 */
@Log4j2
@NoArgsConstructor
public class MotionListener implements WebcamMotionListener {

	private WebcamMotionDetector detector;
	private Webcam logicalWebcam;
	
	public void start(final Webcam logicalWebcam) throws HomeServerException {
		Assert.notNull(logicalWebcam, "logicalWebcam can't be null");
		this.logicalWebcam = logicalWebcam;
		this.detector = new WebcamMotionDetector(logicalWebcam.getPhysicalWebcam());
		this.detector.setInterval(logicalWebcam.getFrequency());
		this.detector.addMotionListener(this);
		this.detector.start();
	}
	
	public void stop() {
		if (this.detector != null) {
			this.detector.stop();
		}
	}
	
	@Override
	public void motionDetected(final WebcamMotionEvent wme) {
		log.info("MOUVEMENT DETECTE !!!!!! sur [" + this.logicalWebcam + "]");
	}

}
