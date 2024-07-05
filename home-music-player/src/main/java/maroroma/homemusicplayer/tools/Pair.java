package maroroma.homemusicplayer.tools;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Pair<LEFT, RIGHT> {
    private final LEFT left;
    private final RIGHT right;

    public static <LEFT, RIGHT> Pair<LEFT, RIGHT> of(LEFT left, RIGHT right) {
        return new Pair(left, right);
    }

    public <NEW_RIGHT> Pair<LEFT, NEW_RIGHT> mapRight(Function<RIGHT, NEW_RIGHT> mappingFunction) {
        CustomAssert.notNull(mappingFunction, "");
        return Optional.ofNullable(this.getRight())
                .map(mappingFunction)
                .map(newRight -> of(this.left, newRight))
                .orElse(of(this.left, null));
    }
}
