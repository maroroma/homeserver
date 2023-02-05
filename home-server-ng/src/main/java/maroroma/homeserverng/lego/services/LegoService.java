package maroroma.homeserverng.lego.services;

import maroroma.homeserverng.filemanager.services.FileManagerServiceImpl;
import maroroma.homeserverng.lego.model.Brick;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDescriptorFactory;
import maroroma.homeserverng.tools.files.FileOperationResult;
import maroroma.homeserverng.tools.helpers.Predicates;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Gestion du catalog lego de la maison
 */
@Service
public class LegoService {


    /**
     * Repository pour la gestion des briques
     */
    @InjectNanoRepository(
            file = @Property("homeserver.lego.bricks.store"),
            persistedType = Brick.class)
    private NanoRepository bricksRepo;

    @Property("homeserver.lego.bricks.image.library")
    private HomeServerPropertyHolder bricksImageLibraryDir;

    @Autowired
    private SecurityManager securityManager;

    @Autowired
    private FileManagerServiceImpl fileManagerService;


    public List<Brick> getAllBricks() {
        return this.bricksRepo.getAll();
    }

    public List<Brick> updateBricks(List<Brick> updatedBricks) {

        // téléchargement des miniatures si modifiées
        updatedBricks.stream()
                .filter(oneBrickToUpdate -> StringUtils.hasLength(oneBrickToUpdate.getPictureUrl()))
                .map(oneBrickToUpdate -> Tuple.from(this.bricksRepo.<Brick>findByIdMandatory(oneBrickToUpdate.getId()), oneBrickToUpdate))
                .filter(oneTuple -> !oneTuple.getItem2().getPictureUrl().equals(oneTuple.getItem1().getPictureUrl()))
                .map(Tuple::getItem2)
                .forEach(this::dowloadBrickPictureAndUpdateBrick);


        updatedBricks.forEach(oneBrickToUpdate -> Traper.trap(() -> this.bricksRepo.update(oneBrickToUpdate)));

        return this.bricksRepo.getAll();
    }

    public List<Brick> createBrick(Brick newBrick) throws HomeServerException {

        // génération de l'id unique pour la nouvelle brique
        newBrick.setId(UUID.randomUUID().toString());

        this.dowloadBrickPictureAndUpdateBrick(newBrick);

        return this.bricksRepo.save(newBrick);
    }

    /**
     * Télécharge une image pour une brique si une url est renseignée
     */
    private Brick dowloadBrickPictureAndUpdateBrick(Brick brickToUpdate) {
        // dans le doute on créé l'arbo
        FileDescriptor brickPicturesDirectory = bricksImageLibraryDir.asFileDescriptorFactory()
                .withSecurityManager(this.securityManager).fileDescriptor();
        brickPicturesDirectory.mkdirs();

        FileDescriptor brickPictureFile = FileDescriptorFactory
                .fromPath(brickPicturesDirectory.getFullName())
                .withSecurityManager(this.securityManager)
                .combinePath(brickToUpdate.getId() + ".brickPicture")
                .fileDescriptor();

        // si url image renseignée, download
        Optional.ofNullable(brickToUpdate.getPictureUrl())
                .filter(Predicates.not(String::isEmpty))
                .map(Traper.trapAndMap(URL::new))
                .map(Traper.trapAndMap(URL::openStream))
                .map(brickPictureFile::copyFrom)
                .filter(FileOperationResult::isCompleted)
                .map(FileOperationResult::getInitialFile)
                .map(FileDescriptor::getId)
                // si download ok
                .ifPresent(brickToUpdate::setPictureFileId);

        return brickToUpdate;

    }

    public List<Brick> deleteBrick(String brickId) throws HomeServerException {
        return this.bricksRepo.delete(brickId);
    }


    /**
     * Permet de télécharger l'image d'une brique lego pour un id de brique donné
     * @param brickId identifiant de la brique
     * @param response response http pour écrire le flux final
     */
    public void getBrickPicture(String brickId, HttpServletResponse response) {

        this.fileManagerService.getFile(() -> this.bricksRepo
                .<Brick>findById(brickId)
                .map(Brick::getPictureFileId), response);

//        // récupération du fichier
//        FileDescriptor toDownload = this.bricksRepo
//                .<Brick>findById(brickId)
//                .map(Brick::getPictureFileId)
//                .map(FileDescriptorFactory::fromId)
//                .map(fileDescriptorFactory -> fileDescriptorFactory.withSecurityManager(securityManager))
//                .map(FileDescriptorFactory::fileDescriptor)
//                .orElseThrow(() -> new RuntimeHomeServerException("impossible de récupérer le picto pour la brique"));
//
//
//        if (toDownload.getSize() > 0) {
//            response.setHeader("Content-Length", "" + toDownload.getSize());
//        }
//
//        toDownload.copyTo(Traper.trap(response::getOutputStream));
    }
}
