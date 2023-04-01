package maroroma.homeserverng.tools.mail;

import com.sun.mail.imap.IMAPFolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;

import java.util.*;
import java.util.function.*;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.Store;

@Data
@AllArgsConstructor
@Builder
public class ImapReceiver {
    private final String host;
    private final String user;
    private final String password;


    public void doReceive(PostActions postActions, Consumer<List<Message>> messagesConsumer) {
        IMAPFolder folder = null;
        Store store = null;
        try {
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);

            store = session.getStore("imaps");
            store.connect(this.host, this.user, this.password);

            folder = (IMAPFolder) store.getFolder("inbox");


            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
            List<Message> messages = List.of(folder.getMessages());

            // ici le traitement de l'appelant sur les messages qu'on lui fourni
            messagesConsumer.accept(messages);

            // ici application des fonctions posttraitement (pouvoir virer les mails si on veut)
            messages.forEach(postActions.action);

        } catch (MessagingException e) {
            throw new RuntimeHomeServerException(e);
        } finally {
            trapMessagingException(folder, (messingFolder) -> {
                if (messingFolder.isOpen()) {
                    // ici la fermeture avec expunge à true permet de purger le dossier
                    // si des deletes ont été demandés par exemple
                    messingFolder.close(true);
                }
            });

            trapMessagingException(store, Service::close);
        }
    }

    private <T> void trapMessagingException(T messingObject, MessagingExceptionThrower<T> action) {
        try {
            if (messingObject != null) {
                action.run(messingObject);
            }
        } catch (MessagingException e) {
            throw new RuntimeHomeServerException(e, "exception rencontrée lors d'un traitement d'email");
        }
    }

    private interface MessagingExceptionThrower<T> {
        void run(T messingObject) throws MessagingException;
    }

    public enum PostActions {
        NONE(oneMessage -> {}),
        DELETE(oneMessage -> Traper.trapToBoolean(() -> oneMessage.setFlag(Flags.Flag.DELETED, true)));

        private final Consumer<Message> action;


        PostActions(Consumer<Message> action) {
            this.action = action;
        }
    }

}
