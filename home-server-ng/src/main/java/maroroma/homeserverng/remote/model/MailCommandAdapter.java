package maroroma.homeserverng.remote.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;


/**
 * Encapsulation d'un mail de base pour exploitation par le système de commande à distance
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailCommandAdapter {
    private Message innerMessage;

    public boolean hasMatchingEmail(List<String> authorizedEmails) {
        return extractEmails()
                .anyMatch(authorizedEmails::contains);
    }

    public boolean hasMatchingCommand(List<String> authorizedCommands) {
        return getOptionalCommandType()
                .map(RemoteCommandType::name)
                .filter(authorizedCommands::contains)
                .isPresent();
    }

    public RemoteCommandType getCommandType() {
        return this.getOptionalCommandType()
                .orElseThrow(() -> new RuntimeHomeServerException("no command type"));
    }

    public Optional<RemoteCommandType> getOptionalCommandType() {
        return Traper.trapWithOptional(() -> this.innerMessage.getSubject())
                // recup des nom de commandes avant validation
                .map(subject -> subject.split(" "))
                .stream()
                .flatMap(Stream::of)
                .map(RemoteCommandType::convert)
                .findFirst()
                .flatMap(Function.identity());
    }

    public InvalidCommandDescriptor invalidCommandAsDescriptor() {
        return InvalidCommandDescriptor.builder()
                .emails(extractEmails().collect(Collectors.joining(";")))
                .subject(this.extractSubject().orElse("NO SUBJECT"))
                .build();
    }

    public Stream<String> extractEmails() {
        return Traper.<Stream<Address>>trapOr(() -> Stream.of(this.innerMessage.getFrom()), Stream::of)
                .filter(InternetAddress.class::isInstance)
                .map(InternetAddress.class::cast)
                .map(InternetAddress::getAddress);
    }

    private Optional<String> extractSubject() {
        return Traper.trapWithOptional(() -> this.innerMessage.getSubject());
    }

}
