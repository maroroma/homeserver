package maroroma.homeserverng.watch.tools;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.webcam.Webcam;
import maroroma.homeserverng.watch.services.WatchService;

public class WebCamClient implements WatchService {

	RestTemplate client;
	String url;

	public WebCamClient(String url) {

		this.client = new RestTemplate();
		this.url = url;
	}


	@Override
	public List<Webcam> getAllWebcams() throws HomeServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Webcam> saveWebCam(Webcam newWebCam) throws HomeServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Webcam> deleteWebCam(String id) throws HomeServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Webcam> getAllAvailableWebcams() throws HomeServerException {
		ResponseEntity<Webcam[]> returned = client.getForEntity(this.url + "/watch/availablewebcams", Webcam[].class);
		return Arrays.asList(returned.getBody());
	}

	@Override
	public byte[] getCaptureFromWebcam(String id) throws HomeServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Webcam> updateWebcam(Webcam newWebCam) throws HomeServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SseEmitter subscribeToStream(String idSubcriber, String idWebcam) throws HomeServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean unsubscribeToStream(String idSubscriber, String idWebcam) throws HomeServerException {
		// TODO Auto-generated method stub
		return false;
	}


}
