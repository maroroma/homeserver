package maroroma.homeserverng.scanner.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.scanner.model.ScanRequest;
import maroroma.homeserverng.tools.annotations.DevProfile;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.files.FileDescriptor;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Classe pour le dev et le test des events sur le scanner.
 * 
 * @author RLEVEXIE
 *
 */
@Service
@Log4j2
@DevProfile
@Primary
// FIXME : supprimer la gestion du scanner
public class ScannerServiceDevImpl extends ScannerServiceImpl {

	@Async
	@Override
	public void scanWithOptions(final ScanRequest request) throws HomeServerException {
		try {

			this.scannerEventEmitter.emitScanRequested();
			Thread.sleep(1000);

			this.scannerEventEmitter.emitScanRunning();
			Thread.sleep(1000);
			
			if(request.getFileName().contains("error")) {
				throw new Exception("TU EXCEPTION");
			}

			this.scannerEventEmitter.emitNPMReady();
			Thread.sleep(1000);

			this.scannerEventEmitter.emitConversionRunning();
			Thread.sleep(1000);

			// suppression du fichier pnm

			List<FileDescriptor> testReturn = this.listDoneScans();

			this.scannerEventEmitter
				.emitScanCompleted(testReturn.isEmpty() ? new File("") : testReturn.get(0).createFile());

		} catch (Exception e) {

			try {
				this.scannerEventEmitter.emitError(e);
			} catch (Exception ex) {
				log.warn("Erreur non diffusable", ex);
			}

		}
	}

}
