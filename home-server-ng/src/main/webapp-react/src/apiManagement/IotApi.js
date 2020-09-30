import { defaultJsonHeaders, errorHandler, apiRoot } from './HttpUtils';

export default function iotApi() {

    const getAllIotComponents = () => fetch(`${apiRoot()}/iot/components`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des iotComponents"))
        .catch(er => console.error(er));

    const getTriggersComponent = () => fetch(`${apiRoot()}/iot/components/triggers`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des triggers"))
        .catch(er => console.error(er));

    const getAllSprites = () => fetch(`${apiRoot()}/iot/minisprites`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des sprites"))
        .catch(er => console.error(er));

    const getAlarmStatus = () => fetch(`${apiRoot()}/iot/alarm/status`)
        .then(errorHandler("Erreur rencontrée lors de la récupération du status d'activation de l'alarme"))
        .catch(er => console.error(er));

    const updateAlarmStatus = (code) => {

        const alarmUpdateRequest = {
            enable: code === undefined,
            code: code
        }

        return fetch(`${apiRoot()}/iot/alarm`, {
            method: 'PUT',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(alarmUpdateRequest)
        }).then(errorHandler("mise à jour du status de l'alarme impossible"))
            .catch(er => console.error(er));
    };

    const updateIotComponents = (componentsToUpdate) => {
        const allUpdatePromises = componentsToUpdate.map(oneComponentToUpdate =>
            fetch(`${apiRoot()}/iot/components`, {
                method: 'PUT',
                headers: defaultJsonHeaders(),
                body: JSON.stringify(oneComponentToUpdate)
            }));

        return Promise.all(allUpdatePromises)
            .then(errorHandler("Erreur rencontrées lors de la mise à jour des composants", "Composants mis à jour"))
            .catch(er => console.error(er));
    }


    const updateSprite = (spriteToSave) => fetch(`${apiRoot()}/iot/minisprites`,
        {
            method: 'PUT',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(spriteToSave)
        })
        .then(errorHandler("Erreur rencontrée lors de la modification du sprite", "Sprite sauvegardé avec succès"))
        .catch(er => console.error(er));

    const createSprite = (spriteToSave) => fetch(`${apiRoot()}/iot/minisprites`,
        {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(spriteToSave)
        })
        .then(errorHandler("Erreur rencontrée lors de la création du sprite", "Sprite sauvegardé avec succès"))
        .catch(er => console.error(er));

    const sendBuzz = (buzzer, spriteId) => {
        console.log(spriteId);
        const request = {
            id: buzzer.componentDescriptor.id,
            ledTemplate: spriteId
        };
        return fetch(`${apiRoot()}/iot/components/buzzers`,
            {
                method: 'POST',
                headers: defaultJsonHeaders(),
                body: JSON.stringify(request)
            })
            .then(errorHandler("Erreur rencontrée lors de l'émission du buzz'", "Buzz émis"))
            .catch(er => console.error(er));
    }



    const deleteSprites = (spritesToDelete) => {
        const allDeletePromises = spritesToDelete.map(oneSpriteToDelete =>
            fetch(`${apiRoot()}/iot/minisprites/${oneSpriteToDelete.name}`, {
                method: 'DELETE',
                headers: defaultJsonHeaders()
            }));

        return Promise.all(allDeletePromises)
            .then(errorHandler("Erreur rencontrées lors de la suppression des sprites", "Sprites supprimés"))
            .catch(er => console.error(er));
    };

    const deleteOneComponent = (componentToDelete) => {
        return fetch(`${apiRoot()}/iot/components/${componentToDelete.id}`, {
            method: 'DELETE',
            headers: defaultJsonHeaders()
        }).then(errorHandler("Erreur rencontrée lors de la suppression du composant"))
        .catch(er => console.error(er));
    }

    return {
        getAllIotComponents: getAllIotComponents,
        getAllSprites: getAllSprites,
        updateSprite: updateSprite,
        createSprite: createSprite,
        deleteSprites: deleteSprites,
        sendBuzz: sendBuzz,
        updateIotComponents: updateIotComponents,
        getTriggersComponent: getTriggersComponent,
        getAlarmStatus: getAlarmStatus,
        updateAlarmStatus: updateAlarmStatus,
        deleteOneComponent: deleteOneComponent
    };
}