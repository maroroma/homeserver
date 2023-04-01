package maroroma.homeserverng.remote.services.commands;

import maroroma.homeserverng.remote.model.MailCommandAdapter;
import maroroma.homeserverng.remote.model.RemoteCommandType;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.*;

import static maroroma.homeserverng.notifyer.services.CommonNotificatonTypes.REMOTE_CONTROL_HELLO_WORLD;

/**
 * Commande de test
 */
@Component
public class HelloWorldRemoteCommand implements RemoteCommand {

    private final NotifyerContainer notifyerContainer;

    public HelloWorldRemoteCommand(NotifyerContainer notifyerContainer) {
        this.notifyerContainer = notifyerContainer;
    }

    @Override
    public RemoteCommandType getCommandType() {
        return RemoteCommandType.HELLOWORLD;
    }

    @Override
    public void execute(MailCommandAdapter mailCommandAdapter) {
        this.notifyerContainer.notify(
                NotificationEvent.builder()
                        .creationDate(new Date())
                        .eventType(REMOTE_CONTROL_HELLO_WORLD)
                        .title("Ceci est une commande de test")
                        .message("Voici la réponse pour votre commande HELLO_WOLRD, " + mailCommandAdapter.extractEmails().collect(Collectors.joining(",")))
                        .build()
        );

    }
}
