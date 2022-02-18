import React from 'react';
import { TASK_DELETE } from '../../eventReactor/EventIds';
import eventReactor from '../../eventReactor/EventReactor';
import IconComponent from '../commons/IconComponent';
import { ProgressBarComponent, range } from '../commons/ProgressBarComponent';

import "./RunningTaskRendererComponent.scss";

export default function RunningTaskRenderer({ runningTask }) {

    const dispatchDeleteEvent = () => eventReactor().emit(TASK_DELETE, runningTask);

    const taskIconResolver = (task) => {
        if (task.supplierType === "KODI") {
            return "ondemand_video";
        }
        if (task.supplierType === "TORRENT") {
            return "import_export";
        }
    }


    return <li className="collection-item">
        <div>
            <div className="title-and-action">
                <IconComponent icon={taskIconResolver(runningTask)} classAddons="left"></IconComponent>
                {runningTask.title}
                <span className="secondary-content">
                    <button className="btn btn-small btn-floating red" onClick={() => dispatchDeleteEvent()}>
                        <IconComponent icon='delete'></IconComponent>
                    </button>
                </span>
            </div>
            <ProgressBarComponent driver={
                {
                    colorScheme: "blue",
                    currentValue: runningTask.done,
                    ranges: [
                        range(0, 10, "orange"),
                        range(11, 99, "blue"),
                        range(100, 101, "green")],
                    labels: {
                        current: runningTask.labelDone,
                        max: runningTask.labelTotal
                    }
                }
            }>

            </ProgressBarComponent>
        </div>
    </li>
}