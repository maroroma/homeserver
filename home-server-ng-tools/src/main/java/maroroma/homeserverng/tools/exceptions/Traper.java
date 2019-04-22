package maroroma.homeserverng.tools.exceptions;

import java.util.Optional;

public abstract class Traper {

    public static <T> T trap(WithExceptionSupplier<T> supplier) {
        return trap(supplier, null);
    }

    public static <T> T trap(WithExceptionSupplier<T> supplier, final String message) {
        try {
            return supplier.supply();
        } catch(Exception exception) {
            throw Optional.ofNullable(message)
                    .map(ifMessage -> new RuntimeHomeServerException(exception, ifMessage))
                    .orElse(new RuntimeHomeServerException(exception));
        }
    }

    public static boolean trapToBoolean(WithExceptionEmptyConsumer emptyConsumer) {
        try {
            emptyConsumer.apply();
            return true;
        } catch(Exception exception) {
            return false;
        }
    }

    public interface WithExceptionEmptyConsumer {
        void apply() throws Exception;
    }

    public interface WithExceptionSupplier<T> {
        T supply() throws Exception;
    }
}
