package maroroma.homeserverng.iot.services;

import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.administration.services.TasksSupplier;
import maroroma.homeserverng.iot.model.scheduler.ScheduledTask;
import maroroma.homeserverng.iot.model.scheduler.SchedulerFrequency;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SchedulerManager implements TasksSupplier {

    private final ThreadPoolTaskScheduler iotTaskScheduler;
    private final Map<String, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>();

    public SchedulerManager(ThreadPoolTaskScheduler iotTaskScheduler) {
        this.iotTaskScheduler = iotTaskScheduler;
    }

    @EventListener
    public void handlerApplicationStart(ContextRefreshedEvent startEvent) {
//        ScheduledTask sampleTask = ScheduledTask.builder()
//                .scheduledFuture(this.iotTaskScheduler.scheduleAtFixedRate(() -> System.out.println("sample task"), SchedulerFrequency.Frequencies.ONE_MINUTE.period()))
//                .id(UUID.randomUUID().toString())
//                .name("Tache de test " + SchedulerFrequency.Frequencies.ONE_MINUTE.frequency().getName())
//                .build();
//
//        scheduledTaskMap.put(sampleTask.getId(), sampleTask);
    }

    @Override
    public String getType() {
        return "SCHEDULER_TASK";
    }

    @Override
    public List<Task> getTasks() {
        return this.scheduledTaskMap.values()
                .stream()
                .map(oneScheduledTask -> taskBuilder()
                        .id(oneScheduledTask.getId())
                        .title(oneScheduledTask.getName())
                        .isRunning(true)
                        .done(50)
                        .labelDone("en cours")
                        .labelTotal("")
                        .build())
                .toList();
    }

    @Override
    public boolean cancelTask(String taskId) {
        return false;
    }
}
