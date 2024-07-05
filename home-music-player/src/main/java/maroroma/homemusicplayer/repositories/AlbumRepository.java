package maroroma.homemusicplayer.repositories;

import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, UUID> {

}
