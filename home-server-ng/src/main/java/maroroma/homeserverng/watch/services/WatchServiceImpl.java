package maroroma.homeserverng.watch.services;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Service
@Deprecated
public class WatchServiceImpl {// implements WatchService {

//	@InjectNanoRepository(file = @Property("homeserver.watch.cameras.store"), 
//			idField = "id", persistedType = Webcam.class)
//	private NanoRepository cameraRepository;
//
//	@Autowired
//	private WebcamStreamService streamService;
//
//
//	@Override
//	public List<Webcam> getAllWebcams() throws HomeServerException {
//		return this.cameraRepository.getAll();
//	}
//
//	@Override
//	public List<Webcam> saveWebCam(final Webcam newWebCam) throws HomeServerException {
//		return this.cameraRepository.save(newWebCam);
//	}
//
//	@Override
//	public List<Webcam> deleteWebCam(final String id) throws HomeServerException {
//		Webcam toDelete = this.cameraRepository.find(id);
//		toDelete.stopWebCam();
//		return this.cameraRepository.delete(id);
//	}
//
//	@Override
//	public List<Webcam> getAllAvailableWebcams() throws HomeServerException {
//		List<Webcam> returnValue = new ArrayList<>();
//
//		List<com.github.sarxos.webcam.Webcam> webcams = com.github.sarxos.webcam.Webcam.getWebcams();
//
//		webcams.forEach(physicalwebcam -> returnValue.add(WebcamConverter.convertWebcam(physicalwebcam)));
//
//		return returnValue;
//	}
//
//	@PostConstruct
//	private void initDeclaredWebcams() throws HomeServerException {
//		List<Webcam> declaredWebcams = this.getAllWebcams();
//
//		validateCameras(declaredWebcams);
//
//		log.info(declaredWebcams.size() + " webcams déclarées");
//
//		declaredWebcams.forEach(declaredWebcam -> {
//			try {
//				declaredWebcam.initializeFromParameters();
//				if (!declaredWebcam.isWebCamstarted()) {
//					this.streamService.stopStreamForWeb(declaredWebcam);
//				}
//			} catch (Exception e) {
//				log.warn("Erreur rencontrée de l'init de la webcam [" + declaredWebcam.getId() + "]. Désactivation.", e);
//				declaredWebcam.setWebCamstarted(false);
//				declaredWebcam.setMotionDetectionStarted(false);
//			}
//		});
//	}
//
//
//	private void validateCameras(List<Webcam> declaredWebcams) {
//
//		for (Webcam webcam : declaredWebcams) {
//			Assert.notEmpty(webcam.getViewPorts(), "camera must have viewports");
//			Assert.notNull(webcam.getSelectedViewPort(), "camera must have a selected viewport");
//			Assert.hasLength(webcam.getId(), "camera must have an id");
//			if (webcam.isMotionDetectionStarted()) {
//				Assert.isTrue(webcam.isWebCamstarted() , "motion can't be activated if camera is not started");
//			}
//		}
//
//	}
//
//	@Override
//	public byte[] getCaptureFromWebcam(final String id) throws HomeServerException {
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		try {
//			Webcam declaredWebCam = this.cameraRepository.find(id);
//			com.github.sarxos.webcam.Webcam physicalWebcam = PhysicalWebCamHLP.openWebCam(declaredWebCam, false);
//
//			BufferedImage image = physicalWebcam.getImage();
//
//			// save image to PNG file
//			ImageIO.write(image, "PNG", baos); 
//
//		} catch (Exception e) {
//			log.error("error in capture for device " + id, e);
//			throw new HomeServerException("Erreur rencontrée lors de la capture pour la caméra " + id, e);
//		}
//
//		return baos.toByteArray();
//	}
//
//
//	@Override
//	public List<Webcam> updateWebcam(Webcam newWebCam) throws HomeServerException {
//
//		Webcam oldWebcam = this.cameraRepository.find(newWebCam.getId());
//
//		// permet d'appliquer le changement de résolution en forçant l'arrêt de la caméra
//		if (oldWebcam.getSelectedViewPort().getWidth() != newWebCam.getSelectedViewPort().getWidth()
//				&& oldWebcam.getSelectedViewPort().getHeight() != newWebCam.getSelectedViewPort().getHeight() && oldWebcam.isWebCamstarted()) {
//			oldWebcam.stopWebCam();
//		}
//
//		List<Webcam> returnValue = this.cameraRepository.update(newWebCam);
//		this.initDeclaredWebcams();
//		return returnValue;
//	}
//
//	@Override
//	public SseEmitter subscribeToStream(String idSubcriber, String idWebcam) throws HomeServerException {
//
//		log.info("Demande d'abonnement par le client [" + idSubcriber + "] à la webcam [" + idWebcam + "]" );
//		Webcam webcam = this.cameraRepository.find(idWebcam);
//
//		return this.streamService.startStreamForWebcam(idSubcriber, webcam);
//	}
//
//	@Override
//	public boolean unsubscribeToStream(String idSubscriber, String idWebcam) throws HomeServerException {
//		log.info("Demande de désabonnement par le client [" + idSubscriber + "] à la webcam [" + idWebcam + "]" );
//		
//		this.streamService.stopStreamForWeb(idSubscriber,this.cameraRepository.find(idWebcam));
//		return true;
//	}

}
