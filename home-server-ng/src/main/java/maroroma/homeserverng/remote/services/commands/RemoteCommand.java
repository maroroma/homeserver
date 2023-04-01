package maroroma.homeserverng.remote.services.commands;

import maroroma.homeserverng.remote.model.MailCommandAdapter;
import maroroma.homeserverng.remote.model.RemoteCommandType;

public interface RemoteCommand {

    RemoteCommandType getCommandType();

    void execute(MailCommandAdapter mailCommandAdapter);
}
