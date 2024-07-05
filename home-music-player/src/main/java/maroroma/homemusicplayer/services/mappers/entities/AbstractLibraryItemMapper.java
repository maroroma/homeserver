package maroroma.homemusicplayer.services.mappers.entities;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.AbstractLibraryItem;
import maroroma.homemusicplayer.model.library.api.LibraryItemArts;
import maroroma.homemusicplayer.model.library.entities.AbstractLibraryEntity;
import maroroma.homemusicplayer.tools.StreamUtils;

import java.util.*;

@RequiredArgsConstructor
public abstract class AbstractLibraryItemMapper<MODEL extends AbstractLibraryItem, ENTITY extends AbstractLibraryEntity> {


    public abstract MODEL mapToModel(ENTITY libraryEntity);

    public List<MODEL> mapToModel(List<ENTITY> libraryEntities) {
        return StreamUtils.of(libraryEntities)
                .map(this::mapToModel)
                .toList();
    }

    public abstract ENTITY mapToEntity(MODEL libraryItem);

    protected MODEL basicMapToModel(ENTITY libraryEntity, MODEL libraryItem) {
        libraryItem.setName(libraryEntity.getName());
        libraryItem.setLibraryItemArts(LibraryItemArts.builder()
                .fanartPath(libraryEntity.getFanartPath())
                .thumbPath(libraryEntity.getThumbPath())
                .build());
        libraryItem.setLibraryItemPath(libraryEntity.getLibraryItemPath());
        return libraryItem;
    }

    protected ENTITY basicMapToEntity(MODEL libraryItem, ENTITY libraryEntity) {
//        libraryEntity.setFanartPath(libraryItem.getFanartPath());
//        libraryEntity.setName(libraryItem.getName());
//        libraryEntity.setThumbPath(libraryItem.getThumbPath());
//        libraryEntity.setLibraryItemPath(libraryItem.getLibraryItemPath());
        return libraryEntity;
    }

}
