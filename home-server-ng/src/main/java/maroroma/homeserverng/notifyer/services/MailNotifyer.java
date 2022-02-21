package maroroma.homeserverng.notifyer.services;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.Notifyer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
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
    private final TemplateEngine templateEngine;

    /**
     * Pour l'instant on fait du cheap
     */
    private final Map<String, String> notificationTypesWithIcons = Map.of(
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

    public MailNotifyer(MailConfigHolder mailConfigHolder,
                        TemplateEngine templateEngine) {
        this.mailConfigHolder = mailConfigHolder;
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    protected void doNotify(final NotificationEvent notification) throws HomeServerException {

        JavaMailSender sender = this.mailConfigHolder.createSender();

        sender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(notification.getTitle());
            mimeMessageHelper.setTo(this.notificationClients.asStringArray());
            mimeMessageHelper.setFrom("homeserverrkt");
            this.processTemplate(mimeMessageHelper, notification);
        });
    }

    void processTemplate(MimeMessageHelper mimeMessageHelper, NotificationEvent notificationEvent) throws MessagingException {
        if (notificationTypesWithIcons.containsKey(notificationEvent.getEventType())) {
            Context context = new Context();
            context.setVariable("notification", notificationEvent);
            context.setVariable("imageResourceName", "imageResourceName");
            mimeMessageHelper.setText(templateEngine.process("default-email", context), true);
            mimeMessageHelper.addInline("imageResourceName",
                    new ClassPathResource("templates/assets/" + notificationTypesWithIcons.get(notificationEvent.getEventType())));
        } else {
            mimeMessageHelper.setText(notificationEvent.getMessage());
        }
    }


}
