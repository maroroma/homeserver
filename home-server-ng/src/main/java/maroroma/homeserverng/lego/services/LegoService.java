package maroroma.homeserverng.lego.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.filemanager.services.FileManagerServiceImpl;
import maroroma.homeserverng.filemanager.services.FilesFactory;
import maroroma.homeserverng.lego.model.Brick;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.FileOperationResult;
import maroroma.homeserverng.tools.helpers.Predicates;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Gestion du catalog lego de la maison
 */
@Service
@RequiredArgsConstructor
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


    private final FileManagerServiceImpl fileManagerService;
    private final FilesFactory filesFactory;


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
        FileDirectoryDescriptor brickPicturesDirectory = this.filesFactory.directoryFromProperty(bricksImageLibraryDir);
        brickPicturesDirectory.mkdirs();

        FileDescriptor brickPictureFile = brickPicturesDirectory
                .combinePath(brickToUpdate.getId() + ".brickPicture")
                .asFile();

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
    }
}
