import React from 'react';
import './App.css';
import { useEffect, useState } from 'react';
import MOFRouter from './components/mainmenu/MOFRouter';
import MainMenu from './components/mainmenu/MainMenu';
import eventReactor from './eventReactor/EventReactor';
import { DISPLAYABLE_MODULES_CHANGED } from './eventReactor/EventIds';

import { administrationApi } from './apiManagement/AdministrationApi';
import AlarmComponent from './components/iot/alarm/AlarmComponent';
import iotApi from './apiManagement/IotApi';
import { when } from './tools/when';
import AlarmPadComponent from './components/iot/alarm/AlarmPadComponent';

function App() {
  // gestion du status de l'alarme : tout est bloqué si l'alarme est active
  const [alarmStatus, setAlarmStatus] = useState(true);

  // premier appel permettant de récupérer la liste initiale des modules affichables par l'ihm
  useEffect(() => {

    const unsubscribeAlarmStatusChanged = eventReactor().shortcuts().onAlarmStatusChanged(newAlarmStatus => setAlarmStatus(newAlarmStatus));

    administrationApi().getAllEnabledModules()
      .then(allEnabledModules => allEnabledModules.filter(oneModule => oneModule.hasClientSide))
      .then(allDisplayableModules => eventReactor().emit(DISPLAYABLE_MODULES_CHANGED, allDisplayableModules))
      .then(response => iotApi().getAlarmStatus())
      .then(alarmStatus => eventReactor().shortcuts().alarmStatusChanged(alarmStatus));

    const intervalToRemove = setInterval(() =>
      iotApi().getAlarmStatus().then(alarmStatus => eventReactor().shortcuts().alarmStatusChanged(alarmStatus))
      , 5000);


    return () => {
      unsubscribeAlarmStatusChanged();
      clearInterval(intervalToRemove);
    }

  }, []);

  return <div>
    <div className={when(alarmStatus).thenHideElement()}>
      <MainMenu></MainMenu>
      <MOFRouter></MOFRouter>
    </div>
    <div className={when(!alarmStatus).thenHideElement("alarmEnabledContainer center-align")}>
      <AlarmPadComponent></AlarmPadComponent>
    </div>
  </div>
}

export default App;
