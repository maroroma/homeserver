package maroroma.homeserverng.seedbox.services;

import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.services.TasksSupplier;
import maroroma.homeserverng.seedbox.model.TorrentsToDelete;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeedBoxTasksSupplier implements TasksSupplier {


    public static final String SEEDBOX_SUPPLIER_TYPE = "SEEDBOX";

    private final SeedboxRemoteControlServiceImpl seedboxRemoteControlService;

    public SeedBoxTasksSupplier(SeedboxRemoteControlServiceImpl seedboxRemoteControlService) {
        this.seedboxRemoteControlService = seedboxRemoteControlService;
    }

    @Override
    public String getType() {
        return SEEDBOX_SUPPLIER_TYPE;
    }

    @Override
    public List<Task> getTasks() {

        return this.seedboxRemoteControlService.getTorrents()
                .stream()
                .map(oneRunningTorrent ->
                        taskBuilder()
                                .id(oneRunningTorrent.getId())
                                .title(oneRunningTorrent.getName())
                                .isRunning(!oneRunningTorrent.isCompleted())
                                .done(oneRunningTorrent.getPercentDone())
                                .labelDone("" + oneRunningTorrent.getDone())
                                .labelTotal("" + oneRunningTorrent.getTotal())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelTask(String taskId) {
        this.seedboxRemoteControlService.removeTorrents(new TorrentsToDelete(List.of(Integer.parseInt(taskId))));
        return true;
    }
}
