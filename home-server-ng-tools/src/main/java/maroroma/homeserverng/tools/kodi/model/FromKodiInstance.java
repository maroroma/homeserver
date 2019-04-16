package maroroma.homeserverng.tools.kodi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import maroroma.homeserverng.tools.kodi.methods.KodiClient;

/**
 * Associe un objet à une instance kodi.
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class FromKodiInstance<T extends FromKodiInstance<T>> {
    private KodiClient kodiClient;

    public T withKodiClient(final KodiClient kodiClient) {
        this.kodiClient = kodiClient;
        return (T) this;
    }

}
