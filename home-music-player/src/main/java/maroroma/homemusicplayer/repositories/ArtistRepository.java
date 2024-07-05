package maroroma.homemusicplayer.repositories;

import maroroma.homemusicplayer.model.library.entities.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

    Optional<ArtistEntity> findByName(String name);
    Optional<ArtistEntity> findByLibraryItemPath(String libraryItemPath);

}
