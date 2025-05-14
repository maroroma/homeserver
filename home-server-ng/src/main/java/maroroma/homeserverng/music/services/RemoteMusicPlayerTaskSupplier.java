package maroroma.homeserverng.music.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.services.TasksSupplier;
import maroroma.homeserverng.music.model.musicplayer.MusicPlayerStatus;
import maroroma.homeserverng.music.model.musicplayer.PlayerStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RemoteMusicPlayerTaskSupplier implements TasksSupplier {

    public static final String REMOTE_MUSIC_SUPPLIER_TYPE = "REMOTE_MUSIC";

    private final RemoteMusicPlayerService remoteMusicPlayerService;

    @Override
    public String getType() {
        return REMOTE_MUSIC_SUPPLIER_TYPE;
    }

    @Override
    public List<Task> getTasks() {
        MusicPlayerStatus musicPlayerStatus = remoteMusicPlayerService.getPlayerStatus();

        if (musicPlayerStatus.getPlayerStatus() == PlayerStatus.STOPPED) {
            return List.of();
        }

        return List.of(
                taskBuilder()
                        .isRunning(true)
                        .id(this.getType())
                        .generateKeyFunction(task -> String.join("#", this.getType(), musicPlayerStatus.getPlayerStatus().toString()))
                        .title(
                                String.join(" - ",
                                        musicPlayerStatus.getArtist().getName(),
                                        musicPlayerStatus.getAlbum().getName(),
                                        musicPlayerStatus.getTrack().getName())
                        )
                        .build()
        );
    }

    @Override
    public boolean cancelTask(String taskId) {
        return remoteMusicPlayerService.stop();
    }
}
