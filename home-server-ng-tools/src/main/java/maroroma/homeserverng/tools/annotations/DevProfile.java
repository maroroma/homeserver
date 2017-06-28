package maroroma.homeserverng.tools.annotations;

import org.springframework.context.annotation.Profile;

/**
 * Annotation correspondant au {@link Profile} spring pour le développement.
 * @author RLEVEXIE
 *
 */
@Profile("developpement")
public @interface DevProfile {
}
