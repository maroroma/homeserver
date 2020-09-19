import React from 'react';
import { useEffect, useState } from 'react';


import modulesAdapter from './ModulesAdapter';

import './MOFRouter.scss';
import mofRouterEventReactor from './MOFRouterEventReactor';
import { searchSubReactor } from './SearchBarComponent';

export default function MOFRouter() {

    const [selectedModule, setSelectedModule] = useState(modulesAdapter().homeMenuDescriptor())


    useEffect(() => {
        const selectedModuleFromPath = modulesAdapter().getMenuDescriptorForPath(window.location.hash.replace("#", ""));
        if (selectedModuleFromPath) {
            setSelectedModule(selectedModuleFromPath);
        }

        const unsubscribeOnSelectedModuleChange = mofRouterEventReactor().onSelectedModuleChange(newSelectedModule => {
            window.location.hash = newSelectedModule.path;
            setSelectedModule(newSelectedModule);
            searchSubReactor().clearSearchBar();
        });

        return () => {
            unsubscribeOnSelectedModuleChange();
        }


    }, []);

    return (
        <>
            <div className={selectedModule.dontUseDefaultPanel ? "" : "current-module"} >
                {selectedModule.component}
            </div>
        </>
    );
}