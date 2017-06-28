package maroroma.homeserverng.tools.webcam;

import java.awt.Dimension;
import java.util.List;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;

public abstract class PhysicalWebCamHLP {

	public static com.github.sarxos.webcam.Webcam getPhysicalWebcam(final String id) throws HomeServerException {
		List<com.github.sarxos.webcam.Webcam> webcams = com.github.sarxos.webcam.Webcam.getWebcams();
		for (com.github.sarxos.webcam.Webcam physicalWebcam : webcams) {
			if (physicalWebcam.getName().equals(id)) {
				return physicalWebcam;
			}
		}
		
		throw new HomeServerException("Can't find physical web cam " + id);
	}
	
	public static com.github.sarxos.webcam.Webcam getPhysicalWebcam(final Webcam logicalWebCam) throws HomeServerException {
		Assert.notNull(logicalWebCam, "logicalWebCam can't be null");
		Assert.hasLength(logicalWebCam.getId(), "logicalWebCam.id can't be null or empty");
		
		return PhysicalWebCamHLP.getPhysicalWebcam(logicalWebCam.getId());
	}
	
	
	public static com.github.sarxos.webcam.Webcam openWebCam(final Webcam logicalWebCam, boolean async) throws HomeServerException {
		Assert.notNull(logicalWebCam, "logicalWebCam can't be null");
		Assert.hasLength(logicalWebCam.getId(), "logicalWebCam.id can't be null or empty");
		Assert.notNull(logicalWebCam.getSelectedViewPort(), "logicalWebCam.viewPort can't be null");
		return PhysicalWebCamHLP.openWebCam(logicalWebCam.getId(), WebcamConverter.convertDimension(logicalWebCam.getSelectedViewPort()), async);
	}
	
	public static com.github.sarxos.webcam.Webcam openWebCam(final String id, Dimension dimension, boolean async) throws HomeServerException {
		Assert.hasLength(id, "id can't be null or empty");
		com.github.sarxos.webcam.Webcam physicalWC = PhysicalWebCamHLP.getPhysicalWebcam(id);
		return PhysicalWebCamHLP.openWebCam(physicalWC, dimension, async);
	}

	public static com.github.sarxos.webcam.Webcam closeWebCam(final com.github.sarxos.webcam.Webcam physicalWC) {
		Assert.notNull(physicalWC, "physicalWC can't be null or empty");
		
		physicalWC.close();
		
		return physicalWC;
	}
	
	public static com.github.sarxos.webcam.Webcam openWebCam(final com.github.sarxos.webcam.Webcam physicalWC, Dimension dimension, boolean async) throws HomeServerException {
		Assert.notNull(physicalWC, "physicalWC can't be null or empty");
		
		if ((physicalWC.isOpen() && !physicalWC.getViewSize().equals(dimension)) || !physicalWC.isOpen()) {
			physicalWC.close();
			physicalWC.setViewSize(dimension);
			physicalWC.open(async);
		}
		
		return physicalWC;
	}
	
}
