import React, {useEffect, useState} from 'react';


import modulesAdapter from './ModulesAdapter';

import './MOFRouter.scss';
import mofRouterEventReactor from './MOFRouterEventReactor';
import {searchSubReactor} from './SearchBarComponent';

export default function MOFRouter() {

    const [selectedModule, setSelectedModule] = useState(modulesAdapter().homeMenuDescriptor())


    useEffect(() => {
        const selectedModuleFromPath = modulesAdapter().getMenuDescriptorForPath(window.location.hash.replace("#", ""));
        if (selectedModuleFromPath) {
            setSelectedModule(selectedModuleFromPath);
        }

        const unsubscribeOnSelectedModuleChange = mofRouterEventReactor().onSelectedModuleChange((moduleChangedEvent) => {
            const path = moduleChangedEvent.newSelectedModule.path;
            // const queryParams = (moduleChangedEvent.queryParams !== undefined) ? `${moduleChangedEvent.queryParams}`: '';
            const queryParams = (moduleChangedEvent.queryParams !== undefined) ? `?${moduleChangedEvent.queryParams}`: '';
            window.location.hash = path;
            window.location.search = queryParams;
            setSelectedModule(moduleChangedEvent.newSelectedModule);
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