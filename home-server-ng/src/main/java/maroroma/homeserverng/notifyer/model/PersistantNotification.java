package maroroma.homeserverng.notifyer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.notifications.NotificationEvent;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Permet de stocket dans un repo les notifications, en remettant à plat une notification standard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersistantNotification {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");;

    /**
     * Identifiant
     */
    private String id;

    /**
     * Titre de l'event.
     */
    private String title;
    /**
     * Message de l'event.
     */
    private String message;

    /**
     * Date de création.
     */
    private String creationDate;

    public static PersistantNotification of(NotificationEvent event) {
        return PersistantNotification.builder()
                .id(UUID.randomUUID().toString())
                .message(event.hasComplexMessage() ? event.getComplexMessage() : event.getMessage())
                .title(event.getTitle())
                .creationDate(sdf.format(event.getCreationDate()))
                .build();
    }
}
