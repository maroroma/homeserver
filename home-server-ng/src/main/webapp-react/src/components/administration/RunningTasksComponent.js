import React from 'react';
import { useEffect, useState } from 'react';
import { administrationApi } from '../../apiManagement/AdministrationApi';
import { TASK_DELETE } from '../../eventReactor/EventIds';
import eventReactor from '../../eventReactor/EventReactor';
import { useDisplayList } from '../../tools/displayList';
import enhance from '../../tools/enhance';
import RunningTaskRenderer from './RunningTaskRendererComponent';




export default function RunningTasksComponent() {


    const [allTasks, setAllTasks] = useDisplayList();


    useEffect(() => {
        // premier appel histoire d'init la page
        administrationApi().getRunningTasks()
            .then(allTasksFromApi => {
                setAllTasks({
                    ...allTasks.update(allTasksFromApi)
                        .updateItems(enhance().selectable())
                });
            });

        const deleteTaskUnsubscribe = eventReactor().subscribe(TASK_DELETE,
            (taskToDelete) => {
                console.log("taskDelete", taskToDelete);
                administrationApi().cancelTask(taskToDelete).then(response => console.log(response));
            }
        );

        return deleteTaskUnsubscribe;


    }, []);

    // une la fréquence de rafraichissement récupérée, on lance l'appel récurrent
    useEffect(() => {
        const intervalToRemove = setInterval(() =>
            administrationApi().getRunningTasks()
                .then(response => {
                    const selectionMemento = allTasks.selectionMemento();
                    setAllTasks({
                        ...allTasks
                            .update(response)
                            .updateItems(enhance().selectable())
                            .applySelectionMemento(selectionMemento)
                    })
                })
            , 3000);

        return () => clearInterval(intervalToRemove);

    }, []);



    return <div>
        <ul className="collection">
            {allTasks.displayList.map((oneTask, taskIndex) => <RunningTaskRenderer key={taskIndex} runningTask={oneTask}></RunningTaskRenderer>)}
        </ul>
    </div>
}