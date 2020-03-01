import { useState, useEffect } from 'react';

export default function useAdministrationApi(onModuleLoaded) {


    const useDisplayableEnabledModules = (modulesLoaded) => {
        const [displayableEnabledModules, setDisplayableEnabledModules] = useState([]);
        useEffect(() => {
            fetch('/administration/enabledmodules')
                .then(response => response.json())
                .then(response => {
                    const enabledModuleWithClientSide = response.filter(oneModule => oneModule.hasClientSide);
                    if (modulesLoaded) {
                        modulesLoaded(enabledModuleWithClientSide);
                    }
                    setDisplayableEnabledModules(enabledModuleWithClientSide);
                })
        }
            , []);

        return displayableEnabledModules;
    }

    const useAllModules = () => {
        const [allModules, setAllModules] = useState([]);
        useEffect(() => {
            fetch('/administration/modules')
                .then(response => response.json())
                .then(response => {
                    setAllModules(response);
                })
                .catch(er => console.error(er))
        }
            , []);

        return [allModules, setAllModules];
    };

    return {
        useDisplayableEnabledModules: useDisplayableEnabledModules,
        useAllModules: useAllModules,
    };

}