package maroroma.homeserverng.scanner.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.scanner.exceptions.SseEmiterException;
import maroroma.homeserverng.scanner.model.ScannerEvent;
import maroroma.homeserverng.scanner.model.ScannerEventType;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.sse.ManagedSseEmitter;
import maroroma.homeserverng.tools.sse.ManagedSseEmitterCollection;

/**
 * Implémentation de {@link ScannerEventEmitter}.
 * @author RLEVEXIE
 *
 */
@Log4j2
@Service
public class ScannerEventEmitterImpl implements ScannerEventEmitter {

	/**
	 * Gestion de la liste de clients.
	 */
	@Autowired
	private ManagedSseEmitterCollection emitters;
	
	@Override
	public SseEmitter createEmitter(final String identifiant) throws HomeServerException {
		try {
			return emitters.createOrReplace(identifiant).await().getInnerEmitter();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode interne pour l'émission d'un event spécifique.
	 * @param eventBuilder -
	 * @throws SseEmiterException -
	 */
	private void emit(final ScannerEvent.ScannerEventBuilder eventBuilder) throws SseEmiterException {

		Assert.notNull(eventBuilder);

		// insertion d'un id unique
		ScannerEvent eventToEmit = eventBuilder.id("" + System.nanoTime()).build();

		// pour la sérialisation des données émises.
		ObjectMapper mapper = new ObjectMapper();

		SseEventBuilder sseEventBuilder = null;
		try {
			sseEventBuilder = SseEmitter
					.event()
					.name(eventToEmit.getEventType().getName())
					.data(mapper.writeValueAsString(eventToEmit), MediaType.APPLICATION_JSON)
					.id(eventToEmit.getId());

			for (ManagedSseEmitter sseEmitter : emitters.getEmitters()) {
				log.info("émission event  [" + eventToEmit + "]; emiter : " + sseEmitter);
				sseEmitter.send(sseEventBuilder);
			}


		} catch (JsonProcessingException e) {
			throw new SseEmiterException("Erreur survenue lors de la sérialisation de l'event à émettre", e);
		} catch (IOException e) {
			throw new SseEmiterException("Erreur survenue lors de l'émission de l'event", e);
		}
	}

	@Override
	public void emitScanRequested() throws SseEmiterException {

		this.emit(ScannerEvent.builder().eventType(ScannerEventType.SCAN_REQUESTED).message("Demande de scan reçue"));

	}

	@Override
	public void emitScanRunning() throws SseEmiterException {
		this.emit(ScannerEvent.builder().eventType(ScannerEventType.SCAN_RUNNING).message("Scan en cours"));
	}

	@Override
	public void emitNPMReady() throws SseEmiterException {
		this.emit(ScannerEvent.builder().eventType(ScannerEventType.SCAN_PNM_READY).message("Image pnm générée"));
	}

	@Override
	public void emitConversionRunning() throws SseEmiterException {
		this.emit(ScannerEvent.builder().eventType(ScannerEventType.SCAN_CONVERSION_RUNNING).message("Image en cours de conversion"));

	}

	@Override
	public void emitScanCompleted(final File jpgFile) throws SseEmiterException {
		this.emit(ScannerEvent.builder().eventType(ScannerEventType.SCAN_TERMINATED).message("Scan terminé : " + jpgFile.getAbsolutePath()));

	}

	@Override
	public void emitError(final Exception e) throws SseEmiterException {
		this.emit(ScannerEvent.builder().eventType(ScannerEventType.SCAN_IN_ERROR).message("Erreur rencontrée lors du scan " + e.getMessage()));

	}

}
