package maroroma.homeserverng.scanner.services;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.scanner.model.ScanRequest;
import maroroma.homeserverng.scanner.model.ScannerColorMode;
import maroroma.homeserverng.scanner.repositories.ScannerRepository;
import maroroma.homeserverng.tools.annotations.ProductionProfile;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.helpers.BashCmdExecutorHLP;
import maroroma.homeserverng.tools.helpers.CommonFileFilter;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;

/**
 * Implémentation du {@link ScannerService}.
 * @author rlevexie
 *
 */
@Service
@Log4j2
@ProductionProfile
public class ScannerServiceImpl implements ScannerService {

	// sudo scanimage -L
	// ---> device `hpaio:/usb/Deskjet_F4100_series?serial=CN7BE4S54D04TJ' is a Hewlett-Packard Deskjet_F4100_series all-in-one
	// sudo scanimage -d hpaio:/usb/Deskjet_F4100_series?serial=CN7BE4S54D04TJ --mode Gray > test.pnm
	// pnmtojpeg test.pnm > test.jpeg
	// penser à supprimer le npm.

	/**
	 * Repo pour les options.
	 */
	@Autowired
	private ScannerRepository scannerRepo;
	
	/**
	 * Permet d'émettre des events relatifs au status du scanner.
	 */
	@Autowired
	protected ScannerEventEmitter scannerEventEmitter;

	/**
	 * Emplacement des scans réalisés.
	 */
	@Property("homeserver.scanner.donescans.directory")
	private HomeServerPropertyHolder doneScansDirectory;

	/**
	 * Identifiant du scanner.
	 */
	@Property("homeserver.scanner.deviceid")
	private HomeServerPropertyHolder deviceID;

//	/**
//	 * Permet d'émettre les messages sur le topic.
//	 */
//	@Autowired
//	private MessageSendingOperations<String> messagingTemplate;

	/**
	 * Helper pour la configuration.
	 */
	//	@Autowired
	//	private ConfigurationHolder configurationHolder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FileDescriptor> listDoneScans() {

		// récupération des fichiers
		File[] listeFilesTodo = this.doneScansDirectory.asFile().listFiles(CommonFileFilter.pureFileFilter());

		return FileDescriptor.toList(listeFilesTodo);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteScan(final String fileName) {
		log.info("Demande de suppression du fichier [" 
				+ fileName + "] dans le répertoire [" 
				+ this.doneScansDirectory.getResolvedValue() + "]");

		File[] candidateFiles = this.doneScansDirectory.asFile().listFiles(CommonFileFilter.fileNameFilter(fileName));
		Assert.isTrue(candidateFiles.length == 1 && candidateFiles[0].exists(), "Fichier [" 
				+ fileName + "] dans le répertoire [" 
				+ this.doneScansDirectory.getResolvedValue() + "] non trouvé");

		candidateFiles[0].delete();
		log.info("Suppression ok du fichier [" + candidateFiles[0].getAbsolutePath() + "] ok");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ScannerColorMode> listColorModes() {
		return scannerRepo.getColorModes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateScanRequest(final ScanRequest request) {
		Assert.doesNotContain(request.getFileName(), " ", "Le nom de fichier ne peut contenir d'espace");
		Assert.doesNotContain(request.getFileName(), ".", "Le nom de fichier ne peut contenir de points");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Async
	public void scanWithOptions(final ScanRequest request) throws HomeServerException {


		// sudo scanimage -L
		// ---> device `hpaio:/usb/Deskjet_F4100_series?serial=CN7BE4S54D04TJ' is a Hewlett-Packard Deskjet_F4100_series all-in-one
		// sudo scanimage -d hpaio:/usb/Deskjet_F4100_series?serial=CN7BE4S54D04TJ --mode Gray > test.pnm
		// pnmtojpeg test.pnm > test.jpeg
		// penser à supprimer le npm.

		try {
			
			this.scannerEventEmitter.emitScanRequested();
			
			log.info("Démarrage d'un scan sur le device [" + this.deviceID + "]");
			log.info("Scan demandé : " + request.toString());


			// création du répertoire de sortie si inexistant
			File scanDoneDir = this.doneScansDirectory.asFile();
			if (!scanDoneDir.exists()) {
				scanDoneDir.mkdirs();
			}

			// commande de scan initiale
			//			String cmdScanFormat = "sudo scanimage -d %s --mode %s > %s";
			String cmdScanFormat = "scanimage -d %s --mode %s > %s";
			// fichier de sortie pnm
			File pnmFile = new File(this.doneScansDirectory.getResolvedValue(), request.getFileName() + ".pnm");
			String cmdScan = String.format(cmdScanFormat, this.deviceID.getResolvedValue(), request.getColorMode().getCode(), pnmFile.getAbsolutePath());
			log.info("Commande de scan à exécuter : [" + cmdScan + "]");
			
			this.scannerEventEmitter.emitScanRunning();
			BashCmdExecutorHLP.executeCommand(cmdScan);
			this.scannerEventEmitter.emitNPMReady();


			// commande de conversion au format jpeg
			String cmdConvertFormat = "pnmtojpeg %s > %s";
			File jpgFile = new File(this.doneScansDirectory.getResolvedValue(), request.getFileName() + ".jpeg");
			String cmdConvert = String.format(cmdConvertFormat, pnmFile.getAbsolutePath(), jpgFile.getAbsolutePath());
			log.info("Commande de conversion à exécuter : [" + cmdConvert + "]");
			this.scannerEventEmitter.emitConversionRunning();
			BashCmdExecutorHLP.executeCommand(cmdConvert);

			// suppression du fichier pnm
			log.info("Suppression du fichier [" + pnmFile.getAbsolutePath() + "]");
			pnmFile.delete();

			this.scannerEventEmitter.emitScanCompleted(jpgFile);


		} catch (Exception e) {
			log.error("Erreur inconnue rencontrée lors du scan", e);

			this.scannerEventEmitter.emitError(e);

//			throw new HomeServerException(e);
		}
	}

}
