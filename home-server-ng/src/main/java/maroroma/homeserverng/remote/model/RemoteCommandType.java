package maroroma.homeserverng.remote.model;

import java.util.*;
import java.util.stream.*;

/**
 * Liste des commandes en remote (par mail quoi)
 */
public enum RemoteCommandType {
    SENDLASTBOOKS,
    HELLOWORLD;

    public static Optional<RemoteCommandType> convert(String rawValue) {
        return Stream.of(RemoteCommandType.values())
                .filter(remoteCommandType -> remoteCommandType.name().equalsIgnoreCase(rawValue))
                .findFirst();
    }
}
