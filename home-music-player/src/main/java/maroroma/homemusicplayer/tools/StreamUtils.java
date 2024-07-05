package maroroma.homemusicplayer.tools;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

public final class StreamUtils {
    private StreamUtils() {}
    public static <T> Stream<T> of(Collection<T> collection) {
        return Optional.ofNullable(collection)
                .map(Collection::stream)
                .orElse(Stream.of());
    }

    public static <T> Collector<T, Object, List<T>> shuffle() {
        return collectingAndThen(toList(), listToShuffle -> {
            Collections.shuffle(listToShuffle);
            return listToShuffle;
        });
    }
}
