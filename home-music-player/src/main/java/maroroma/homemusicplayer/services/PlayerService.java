package maroroma.homemusicplayer.services;

import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.player.api.AddAlbumToPlayListRequest;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;

import java.util.*;

public interface PlayerService {

    void play(CreatePlayerRequest createPlayerRequest);

    void addAlbumToPlayList(AddAlbumToPlayListRequest addAlbumToPlayListRequest);

    void stop();

    void pause();

    void resume();

    void next();

    void previous();

    void volumeUp();

    int getVolume();

    void volumeDown();

    List<TrackEntity> getPlayListTracks();
}
