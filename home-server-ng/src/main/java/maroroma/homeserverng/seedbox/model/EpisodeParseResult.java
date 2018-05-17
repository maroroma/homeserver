package maroroma.homeserverng.seedbox.model;


import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.model.FileDescriptor;
import maroroma.homeserverng.tools.model.FileToMoveDescriptor;
import maroroma.homeserverng.tools.model.MoveRequest;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collections;

/**
 * Container du résultat de parsing d'un fichier identifié comme un épisode de série tv.
 */
@Data
@Builder
public class EpisodeParseResult {

    /**
     * Emplacement du répertoire cible de la série.
     */
    private FileDescriptor tvShowDirectory;
    /**
     * Nom initial du fichier.
     */
    private String rawTvShowTitle;
    /**
     * Fichier à déplacer.
     */
    private FileDescriptor todoFile;
    /**
     * Saison de l'épisode.
     */
    private int season;
    /**
     * Numéro de l'épisode.
     */
    private int episode;
    /**
     * Répertoire cible.
     */
    private File targetDir;
    /**
     * Parse ok.
     */
    private boolean success;

    /**
     * Retourne un parse result en erreur.
     * @return
     */
    public static EpisodeParseResult failed() {
        return EpisodeParseResult.builder().success(false).build();
    }

    /**
     * Retourne une base de builder en success.
     * @return
     */
    public static EpisodeParseResultBuilder success() {
        return EpisodeParseResult.builder().success(true);
    }

    /**
     * Création d'une {@link MoveRequest} à partir du résultat du parsing d'un fichier.
     * @return -
     */
    public MoveRequest generateMoveRequest() {
        MoveRequest returnValue = new MoveRequest();
        File targetDirectory = new File(this.tvShowDirectory.createFile(), String.format("Season %02d", this.getSeason()));
        returnValue.setTarget(new FileDescriptor(targetDirectory));

        FileToMoveDescriptor fileToMoveDescriptor = new FileToMoveDescriptor();
        fileToMoveDescriptor.setFullName(this.todoFile.getFullName());
        fileToMoveDescriptor.setName(this.todoFile.getName());
        fileToMoveDescriptor.setNewName(String.format("%s.S%02dE%02d.%s",
                this.tvShowDirectory.getName(),
                this.getSeason(),
                this.getEpisode(),
                FilenameUtils.getExtension(this.todoFile.getName())));

        returnValue.setFilesToMove(Collections.singletonList(fileToMoveDescriptor));

        return returnValue;
    }

}
