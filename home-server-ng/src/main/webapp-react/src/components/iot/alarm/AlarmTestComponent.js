import React from 'react';
import { useEffect, useState } from 'react';
import iotApi from '../../../apiManagement/IotApi';
import { when } from '../../../tools/when';
import toaster from '../../commons/Toaster';


export default function AlarmTestComponent() {


    const allAvailableStatus = {
        startup: {
            color: "blue",
            icon: "assignment_returned"
        },
        KO: {
            color: "red",
            icon: "assignment_late"
        },
        OK: {
            color: "green",
            icon: "assignment_turned_in"
        },
        WARNING: {
            color: "orange",
            icon: "assignment_turned_in"
        }
    }


    const [alarmTestResult, setAlarmTestResult] = useState(allAvailableStatus.startup);
    const [warnings, setWarnings] = useState([]);



    const getAlarmTestReport = () => {
        setWarnings([]);
        toaster()
        .plopAndWait(iotApi().getAlarmTestReport, "Test du système d'alarme en cours...")
        .then(response => {
            setAlarmTestResult(
                { ...allAvailableStatus[response.status] }
            );
            setWarnings(response.messages);
        });;
    }


    return <div>
        <div className="center-align">
            <button className={`btn btn-large waves-effect ${alarmTestResult.color}`} onClick={() => getAlarmTestReport()}><i class="material-icons left">{alarmTestResult.icon}</i>TESTER ALARME</button>
        </div>
        <ul className={when(warnings.length === 0).thenHideElement("collection with-header")}>
            <li className="collection-header"><h4>Problèmes rencontrés</h4></li>
            {warnings.map(oneWarning => (<li className="collection-item">{oneWarning}</li>))}
        </ul>
    </div>;
}