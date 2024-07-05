package maroroma.homemusicplayer.services.mp3.tags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class Mp3TagReader {

    private final Id3TagParser id3TagParser;
    private final FileNameTagParser fileNameTagParser;


    public Map<Mp3Tags, TagReadingResult> extractTags(FileAdapter aMusicFile) {
       if (aMusicFile.isLowPerformanceFile()) {
           return fileNameTagParser.extractTags(aMusicFile);
       } else {
           return id3TagParser.extractTags(aMusicFile);
       }
    }

    @RequiredArgsConstructor
    public enum Mp3Tags {
        TITLE("title"),
        TRACK_NUMBER("trackNumber");

        @Getter
        private final String tagSearch;

        public TagReadingResult result(String value) {
            return new TagReadingResult(this, value);
        }

        public static Optional<TagReadingResult> resolve(Metadata metadata, String metadataTagName) {

            return Stream.of(Mp3Tags.values()).filter(aTag -> StringUtils.containsIgnoreCase(metadataTagName, aTag.getTagSearch()))
                    .findFirst()
                    .map(matchingMp3Tag -> new TagReadingResult(matchingMp3Tag, metadata.get(metadataTagName)));
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static class TagReadingResult {
        private final Mp3Tags type;
        private final String value;
    }
}
