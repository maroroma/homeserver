package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.model.TaskCancelRequest;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestion de l'affichage et de la suppression des taches en cours
 */
@Service
public class TasksManager {

    /**
     * Les fournisseurs de taches de l'appli
     */
    private final List<TasksSupplier> tasksSuppliers;


    public TasksManager(List<TasksSupplier> tasksSuppliers) {
        this.tasksSuppliers = tasksSuppliers;
    }

    /**
     * REtourne l'ensemble des taches en cours
     * @return
     */
    public List<Task> getCurrentTasks() {
        return tasksSuppliers.parallelStream()
                .map(oneSupplier -> Traper.trapWithOptional(oneSupplier::getTasks))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Annule la tache demandée
     * @param taskCancelRequest
     * @return
     */
    public boolean cancelOneTask(TaskCancelRequest taskCancelRequest) {
        return tasksSuppliers.stream()
                // la requete détermine le gestionnaire cible
                .filter(oneSupplier -> oneSupplier.getType().equals(taskCancelRequest.getSupplierType()))
                .findFirst()
                .map(matchingSupplier -> matchingSupplier.cancelTask(taskCancelRequest.getTaskId()))
                .orElse(false);
    }

}
