package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.security.SecurityManager;
import maroroma.homeserverng.tools.security.SimpleUser;
import org.springframework.stereotype.Service;

/**
 * Gestion centralisation de la sécurité de l'application.
 * Pour l'instant, c'est plus une blaque qu'autre chose
 */
@Service
public class SecurityManagerImpl implements SecurityManager {

    @Property("homeserver.administrations.sambaUser")
    HomeServerPropertyHolder sambaUser;

    public SimpleUser getSambaUser() {
        return this.sambaUser.asUser();
    }

}
