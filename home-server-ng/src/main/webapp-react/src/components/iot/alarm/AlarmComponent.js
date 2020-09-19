import React from 'react';
import iotApi from '../../../apiManagement/IotApi';
import eventReactor from '../../../eventReactor/EventReactor';


export default function AlarmComponent() {

    const armAlarm = () => {
        iotApi().updateAlarmStatus().then(response => eventReactor().shortcuts().alarmStatusChanged(response));
    }

    return <div className="center-align">
        <button className="btn btn-large waves-effect waves-green" onClick={() => armAlarm()}><i class="material-icons right">alarm_on</i>ACTIVER ALARME</button>
    </div>
}