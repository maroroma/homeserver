import React from 'react';
import { useEffect, useState } from 'react';
import './AlarmPadComponent.scss';
import { when } from '../../../tools/when';
import eventReactor from '../../../eventReactor/EventReactor';
import iotApi from '../../../apiManagement/IotApi';
import toaster from '../../commons/Toaster';

export default function AlarmPadComponent() {

    const [alarmCode, setAlarmCode] = useState("");
    const valuesForButton = [[1, 2, 3, 4, 5], [6, 7, 8, 9, 0]];

    const appendToCode = code => setAlarmCode(alarmCode + code);

    const disarmAlarm = () => {
        setAlarmCode("");
        iotApi().updateAlarmStatus(alarmCode)
            .then(response => {
                if (!response) {
                    toaster().plopError("Code faux, alarme toujours active");
                } else {
                    toaster().plopSuccess("Alarme désactivée");
                }

                return iotApi().getAlarmStatus();
            })
            .then(response => eventReactor().shortcuts().alarmStatusChanged(response));
    }


    return <div>
        <input type="text" className="validate"
            value={alarmCode}
            placeholder="Code de désactivation pour l'alarme" disabled={true} />
        <div>
            <table className="alarm-pad">
                <thead></thead>
                <tbody>
                    {
                        valuesForButton.map((oneRow, index) => <tr key={index}>
                            {oneRow.map((oneCell, cellIndex) => <td key={cellIndex + "_" + index}>
                                <button className="btn waves-effect waves-green" onClick={event => appendToCode(oneCell)}>{oneCell}</button>
                            </td>)}
                        </tr>)
                    }
                </tbody>
            </table>
        </div>
        <div>
            <button className={when(alarmCode === "").thenDisableElement("btn btn-large red waves-effect waves-green")} onClick={() => disarmAlarm()}><i className="material-icons right">alarm_off</i>DESACTIVER ALARME</button>
        </div>
    </div>
}