package maroroma.homeserverng.remote.model;

import java.util.*;
import java.util.stream.*;

/**
 * Liste des commandes en remote (par mail quoi)
 */
public enum RemoteCommandType {
    SENDLASTBOOKS("books"),
    HELLOWORLD();

    private final List<String> aliases;

    RemoteCommandType(String... aliases) {
        this.aliases = List.of(aliases);
    }

    private boolean match(String rawValue) {
        return this.name().equalsIgnoreCase(rawValue) || aliases.stream().anyMatch(anAlias -> anAlias.equalsIgnoreCase(rawValue));
    }

    public static Optional<RemoteCommandType> convert(String rawValue) {
        return Stream.of(RemoteCommandType.values())
                .filter(remoteCommandType -> remoteCommandType.match(rawValue))
                .findFirst();
    }
}
