package maroroma.homemusicplayer.services.mp3.tags;

import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

@Component
public class FileNameTagParser implements TagParser{

    private static final String TRACK_REGEX = "^(\\d*) - (.+)\\.mp3";

    @Override
    public Map<Mp3TagReader.Mp3Tags, Mp3TagReader.TagReadingResult> extractTags(FileAdapter aMusicFile) {
        Matcher matcher = Pattern.compile(TRACK_REGEX).matcher(aMusicFile.getFileName());

        List<Mp3TagReader.TagReadingResult> results = new ArrayList<>();

        if (matcher.find()) {
            results.add(Mp3TagReader.Mp3Tags.TRACK_NUMBER.result(matcher.group(1)));
            results.add(Mp3TagReader.Mp3Tags.TITLE.result(matcher.group(2)));
        }

        return StreamUtils.of(results).collect(Collectors.toMap(Mp3TagReader.TagReadingResult::getType, Function.identity()));
    }
}
