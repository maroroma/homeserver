package maroroma.homeserverng.remote.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.notifyer.services.CommonNotificatonTypes;
import maroroma.homeserverng.remote.RemoteModuleDescriptor;
import maroroma.homeserverng.remote.model.MailCommandAdapter;
import maroroma.homeserverng.remote.services.commands.RemoteCommand;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.mail.ImapReceiver;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javax.annotation.PostConstruct;

@Service
@Slf4j
public class MailCommandReceiver {


    public static final String HOMESERVER_REMOTE_MAIL_FETCH_FREQUENCY_PROPERTY = "homeserver.remote.mail.fetch.frequency";
    @Property(HOMESERVER_REMOTE_MAIL_FETCH_FREQUENCY_PROPERTY)
    private HomeServerPropertyHolder mailFetchFrequency;

    @Property("homeserver.remote.mail.authorized")
    private HomeServerPropertyHolder authorizedMails;

    @Property("homeserver.remote.command.authorized")
    private HomeServerPropertyHolder autorizedCommands;

    private ScheduledFuture<?> scheduledFuture;

    private final ThreadPoolTaskScheduler iotTaskScheduler;

    private final MailConfigHolder mailConfigHolder;

    private final NotifyerContainer notifyer;

    private final List<RemoteCommand> availableCommands;

    private final RemoteModuleDescriptor remoteModuleDescriptor;


    public MailCommandReceiver(ThreadPoolTaskScheduler iotTaskScheduler,
                               MailConfigHolder mailConfigHolder, NotifyerContainer notifyer,
                               List<RemoteCommand> availableCommands, RemoteModuleDescriptor remoteModuleDescriptor) {
        this.iotTaskScheduler = iotTaskScheduler;
        this.mailConfigHolder = mailConfigHolder;
        this.notifyer = notifyer;
        this.availableCommands = availableCommands;
        this.remoteModuleDescriptor = remoteModuleDescriptor;
    }

    @PostConstruct
    public void startScheduling() {
        this.scheduledFuture = iotTaskScheduler.scheduleAtFixedRate(this::fetchMailAndRunCommands, this.mailFetchFrequency.asInt());
    }

    @PropertyRefreshHandlers(HOMESERVER_REMOTE_MAIL_FETCH_FREQUENCY_PROPERTY)
    public void updateScheduling() {
        this.scheduledFuture.cancel(true);

        // on reschedule avec la nouvelle valeur
        this.startScheduling();

    }

    private void fetchMailAndRunCommands() {

        if (!this.remoteModuleDescriptor.isModuleEnabled()) {
            return;
        }



        log.warn("fetch mails");

        mailConfigHolder.createReceiver()
                // les mails sont supprimés après traitement
                .doReceive(ImapReceiver.PostActions.DELETE, messages -> {

                    // séparation entre mails valides et invalides pour le traitement
                    var allCommands = messages.stream()
                            .filter(Objects::nonNull)
                            .map(oneMessage -> MailCommandAdapter.builder().innerMessage(oneMessage).build())
                            .collect(Collectors.groupingBy(this::isMailValidCommand));

                    this.manageInvalidCommand(allCommands.get(false));
                    this.manageValidCommand(allCommands.get(true));
                });
    }

    private void manageValidCommand(List<MailCommandAdapter> mailCommandAdapters) {

        if (CollectionUtils.isEmpty(mailCommandAdapters)) {
            return;
        }

        mailCommandAdapters
                .forEach(oneMailCommandAdapter -> {
                    availableCommands.stream()
                            .filter(oneAvailableCommand -> oneAvailableCommand.getCommandType().equals(oneMailCommandAdapter.getCommandType()))
                            .findFirst()
                            .ifPresent(matchingRemoteCommand -> matchingRemoteCommand.execute(oneMailCommandAdapter));
                });
    }

    private void manageInvalidCommand(List<MailCommandAdapter> invalidCommand) {

        if (CollectionUtils.isEmpty(invalidCommand)) {
            return;
        }


        this.notifyer.notify(NotificationEvent.builder()
                .eventType(CommonNotificatonTypes.INVALID_COMMAND)
                .title("Réception d'une commande éronée")
                .message("homeserver a réceptionné un mail qu'il ne peut traiter en tant que commande")
                // TODO : créer un template pour ce mail
                .properties(Map.of("invalidCommands", invalidCommand.stream().map(MailCommandAdapter::invalidCommandAsDescriptor).toList()))
                .creationDate(new Date())
                .build());
    }

    /**
     * Une command est valide si email attendu et nom de commande attendu
     * @param mailCommandAdapter
     * @return
     */
    private boolean isMailValidCommand(MailCommandAdapter mailCommandAdapter) {
        return mailCommandAdapter.hasMatchingEmail(this.authorizedMails.asStringList())
                && mailCommandAdapter.hasMatchingCommand(this.autorizedCommands.asStringList());
    }


}
