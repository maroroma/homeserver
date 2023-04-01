package maroroma.homeserverng.remote.services.commands;

import maroroma.homeserverng.book.model.SendCollectionsStatusRequest;
import maroroma.homeserverng.book.services.BookService;
import maroroma.homeserverng.remote.model.MailCommandAdapter;
import maroroma.homeserverng.remote.model.RemoteCommandType;
import org.springframework.stereotype.Component;

import java.util.stream.*;

/**
 * Renvoi les livres manquants sur sollicitations extérieures
 */
@Component
public class SendLastBooksRemoteControl implements RemoteCommand {

    private final BookService bookService;

    public SendLastBooksRemoteControl(BookService bookService) {
        this.bookService = bookService;
    }


    @Override
    public RemoteCommandType getCommandType() {
        return RemoteCommandType.SENDLASTBOOKS;
    }

    @Override
    public void execute(MailCommandAdapter mailCommandAdapter) {
        this.bookService.sendMailForUncompleteCollections(SendCollectionsStatusRequest.builder()
                .emails(mailCommandAdapter.extractEmails().collect(Collectors.toList()))
                .build());
    }
}
