package maroroma.homeserverng.music.tools;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;

import maroroma.homeserverng.music.model.AlbumDescriptor;
import maroroma.homeserverng.tools.repositories.NanoRepository;

/**
 * Préprocessor utilisé pour le {@link NanoRepository} en charge de la sauvegarde des listes d'albums.
 * <br />
 * Permet de mettre  à jour le champ {@link AlbumDescriptor#setLastRefresh(long)}.
 * @author rlevexie
 *
 */
public class LastRefreshPreProcessor implements Consumer<AlbumDescriptor> {

	@Override
	public void accept(final AlbumDescriptor t) {
		t.setLastRefresh(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
	}

}
