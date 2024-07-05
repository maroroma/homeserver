package maroroma.homemusicplayer.services.mp3.tags;

import maroroma.homemusicplayer.model.files.FileAdapter;

import java.util.*;

public interface TagParser {
    Map<Mp3TagReader.Mp3Tags, Mp3TagReader.TagReadingResult> extractTags(FileAdapter aMusicFile);
}
