package maroroma.homeserverng.administration.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.notifyer.services.CommonNotificatonTypes;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javax.annotation.PostConstruct;

/**
 * Controle à intervalle régulier les taches en cours remontées par le {@link TasksManager}
 * <br />
 * Si le service détecte des différences, on notifie
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TasksMonitor {

    public static final String HOMESERVER_TASK_MONITOR_REFRESH_FREQUENCY = "homeserver.administration.tasks.monitor.refresh.frequency";
    public static final int DEFAULT_OFF_VALUE = -1;

    /**
     * PErmet de récuper les taches en cours
     */
    private final TasksManager tasksManager;
    private final ThreadPoolTaskScheduler adminTaskScheduler;

    /**
     * Dernière liste de tache en cours connue
     */
    private List<Task> lastCurrentTaskList = new ArrayList<>();

    /**
     * Pour la levée de notifications
     */
    private final NotifyerContainer notifyerContainer;


    @Property(HOMESERVER_TASK_MONITOR_REFRESH_FREQUENCY)
    private HomeServerPropertyHolder refreshTasksStatusFrequency;

    /**
     * Tache en cours, stockée pour annulation et reprogrammation
     * si changement de la fréquence
     */
    private ScheduledFuture<?> scheduledFuture;


    @PostConstruct
    public void startScheduling() {
        if (this.refreshTasksStatusFrequency.asInt() != DEFAULT_OFF_VALUE) {
            this.scheduledFuture = adminTaskScheduler.scheduleAtFixedRate(this::scheduledTasksMonitoring, this.refreshTasksStatusFrequency.asInt());
        }
    }

    @PropertyRefreshHandlers(HOMESERVER_TASK_MONITOR_REFRESH_FREQUENCY)
    public void updateScheduling() {
        this.scheduledFuture.cancel(true);

        // on reschedule avec la nouvelle valeur
        this.startScheduling();

    }

    private void scheduledTasksMonitoring() {
        List<Task> newCurrentTasksList = this.tasksManager.getCurrentTasks();

        // pour la comparaison, on va générer un "hash" pour chaque liste et comparer le tout
        String newListHash = this.generateGlobalHashFromTaskList(newCurrentTasksList);
        String lastCurrentHash = this.generateGlobalHashFromTaskList(this.lastCurrentTaskList);

        // si les deux hash sont différents
        if (!lastCurrentHash.equals(newListHash)) {
            log.info("lastCurrentHash: {}", lastCurrentHash);
            log.info("newListHash: {}", newListHash);

            // qui est nouveau
            List<Task> newTasksForNotification = newCurrentTasksList.stream()
                    .filter(oneTask -> oneTask.isNotInTaskList(lastCurrentTaskList))
                    .toList();

            // qui a disparu
            List<Task> deletedTasksForNotification = lastCurrentTaskList.stream()
                    .filter(oneTask -> oneTask.isNotInTaskList(newCurrentTasksList))
                    .toList();

            this.notifyerContainer.notify(NotificationEvent.builder()
                            .creationDate(new Date())
                            .eventType(CommonNotificatonTypes.TASKS_LIST_CHANGED)
                            .title("Modification des taches en cours")
                            .message("Une modification des taches en cours a été détectée via le serveur")
                            .properties(Map.of("newTasks",
                                    newTasksForNotification,
                                    "deletedTasks",
                                    deletedTasksForNotification))
                    .build());
        }



        this.lastCurrentTaskList = newCurrentTasksList;
    }

    private String generateGlobalHashFromTaskList(List<Task> taskList) {
        return taskList.stream()
                .map(Task::generateKey)
                .sorted()
                .collect(Collectors.joining("---"));
    }


}
