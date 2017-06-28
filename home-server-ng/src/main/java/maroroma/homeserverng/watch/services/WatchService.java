package maroroma.homeserverng.watch.services;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.webcam.Webcam;

public interface WatchService {

	List<Webcam> getAllWebcams() throws HomeServerException;

	List<Webcam> saveWebCam(Webcam newWebCam) throws HomeServerException;

	List<Webcam> deleteWebCam(String id) throws HomeServerException;

	List<Webcam> getAllAvailableWebcams() throws HomeServerException;

	byte[] getCaptureFromWebcam(String id) throws HomeServerException;

	List<Webcam>  updateWebcam(Webcam newWebCam) throws HomeServerException;
	
	SseEmitter subscribeToStream(String idSubcriber, String idWebcam) throws HomeServerException;

	boolean unsubscribeToStream(String idSubscriber, String idWebcam) throws HomeServerException;
	
	
	
}
