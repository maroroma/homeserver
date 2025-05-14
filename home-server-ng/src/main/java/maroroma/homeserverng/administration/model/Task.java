package maroroma.homeserverng.administration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    private static Function<Task, String> DEFAULT_KEY_GENERATOR = task -> String.join("#",task.supplierType,
            task.id,
            task.title,
            Boolean.toString(task.isRunning));


    String id;
    String supplierType;
    String title;
    boolean isRunning;
    float done;
    float remaining;
    String labelTotal;
    String labelDone;
    String labelRemaining;
    @JsonIgnore
    Function<Task, String> generateKeyFunction;

    @JsonIgnore
    public String generateKey() {
        return Optional.ofNullable(this.generateKeyFunction)
                .map(keyGenerator -> keyGenerator.apply(this))
                .orElse(DEFAULT_KEY_GENERATOR.apply(this));

    }

    @JsonIgnore
    public boolean isInTaskList(List<Task> taskList) {
        return taskList.stream().map(Task::generateKey).toList().contains(this.generateKey());
    }

    @JsonIgnore
    public boolean isNotInTaskList(List<Task> taskList) {
        return !isInTaskList(taskList);
    }
}
