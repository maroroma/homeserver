package maroroma.homemusicplayer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.FileAdapterFilter;
import maroroma.homemusicplayer.model.library.api.PlayList;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.CustomAssert;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import maroroma.homemusicplayer.tools.Traper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Service
public class PlayListService {

    private final FilesFactory filesFactory;
    private final TrackService trackService;
    private final String playListDirectoryPath;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlayListService(FilesFactory filesFactory,
                           TrackService trackService,
                           @Value("${musicplayer.playlists.directory}") String playListDirectoryPath) {
        this.filesFactory = filesFactory;
        this.trackService = trackService;
        this.playListDirectoryPath = playListDirectoryPath;
    }

    public PlayList createPlayList(final PlayList requestedPlayList) {
        CustomAssert.notNull(requestedPlayList, "La playlist ne peut pas être nulle");
        CustomAssert.hasLength(requestedPlayList.getName(), "Le nom de la playlist ne peut pas être vide");

        var sameNameExists = this.getAllPlayLists()
                .stream()
                .anyMatch(p -> p.getName().equals(requestedPlayList.getName()));

        CustomAssert.isFalse(sameNameExists, "Une playlist portant le même nom existe déjà");

        var newPlayList = PlayList.builder()
                .name(requestedPlayList.getName())
                .playListId(UUID.randomUUID())
                .build();

        var playListAndItsFileAdapter =
                PlayListAndItsFileAdapter.builder()
                        .fileAdapter(playListDirectory().combine(newPlayList.getPlayListId().toString() + ".json"))
                        .playList(newPlayList)
                        .build();

        return this.save(playListAndItsFileAdapter).playList();
    }

    public PlayList getPlayList(final UUID playListId) {
        return this.findPlayList(playListId)
                .map(PlayListAndItsFileAdapter::playList)
                .orElseThrow(() -> new MusicPlayerException("playList non trouvée"));
    }

    public PlayList addTrackToPlayList(final UUID playListId, final UUID trackId) {
        CustomAssert.isTrue(trackService.findTrackById(trackId).isPresent(), String.format("Aucun track trouvé pour l'id <%s>", trackId));
        return this.updatePlayList(playListId, playList -> playList.addTrack(trackId));
    }

    public PlayList removeTrackFromPlayList(final UUID playListId, final UUID trackId) {
        return this.updatePlayList(playListId, playList -> Optional
                .ofNullable(playList.getTracks())
                .ifPresent(existingTracks -> existingTracks.remove(trackId)));
    }

    public PlayList renamePlayList(final UUID playListId, final String newName) {
        return this.updatePlayList(playListId, playList -> playList.setName(newName));
    }

    public boolean deletePlayList(final UUID playListId) {
        return this.findPlayList(playListId)
                .map(PlayListAndItsFileAdapter::fileAdapter)
                .map(FileAdapter::delete)
                .orElse(false);
    }

    public List<TrackEntity> getTracksFromPlayList(final UUID playListId) {

        var requestedPlayList = this.findPlayList(playListId)
                .orElseThrow(() -> new MusicPlayerException("playList non trouvée"));

        if (CollectionUtils.isEmpty(requestedPlayList.playList().getTracks())) {
            return Collections.emptyList();
        }

        var tracksFromDataBase = Optional.of(requestedPlayList)
                .map(PlayListAndItsFileAdapter::playList)
                .map(PlayList::getTracks)
                .map(this.trackService::findTrackEntities)
                .stream()
                .flatMap(Collection::stream)
                .toList();

        // nettoyage de la playList si des tracks ont disparus
        var trackIdsFromDatabase = tracksFromDataBase.stream()
                .map(TrackEntity::getId)
                .collect(Collectors.toSet());

        if (trackIdsFromDatabase.size() != requestedPlayList.playList().getTracks().size()) {
            this.updatePlayList(playListId, playList -> playList.getTracks().removeIf(trackId -> !trackIdsFromDatabase.contains(trackId)));
        }

        return tracksFromDataBase;
    }

    private FileAdapter playListDirectory() {
        var playListDirectory = this.filesFactory.getFileFromPath(this.playListDirectoryPath);

        if (!playListDirectory.exists()) {
            playListDirectory.mkdirs();
        }

        return playListDirectory;
    }

    private Optional<PlayListAndItsFileAdapter> findPlayList(UUID playListId) {
        return playListDirectory().getFiles()
                .stream()
                .filter(FileAdapterFilter.nameStartingBy(List.of(playListId.toString())))
                .findFirst()
                .map(playListFile -> PlayListAndItsFileAdapter.builder()
                        .fileAdapter(playListFile)
                        .playList(deserializePlayList(playListFile))
                        .build());
    }

    public List<PlayList> getAllPlayLists() {
        return playListDirectory()
                .getFiles()
                .stream()
                .filter(FileAdapterFilter.jsonFile())
                .map(this::deserializePlayList)
                .toList();
    }

    private PlayList deserializePlayList(FileAdapter playListFileAdapter) {
        return Traper.trap(() -> objectMapper.readValue(playListFileAdapter.getInputStream(), PlayList.class));
    }

    private PlayListAndItsFileAdapter save(PlayListAndItsFileAdapter playListAndItsFileAdapter) {
        return Traper.trap(() -> {
            this.objectMapper.writeValue(playListAndItsFileAdapter.fileAdapter().getOutputStream(), playListAndItsFileAdapter.playList());
            return playListAndItsFileAdapter;
        });
    }

    private PlayList updatePlayList(UUID playListId, Consumer<PlayList> updateActions) {
        return this.findPlayList(playListId)
                .map(playListWithFile -> {
                    updateActions.accept(playListWithFile.playList());
                    return playListWithFile;
                })
                .map(this::save)
                .map(PlayListAndItsFileAdapter::playList)
                .orElseThrow(() -> new MusicPlayerException("playList non trouvée"));
    }

    @Builder
    private record PlayListAndItsFileAdapter(PlayList playList, FileAdapter fileAdapter) {
    }
}
