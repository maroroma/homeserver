package maroroma.homeserverng.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.notifyer.model.PersistantNotification;

import java.util.List;

/**
 * Container de réponse pour la récupération de l'ensemble des logs.
 * Utilisé pour avoir l'id du repo nettoyable sur les services génériques de l'admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllLogEvents {

    private String repoId;
    private List<PersistantNotification> persistantNotifications;

}
