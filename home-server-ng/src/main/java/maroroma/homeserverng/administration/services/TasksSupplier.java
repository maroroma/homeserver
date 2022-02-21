package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.Task;

import java.util.List;

/**
 * Définition d'un supplier de {@link Task}
 */
public interface TasksSupplier {

    /**
     * Type du supplier
     * @return
     */
    String getType();

    /**
     * Retourne l'ensemble de taches connues pour ce supplier
     * @return
     */
    List<Task> getTasks();

    /**
     * annulation de la tache demandée
     * @param taskId
     * @return
     */
    boolean cancelTask(String taskId);

    /**
     * Retourne l'instance prétypée du builder pour les {@link Task} retournée par l'implémentation du supplier
     * @return
     */
    default Task.TaskBuilder taskBuilder() {
        return Task.builder().supplierType(this.getType());
    }



}
