package maroroma.homeserverng.administration.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.model.TaskCancelRequest;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

/**
 * Gestion de l'affichage et de la suppression des taches en cours
 */
@Service
@RequiredArgsConstructor
public class TasksManager {

    /**
     * Les fournisseurs de taches de l'appli
     */
    private final List<TasksSupplier> tasksSuppliers;
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
