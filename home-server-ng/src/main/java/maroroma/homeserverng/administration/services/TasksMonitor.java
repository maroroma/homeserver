package maroroma.homeserverng.administration.services;

import maroroma.homeserverng.administration.model.Task;
import maroroma.homeserverng.notifyer.services.CommonNotificatonTypes;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.annotations.PropertyRefreshHandlers;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.notifications.NotificationEvent;
import maroroma.homeserverng.tools.notifications.NotifyerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * Controle à intervalle régulier les taches en cours remontées par le {@link TasksManager}
 * <br />
 * Si le service détecte des différences, on notifie
 */
@Service
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

    public TasksMonitor(TasksManager tasksManager,
                        ThreadPoolTaskScheduler adminTaskScheduler,
                        NotifyerContainer notifyerContainer) {
        this.tasksManager = tasksManager;
        this.adminTaskScheduler = adminTaskScheduler;
        this.notifyerContainer = notifyerContainer;
    }


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
        String newListHash = this.generateKeyFromTaskList(newCurrentTasksList);
        String lastCurrentHash = this.generateKeyFromTaskList(this.lastCurrentTaskList);

        // si les deux hash sont différents
        if (!lastCurrentHash.equals(newListHash)) {
            System.out.println("lastCurrentHash: " + lastCurrentHash);
            System.out.println("newListHash: " + newListHash);

            List<String> newListHashes = newCurrentTasksList.stream().map(Task::generateKey).toList();
            List<String> lastListHashes = lastCurrentTaskList.stream().map(Task::generateKey).toList();

            // qui est nouveau
            List<Task> newTasks = newCurrentTasksList.stream()
                    .filter(oneTask -> !lastListHashes.contains(oneTask.generateKey()))
                    .toList();

            // qui a disparu
            List<Task> deletedTasks = lastCurrentTaskList.stream()
                    .filter(oneTask -> !newCurrentTasksList.contains(oneTask.generateKey()))
                    .toList();

            this.notifyerContainer.notify(NotificationEvent.builder()
                            .creationDate(new Date())
                            .eventType(CommonNotificatonTypes.TASKS_LIST_CHANGED)
                            .title("Modification des taches en cours")
                            .message("Une modification des taches en cours a été détectée via le serveur")
                            .properties(Map.of("newTasks",
                                    newTasks,
                                    "deletedTasks",
                                    deletedTasks))
                    .build());
        }



        this.lastCurrentTaskList = newCurrentTasksList;
    }

    private String generateKeyFromTaskList(List<Task> taskList) {
        return taskList.stream()
                .map(Task::generateKey)
                .sorted()
                .collect(Collectors.joining("---"));
    }


}
