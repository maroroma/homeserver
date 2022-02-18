package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.model.TaskCancelRequest;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TasksManager {

    private final List<TasksSupplier> tasksSuppliers;

    public TasksManager(List<TasksSupplier> tasksSuppliers) {
        this.tasksSuppliers = tasksSuppliers;
    }

    public List<Task> getCurrentTasks() {
        return tasksSuppliers.parallelStream()
                .map(oneSupplier -> Traper.trapWithOptional(oneSupplier::getTasks))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public boolean cancelOneTask(TaskCancelRequest taskCancelRequest) {
        return tasksSuppliers.stream()
                .filter(oneSupplier -> oneSupplier.getType().equals(taskCancelRequest.getSupplierType()))
                .findFirst()
                .map(matchingSupplier -> matchingSupplier.cancelTask(taskCancelRequest.getTaskId()))
                .orElse(false);
    }

}
