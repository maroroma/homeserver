package maroroma.homeserverng.notifyer.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import maroroma.homeserverng.tools.template.TemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

/**
 * Implémentation de {@link Notifyer} permettant d'envoyer des mails.
 * Attention, ne fonctionne pas derrière certaines firewall.
 *
 * @author RLEVEXIE
 */
@Service
@Slf4j
public class MailNotifyer extends AbstractDisableableNotifyer implements Notifyer {

    /**
     * Effort de centralisation de la configuration des mails.
     */
    private final MailConfigHolder mailConfigHolder;

    /**
     * Pour l'instant on fait du cheap
     */
    private final Map<String, String> simpleNotificationTypesWithIcons = Map.of(
            CommonNotificatonTypes.STARTUP, "home.png",
            CommonNotificatonTypes.ALARM_ON, "alarm_on.png",
            CommonNotificatonTypes.ALARM_OFF, "alarm_off.png",
            CommonNotificatonTypes.DOWNLOAD_COMPLETED, "download_completed.png"
    );

    /**
     * liste des adresse mails pour lesquels on envoie les notifications.
     */
    @Property("homeserver.notifyer.mail.smtp.clients")
    private HomeServerPropertyHolder notificationClients;

    public MailNotifyer(MailConfigHolder mailConfigHolder) {
        this.mailConfigHolder = mailConfigHolder;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void doNotify(final NotificationEvent notification) throws HomeServerException {

        JavaMailSender sender = this.mailConfigHolder.createSender();

        sender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(notification.getTitle());
            mimeMessageHelper.setTo(this.notificationClients.asStringArray());
            mimeMessageHelper.setFrom("homeserverrkt");
            this.processTemplate(mimeMessageHelper, notification);
        });
    }

    void processTemplate(MimeMessageHelper mimeMessageHelper, NotificationEvent notificationEvent) throws MessagingException {
        if (simpleNotificationTypesWithIcons.containsKey(notificationEvent.getEventType())) {
            String content = TemplateBuilder.create()
                    .addParameter("title", notificationEvent.getTitle())
                    .addParameter("message", notificationEvent.getMessage())
                    .withTemplate("templates/default-notification-email.html")
                    .resolve();
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.addInline("imageResourceName",
                    new ClassPathResource("templates/assets/" + simpleNotificationTypesWithIcons.get(notificationEvent.getEventType())));
        } else if(CommonNotificatonTypes.TASKS_LIST_CHANGED.equals(notificationEvent.getEventType())) {
            String content = TemplateBuilder.create()
                    .addParameter("title", notificationEvent.getTitle())
                    .addParameter("message", notificationEvent.getMessage())
                    .addArrayParameter("newTasks", (List<Task>) notificationEvent.getProperties().get("newTasks"), this::taskToTableLineTransformer)
                    .addArrayParameter("deletedTasks", (List<Task>) notificationEvent.getProperties().get("deletedTasks"), this::taskToTableLineTransformer)
                    .withTemplate("templates/tasks-changed-email.html")
                    .resolve();
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.addInline("imageResourceName",
                    new ClassPathResource("templates/assets/home.png"));
        } else {
            mimeMessageHelper.setText(notificationEvent.getMessage());
        }
    }

    private String taskToTableLineTransformer(Task oneTask) {
        return "<tr><td>"  + oneTask.getSupplierType() + "&nbsp;:&nbsp;" + oneTask.getTitle() + "</td></tr>";
    }


}
