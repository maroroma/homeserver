package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.Task;

import java.util.List;

public interface TasksSupplier {

    String getType();
    List<Task> getTasks();
    boolean cancelTask(String taskId);

    default Task.TaskBuilder taskBuilder() {
        return Task.builder().supplierType(this.getType());
    }



}
