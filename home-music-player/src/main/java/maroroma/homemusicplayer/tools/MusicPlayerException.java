package maroroma.homemusicplayer.tools;

public class MusicPlayerException extends RuntimeException {

    public MusicPlayerException(String message) {
        super(message);
    }

    public MusicPlayerException(Throwable cause) {
        super(cause);
    }

    public MusicPlayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
