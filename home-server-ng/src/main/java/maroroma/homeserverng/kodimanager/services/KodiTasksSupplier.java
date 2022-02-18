package maroroma.homeserverng.kodimanager.services;

import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.services.TasksSupplier;
import maroroma.homeserverng.kodimanager.model.CurrentPlayer;
import maroroma.homeserverng.kodimanager.model.KodiCurrentPlayers;
import maroroma.homeserverng.tools.helpers.Tuple;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KodiTasksSupplier implements TasksSupplier {

    public static final String KODI_SUPPLIER_TYPE = "KODI";
    public static final String DURATION_STRING_FORMAT = "%02d:%02d:%02d";
    public static final String TASK_ID_SEPARATOR = "###";
    public static final String TASK_ID_FORMAT = "%s" + TASK_ID_SEPARATOR + "%s";

    private final KodiManagerService kodiManagerService;

    public KodiTasksSupplier(KodiManagerService kodiManagerService) {
        this.kodiManagerService = kodiManagerService;
    }

    @Override
    public String getType() {
        return KODI_SUPPLIER_TYPE;
    }

    @Override
    public List<Task> getTasks() {
        return kodiManagerService.getCurrentPlayersWithProperties().stream()
                .flatMap(playersFromOneInstance -> playersFromOneInstance.getCurrentPlayers().stream()
                        .map(onePlayer -> taskBuilder()
                                .id(generateId(playersFromOneInstance, onePlayer))
                                .title(onePlayer.getTitle())
                                .isRunning(true)
                                .done(onePlayer.getPercentage())
                                .remaining(100 - onePlayer.getPercentage())
                                .labelDone(convertDuration(onePlayer.getCurrentTime()))
                                .labelTotal(convertDuration(onePlayer.getTotalTime()))
                                .build()))
                .collect(Collectors.toList());
    }

    String convertDuration(Duration duration) {
        return String.format(DURATION_STRING_FORMAT,
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

    String generateId(KodiCurrentPlayers container, CurrentPlayer currentPlayer) {
        return Base64Utils
                .encodeToString(
                        TASK_ID_FORMAT
                                .formatted(container.getKodiUrl(), currentPlayer.getPlayerId()).getBytes()
                );
    }

    Tuple<String, String> reverseInfosFromId(String taskId) {
        String idFromBase64 = new String(Base64Utils.decodeFromString(taskId));
        String[] splittedId = idFromBase64.split(TASK_ID_SEPARATOR);
        return Tuple.from(splittedId[0], splittedId[1]);
    }

    @Override
    public boolean cancelTask(String taskId) {
        Tuple<String, String> taskIdentifiers = reverseInfosFromId(taskId);

        return this.kodiManagerService.stopPlayer(taskIdentifiers.getItem1(), taskIdentifiers.getItem2());

    }
}
