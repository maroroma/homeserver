import { defaultJsonHeaders, errorHandler, apiRoot } from './HttpUtils';

export function administrationApi() {
    const updateModulesStatus = (updateModules) => {
        const request = updateModules.map(oneUpdatedModule => {
            return {
                id: oneUpdatedModule.moduleId,
                enabled: oneUpdatedModule.enabled
            }
        });


        return fetch(`${apiRoot()}/administration/modules`, {
            method: 'PATCH',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler("Erreur rencontrée lors de la mise à jour des modules",
                "Modules mis à jour"))
            .catch(er => console.error(er));
    }

    const updateProperties = (propertiesToUpdate) => {
        const allUpdatePromises = propertiesToUpdate.map(onePropertyToUpdate =>
            fetch(`${apiRoot()}/administration/config/${onePropertyToUpdate.id}`, {
                method: 'PATCH',
                headers: defaultJsonHeaders(),
                body: JSON.stringify(onePropertyToUpdate)
            }));

        return Promise.all(allUpdatePromises)
            .then(errorHandler("Erreur rencontrées lors de la mise à jour des propriétés", "Propriétés mises à jour"))
            .catch(er => console.error(er));
    }



    const getRunningTasks = () =>
        fetch(`${apiRoot()}/administration/tasks`)
            .then(errorHandler("Erreur rencontrée lors de la récupération des taches en cours"))
            .catch(er => console.error(er));

    const cancelTask = (taskToCancel) => {

        const request = {
            supplierType: taskToCancel.supplierType,
            taskId: taskToCancel.id
        }


        return fetch(`${apiRoot()}/administration/tasks`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler(`Erreur rencontrée lors de l'annulation de la tache ${taskToCancel.title}`,
                `Annulation de la tache ${taskToCancel.title} demandée`))
            .catch(er => console.error(er));
    }



    const getAllModule = () =>
        fetch(`${apiRoot()}/administration/modules`)
            .then(errorHandler("Erreur rencontrée lors de la récupération des modules"))
            .catch(er => console.error(er));

    const getAllLogEvents = () => fetch(`${apiRoot()}/administration/logEvents`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des logEvents", "logEvents récupérés"))
        .catch(er => console.error(er));

    const deleteRepo = (repoId) => fetch(`${apiRoot()}/administration/repo/${repoId}`, {
        method: 'DELETE',
        headers: defaultJsonHeaders()
    })
        .then(errorHandler("Erreur rencontrée lors de la purge des logsEvents", "Tous les logs ont été supprimés"))
        .catch(er => console.error(er));

    const getAllEnabledModules = () => fetch(`${apiRoot()}/administration/enabledmodules`)
        .then(response => response.json())
        .catch(er => console.error(er));

    const getAllProperties = () =>
        fetch(`${apiRoot()}/administration/configs`)
            .then(response => response.json())
            .catch(er => console.error(er));

    const getServerStatus = () => fetch(`${apiRoot()}/administration/server/status`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des stats du serveur"))
        .catch(er => console.error(er));

    const getOneProperty = (propertyName) => fetch(`${apiRoot()}/administration/configs/${propertyName}`)
        .then(errorHandler(`Erreur rencontrée lors de la récupération de la propriété ${propertyName}`))
        .catch(er => console.error(er));


    return {
        updateModulesStatus: updateModulesStatus,
        getAllModule: getAllModule,
        getAllEnabledModules: getAllEnabledModules,
        getAllProperties: getAllProperties,
        updateProperties: updateProperties,
        getServerStatus: getServerStatus,
        getOneProperty: getOneProperty,
        getAllLogEvents: getAllLogEvents,
        deleteRepo: deleteRepo,
        getRunningTasks: getRunningTasks,
        cancelTask: cancelTask
    }
}