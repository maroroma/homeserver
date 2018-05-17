package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.seedbox.model.EpisodeParseResult;
import maroroma.homeserverng.seedbox.model.TargetDirectory;
import maroroma.homeserverng.tools.kodi.methods.GetSources;
import maroroma.homeserverng.tools.kodi.methods.KodiClient;
import maroroma.homeserverng.tools.kodi.methods.VideoLibraryScan;
import maroroma.homeserverng.tools.kodi.model.GetSourcesResponse;
import maroroma.homeserverng.tools.kodi.model.KodiSource;
import maroroma.homeserverng.tools.model.FileDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

    @Test
    public void testBidon() {
        GetSourcesResponse response = GetSources.create().build().execute(new KodiClient("http://192.168.0.51/jsonrpc"));
        System.out.println(response);

        System.out.println(VideoLibraryScan.create().kodiSource(KodiSource.builder().file("smb://MELCHIOR/SHARE/Videos/Series/").build())
                .withDialog()
                .build()
                .execute(new KodiClient("http://192.168.0.51/jsonrpc")));
    }


}