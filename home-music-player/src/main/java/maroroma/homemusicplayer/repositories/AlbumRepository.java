package maroroma.homemusicplayer.repositories;

import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, UUID> {
    List<AlbumEntity> findByNameContainsIgnoreCase(String expectedName);
}
