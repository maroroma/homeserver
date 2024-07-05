package maroroma.homemusicplayer.services.mp3.tags;

import maroroma.homemusicplayer.model.files.FileAdapter;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;


@Component
public class Id3TagParser implements TagParser {
    @Override
    public Map<Mp3TagReader.Mp3Tags, Mp3TagReader.TagReadingResult> extractTags(FileAdapter aMusicFile) {
        var inputStream = aMusicFile.getInputStream();
        var handler = new DefaultHandler();
        var metadata = new Metadata();
        var parser = new Mp3Parser();
        var parseContext = new ParseContext();
        try {
            parser.parse(inputStream, handler, metadata, parseContext);
            inputStream.close();
        } catch (IOException | SAXException | TikaException e) {
            // DO NOTHING
            return Collections.emptyMap();
        }

        return Stream.of(metadata.names())
                .map(aMetadataTagNAme -> Mp3TagReader.Mp3Tags.resolve(metadata, aMetadataTagNAme))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Mp3TagReader.TagReadingResult::getType, Function.identity()));
    }
}
