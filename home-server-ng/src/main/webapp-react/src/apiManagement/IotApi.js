import { defaultJsonHeaders, errorHandler, apiRoot } from './HttpUtils';

export default function iotApi() {

    const getAllIotComponents = () => fetch(`${apiRoot()}/iot/components`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des iotComponents"))
        .catch(er => console.error(er));

    const getAllSprites = () => fetch(`${apiRoot()}/iot/minisprites`)
        .then(errorHandler("Erreur rencontrée lors de la récupération des sprites"))
        .catch(er => console.error(er));



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

    return {
        getAllIotComponents: getAllIotComponents,
        getAllSprites: getAllSprites,
        updateSprite: updateSprite,
        createSprite: createSprite,
        deleteSprites: deleteSprites
    };
}