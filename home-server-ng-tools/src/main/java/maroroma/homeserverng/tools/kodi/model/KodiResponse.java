package maroroma.homeserverng.tools.kodi.model;

import lombok.Getter;
import maroroma.homeserverng.tools.kodi.methods.KodiClient;

import java.util.function.Function;


/**
 * Lie une réponse de kodi à l'instance qui a été intérrogée
 * @param <T>
 */
public class KodiResponse<T> extends FromKodiInstance<KodiResponse<T>> {

    @Getter
    private T response;


    private boolean hasError;

    private KodiResponse(KodiClient kodiClient) {
        super(kodiClient);
    }

    public static <U> KodiResponse<U> fromInstance(KodiClient kodiClient) {
        return new KodiResponse<>(kodiClient);
    }

    public KodiResponse<T> withResponse(T response) {
        this.response = response;
        return this;
    }

    public KodiResponse<T> withError() {
        return withError(true);
    }

    public KodiResponse<T> withError(boolean hasError) {
        this.hasError = hasError;
        return this;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public boolean hasNoError() {
        return !this.hasError();
    }

    /**
     * permet de transformer le contenu de la réponse en
     * @param mappingFunction fonction  de transfo
     * @param <U> nouveau type
     * @return nouvelle {@link KodiResponse} dont la response a été transformée.
     */
    public <U> KodiResponse<U> map(Function<T, U> mappingFunction) {
        return KodiResponse.<U>fromInstance(this.getKodiClient())
                .withResponse(mappingFunction.apply(this.getResponse()))
                .withError(this.hasError());
    }
}
