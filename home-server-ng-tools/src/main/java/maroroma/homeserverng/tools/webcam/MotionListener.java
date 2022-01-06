package maroroma.homeserverng.tools.webcam;

import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.util.Assert;

/**
 * Listener pour la détection de mouvement.
 * @author rlevexie
 *
 */
@Slf4j
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
