package maroroma.homeserverng.watch.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.sse.ManagedSseEmitterCollection;
import maroroma.homeserverng.tools.webcam.Webcam;
import maroroma.homeserverng.watch.model.WebcamEmitterKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebcamStreamService {

	@Autowired
	ManagedSseEmitterCollection sseEmitters;

	private SseEmitter createEmitter(String id) throws HomeServerException {
		try {
			return this.sseEmitters.createOrReplace(id).await().getInnerEmitter();
		} catch (InterruptedException e) {
			throw new HomeServerException(e);
		}
	}

	Map<String, Webcam> webcamStarted = new ConcurrentHashMap<>();

	public SseEmitter startStreamForWebcam(String subcriberId, Webcam webcam) throws HomeServerException {
		if (!this.webcamStarted.containsKey(webcam.getId())) {
			this.webcamStarted.put(webcam.getId(), webcam);
		}
		return this.createEmitter(new WebcamEmitterKey(webcam.getId(), subcriberId).toString());
	}

	public void stopStreamForWeb(String subscriberId, Webcam webcam) {
		this.sseEmitters.removeEmitter(new WebcamEmitterKey(webcam.getId(), subscriberId).toString());
	}
	
	public void stopStreamForWeb(Webcam webcam) {
		
		log.info("Arrêt de tous les streamings pour la webcam " + webcam);
		
		List<String> idEmitterToRemove = new ArrayList<>();
		this.sseEmitters.getEmitters().forEach(emitter -> {
			WebcamEmitterKey key = WebcamEmitterKey.fromString(emitter.getId());
			if (key.getWebcamId().equals(webcam.getId())) {
				idEmitterToRemove.add(key.toString());
			}
		});
		
		idEmitterToRemove.forEach(key -> {
			this.sseEmitters.removeEmitter(key);
		});
	}

	@Scheduled(fixedDelay = 500)
	public void scheduledStreaming() throws HomeServerException {

		List<String> webcamsToRemove = new ArrayList<>();

		for (Webcam webcam : webcamStarted.values()) {
			

			if (sseEmitters.getEmitters().isEmpty()) {
				webcamsToRemove.add(webcam.getId());
			} else {

				byte[] capture = webcam.getCapture();

				log.debug("capture prise");
				
				sseEmitters.getEmitters().forEach(emiter -> {

					// on émet sur un emitter que si la clef correspond à la webcam donnée.
					if (WebcamEmitterKey.fromString(emiter.getId()).getWebcamId().equals(webcam.getId())) {
						try {
							emiter.send(SseEmitter
									.event()
									.name("newFrame")
									.id(webcam.getId())
									.data(Base64Utils.encodeToString(capture), MediaType.IMAGE_PNG));
						} catch (IOException e) {
							log.warn("erreur rencontrée lors de l'émission d'une capture", e);
						}
					}
				});
			}
		}


		webcamsToRemove.forEach(webcamid -> this.webcamStarted.remove(webcamid));
	}

}
