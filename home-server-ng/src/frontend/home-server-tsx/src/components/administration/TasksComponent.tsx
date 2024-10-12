import {FC, ReactElement, useEffect} from "react";
import {Button, ListGroup, ProgressBar} from "react-bootstrap";
import {BoxSeam, Display, Question, Trash} from "react-bootstrap-icons";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {AdministrationRequester} from "../../api/AdministrationRequester";
import {AdministrationLoadedTasksAction} from "../../context/actions/administration/AdministrationLoadedTasksAction";
import Task from "../../model/administration/Task";
import {CustomClassNames} from "../bootstrap/CssTools";


const TasksComponent: FC = () => {

    const { administrationSubState, dispatch } = useHomeServerContext();


    useEffect(() => {

        const intervalToRemove = setInterval(() => AdministrationRequester.getRunningTasks()
            .then(response => dispatch(new AdministrationLoadedTasksAction(response))), 3000);

        AdministrationRequester.getRunningTasks()
            .then(response => dispatch(new AdministrationLoadedTasksAction(response)));

        return () => clearInterval(intervalToRemove);
    }, [dispatch]);

    const cancelTask = (aRunningTask: Task) => {
        AdministrationRequester.deleteTask(aRunningTask)
            .then(response => console.log("taskCancel", response))
    }

    const resolveIcon = (aTask: Task): ReactElement => {
        if (aTask.supplierType === "KODI") {
            return <Display size={40} className={CustomClassNames.SpaceAfterIcon} />
        }

        if (aTask.supplierType === "TORRENT") {
            return <BoxSeam size={40} className={CustomClassNames.SpaceAfterIcon} />
        }

        return <Question />
    }


    if (administrationSubState.tasks.length === 0) {
        return <h1>Aucune tache en cours</h1>
    }




    return <div>
        <ListGroup data-bs-theme="light">
            {administrationSubState.tasks.map(aTask => {
                return <ListGroup.Item className="text-start">
                    <h2>
                        {resolveIcon(aTask)}
                        {aTask.title}
                        <Button variant="danger" className={CustomClassNames.PullRight} onClick={() => cancelTask(aTask)}><Trash /></Button></h2>
                    <ProgressBar
                        now={aTask.done}
                        label={`${aTask.labelDone} / ${aTask.labelTotal}`}
                        variant="info"
                    />

                </ListGroup.Item>
            })
            }
        </ListGroup>
    </div>
}

export default TasksComponent;