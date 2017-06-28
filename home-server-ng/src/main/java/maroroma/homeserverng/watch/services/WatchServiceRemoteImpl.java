package maroroma.homeserverng.watch.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.webcam.Webcam;
import maroroma.homeserverng.watch.tools.WebCamClient;

@Service
public class WatchServiceRemoteImpl implements WatchService {

	
	@Property("homeserver.watch.remoteagents.urls")
	HomeServerPropertyHolder webCamAgentUrls;
	
	List<WebCamClient> remoteClients;
	
	@PostConstruct
	private void initMethod() {
		this.remoteClients = new ArrayList<>();
		this.webCamAgentUrls.asStringList().forEach(url -> {
			this.remoteClients.add(new WebCamClient(url));
		});
		
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
		List<Webcam> returnValue = new ArrayList<>();
		
		this.remoteClients.parallelStream().forEach(client -> {
			try {
				returnValue.addAll(client.getAllAvailableWebcams());
			} catch (HomeServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		return returnValue;
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
