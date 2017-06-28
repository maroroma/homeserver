package maroroma.homeserverng.tools.webcam;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class WebcamConverter {

	private static final int DEFAULT_WATCH_FREQUENCY = 500;

	public static Webcam convertWebcam(final String agentId, final com.github.sarxos.webcam.Webcam physicalWebcam) {
		
		List<Viewport> viewPorts = WebcamConverter.convertViewPorts(Arrays.asList(physicalWebcam.getViewSizes()));
		
		return Webcam.builder()
				.id(physicalWebcam.getName())
				.agentId(agentId)
				.viewPorts(viewPorts)
				// par défaut la premiere dimension trouvée
				.selectedViewPort(viewPorts.get(0))
				// sur une fréquence de 500ms
				.frequency(DEFAULT_WATCH_FREQUENCY)
				// par défaut la webcam n'est pas activée.
				.motionDetectionStarted(false)
				.webCamstarted(false)
				.build();
	}
	
	public static List<Webcam> convertWebcams(final String agentId, final List<com.github.sarxos.webcam.Webcam> physicalWebcams) {
		List<Webcam> returnValue = new ArrayList<>();
		
		physicalWebcams.forEach(pwebcam -> returnValue.add(WebcamConverter.convertWebcam(agentId, pwebcam)));
		
		return returnValue;
	}
	
	public static Dimension convertDimension(final Viewport viewPort) {
		return new Dimension((int)viewPort.getWidth(),(int) viewPort.getHeight());
	}
	
	public static Viewport convertViewPort(final Dimension dimension) {
		return Viewport.builder().height(dimension.getHeight()).width(dimension.getWidth()).build();
	}
	
	public static List<Viewport> convertViewPorts(final List<Dimension> dimensions) {
		List<Viewport>  returnValue = new ArrayList<>();
		
		dimensions.forEach(dimension -> returnValue.add(WebcamConverter.convertViewPort(dimension)));
		
		return returnValue;
	}
	
	
}
