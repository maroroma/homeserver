package homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.EpisodeParseResult;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.seedbox.services.EpisodeParser;
import maroroma.homeserverng.seedbox.services.TargetDirectoryTvShowLoader;
import maroroma.homeserverng.tools.files.FileDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class EpisodeParserTest {

    @Mock
    TargetDirectoryTvShowLoader targetDirectoryTvShowLoader;

    @InjectMocks
    EpisodeParser episodeParser;


    @Test
    public void isTvShowEpisode() {
        FileDescriptor fileDescriptor = new FileDescriptor(new File("[ www.CpasBien.io ] Brooklyn.Nine-Nine.S03E09.FASTSUB.VOSTFR.HDTV.XviD-ZT.avi"));
        assertThat(episodeParser.isTvShowEpisode(fileDescriptor)).isTrue();

    }

    @Test
    public void parseFile_shouldParseEpisodeWithDot() {


        FileDescriptor fileDescriptor = new FileDescriptor(new File("[ www.CpasBien.io ] Brooklyn.Nine-Nine.S03E09.FASTSUB.VOSTFR.HDTV.XviD-ZT.avi"));

        TargetDirectory targetDirectory = new TargetDirectory(new File("bidon"), null);
        targetDirectory.setSubDirectories(Collections.singletonList(new FileDescriptor(new File("Brooklyn Nine Nine"))));


        given(this.targetDirectoryTvShowLoader.loadTargetDirectory()).willReturn(targetDirectory);


        EpisodeParseResult episodeParseResult = episodeParser.parseFile(fileDescriptor);

        assertThat(episodeParseResult)
                .extracting(r -> r.getTvShowDirectory().getName(), EpisodeParseResult::getSeason, EpisodeParseResult::getEpisode)
                .containsExactly(targetDirectory.getSubDirectories().get(0).getName(), 3, 9);

        then(this.targetDirectoryTvShowLoader).should().loadTargetDirectory();
    }

    @Test
    public void parseFile_shouldParseEpisodeWithBlank() {
        // The Handmaids Tale S01E01.mp4

        FileDescriptor fileDescriptor = new FileDescriptor(new File("The Handmaids Tale S01E01.mp4"));

        TargetDirectory targetDirectory = new TargetDirectory(new File("bidon"), null);
        targetDirectory.setSubDirectories(Collections.singletonList(new FileDescriptor(new File("The Handmaids Tale"))));


        given(this.targetDirectoryTvShowLoader.loadTargetDirectory()).willReturn(targetDirectory);


        EpisodeParseResult episodeParseResult = episodeParser.parseFile(fileDescriptor);

        assertThat(episodeParseResult)
                .extracting(r -> r.getTvShowDirectory().getName(), EpisodeParseResult::getSeason, EpisodeParseResult::getEpisode)
                .containsExactly("The Handmaids Tale", 1, 1);

    }




}