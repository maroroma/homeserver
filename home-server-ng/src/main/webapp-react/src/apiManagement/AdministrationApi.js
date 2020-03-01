export function administrationApi() {
    const updateModulesStatus = (updateModules) => {
        const request = updateModules.map(oneUpdatedModule => {
            return {
                id: oneUpdatedModule.moduleId,
                enabled: oneUpdatedModule.enabled
            }
        });


        return fetch('/administration/modules', {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        })
            .then(response => response.json())
            .catch(er => console.error(er));
    }

    const getAllModule = () =>
        fetch('/administration/modules')
            .then(response => response.json())
            .catch(er => console.error(er));

    const getAllEnabledModules = () => fetch('/administration/enabledmodules')
        .then(response => response.json())
        .catch(er => console.error(er));

    return {
        updateModulesStatus: updateModulesStatus,
        getAllModule: getAllModule,
        getAllEnabledModules: getAllEnabledModules
    }
}