import React from 'react';
import './App.css';
import { useState, useEffect } from 'react';
import modulesAdapter from './components/mainmenu/ModulesAdapter';
import MOFRouter from './components/mainmenu/MOFRouter';
import MainMenu from './components/mainmenu/MainMenu';
import eventReactor from './eventReactor/EventReactor';
import { DISPLAYABLE_MODULES_CHANGED } from './eventReactor/EventIds';

import { administrationApi } from './apiManagement/AdministrationApi';

function App() {

  const [selectedModule, setSelectedModule] = useState(modulesAdapter().homeMenuDescriptor());


  // premier appel permettant de récupérer la liste initiale des modules affichables par l'ihm
  useEffect(() => {
    administrationApi().getAllEnabledModules()
      .then(allEnabledModules => allEnabledModules.filter(oneModule => oneModule.hasClientSide))
      .then(allDisplayableModules => eventReactor().emit(DISPLAYABLE_MODULES_CHANGED, allDisplayableModules));
  }, []);

  return (
    <div>
      <MainMenu onMenuItemSelected={(newSelectedModuleDescriptor) => setSelectedModule(newSelectedModuleDescriptor)}></MainMenu>
      <MOFRouter selectedPath={selectedModule.path}></MOFRouter>
    </div>
  );
}

export default App;
