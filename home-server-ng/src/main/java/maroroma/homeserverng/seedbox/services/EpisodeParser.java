package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.EpisodeParseResult;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Permet de parser un fichier pour en extraire automatiquement un répertoire cible.
 */
@Component
public class EpisodeParser {

    /**
     * Extraction du titre de la série, de la saison et de l'épisode.
     */
    private static final String REGEX_EPISODE_TITLE_AND_NUMBER = "^(.+)[\\.\\s][Ss](\\d{2})[Ee](\\d{2})";

    /**
     * Détection d'un numéro d'épisode.
     */
    private static final String REGEX_EPISODE_NUMBER = "[Ss](\\d{2})[Ee](\\d{2})";

    @Autowired
    private TargetDirectoryTvShowLoader targetDirectoryTvShowLoader;

    /**
     * Détermine si le fichier est un épisode de série
     *
     * @param fileToParse -
     * @return -
     */
    public boolean isTvShowEpisode(final FileDescriptor fileToParse) {
        return Pattern.compile(EpisodeParser.REGEX_EPISODE_NUMBER).matcher(fileToParse.getName()).find();
    }

    /**
     * Parse du fichier et extraction
     * @param fileToParse -
     * @return -
     */
    public EpisodeParseResult parseFile(final FileDescriptor fileToParse) {
        Matcher matcher = Pattern.compile(REGEX_EPISODE_TITLE_AND_NUMBER).matcher(fileToParse.getName());

        if (matcher.find()) {

            String rawTvShowTitle = matcher.group(1);

            // on récupère la liste des séries tv
            return this.targetDirectoryTvShowLoader
                    .loadTargetDirectory()
                    .getSubDirectories()
                    .stream()
                    // on ne garde que les répertoires dont chaque mot est présent dans le nom de la série que la regex à remonter
                    .filter(oneDirectory -> Stream.of(oneDirectory.getName().split(" "))
                            .allMatch(oneWord -> rawTvShowTitle.toLowerCase().contains(oneWord.toLowerCase())))
                    .findAny()
                    // si un résultat est trouvé, on transforme et retourne le résultat du parsing
                    .map(oneCandidate -> EpisodeParseResult
                            .success()
                            .todoFile(fileToParse)
                            .rawTvShowTitle(rawTvShowTitle)
                            .tvShowDirectory(oneCandidate)
                            .season(Integer.parseInt(matcher.group(2)))
                            .episode(Integer.parseInt(matcher.group(3)))
                            .build())
                    // si aucun résultat on se casse
                    .orElse(EpisodeParseResult.failed());

        }

        return EpisodeParseResult.failed();
    }


}
