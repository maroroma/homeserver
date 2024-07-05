package maroroma.homemusicplayer.repositories;

import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, UUID> {

    List<TrackEntity> findAllByAlbum(AlbumEntity albumEntity);

    void deleteByAlbum(AlbumEntity albumEntity);
}
