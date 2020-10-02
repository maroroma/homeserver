import React from 'react';
import iotApi from '../../../apiManagement/IotApi';
import eventReactor from '../../../eventReactor/EventReactor';
import AlarmTestComponent from './AlarmTestComponent';

import "./AlarmComponent.scss";

export default function AlarmComponent() {

    const armAlarm = () => {
        iotApi().updateAlarmStatus().then(response => eventReactor().shortcuts().alarmStatusChanged(response));
    }

    return <div>
        <div className="center-align button-grid">
            <button className="btn btn-large waves-effect waves-green" onClick={() => armAlarm()}><i class="material-icons left">alarm_on</i>ACTIVER ALARME</button>
        </div>
        <div className="button-grid">
            <AlarmTestComponent></AlarmTestComponent>
        </div>
    </div>
}