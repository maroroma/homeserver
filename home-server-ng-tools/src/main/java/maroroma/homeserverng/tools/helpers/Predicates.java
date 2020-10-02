package maroroma.homeserverng.tools.helpers;

import java.util.function.Predicate;

/**
 * Oué java 8 je sais
 */
public class Predicates {
    public static <T> Predicate<T> not(Predicate<T> predicateToInvert) {
        return toBeTested -> !predicateToInvert.test(toBeTested);
    }
}
