package maroroma.homeserverng.tools.annotations;

import org.springframework.context.annotation.Profile;

/**
 * Annotation correspondant au {@link Profile} spring pour la production.
 * @author RLEVEXIE
 *
 */
@Profile("production")
public @interface ProductionProfile {

}
