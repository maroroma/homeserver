import React from 'react';
import './App.css';
import { useEffect } from 'react';
import MOFRouter from './components/mainmenu/MOFRouter';
import MainMenu from './components/mainmenu/MainMenu';
import eventReactor from './eventReactor/EventReactor';
import { DISPLAYABLE_MODULES_CHANGED } from './eventReactor/EventIds';

import { administrationApi } from './apiManagement/AdministrationApi';

function App() {

  // premier appel permettant de récupérer la liste initiale des modules affichables par l'ihm
  useEffect(() => {
    administrationApi().getAllEnabledModules()
      .then(allEnabledModules => allEnabledModules.filter(oneModule => oneModule.hasClientSide))
      .then(allDisplayableModules => eventReactor().emit(DISPLAYABLE_MODULES_CHANGED, allDisplayableModules));
  }, []);

  return (
    <div>
      <MainMenu></MainMenu>
      <MOFRouter></MOFRouter>
    </div>
  );
}

export default App;
