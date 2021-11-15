import { defaultJsonHeaders, errorHandler, apiRoot } from './HttpUtils';

export default function legoApi() {
    const getAllBricks = () =>
        fetch(`${apiRoot()}/lego/bricks`)
            .then(errorHandler("Erreur lors de la récupération des briques"))
            .catch(er => console.error(er));

    const addBrick = (newBrick) => {

        return fetch(`${apiRoot()}/lego/bricks`,
            {
                method: 'POST',
                headers: defaultJsonHeaders(),
                body: JSON.stringify(newBrick)
            })
            .then(errorHandler("Erreur rencontrée lors de la demande d'ajout de brique", "Brique ajoutée"))
            .catch(er => console.error(er));
    }


    const updateBricks = (updatedBricks) => {

        return fetch(`${apiRoot()}/lego/bricks`, {
            method: 'PUT',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(updatedBricks)
        })
            .then(errorHandler("Erreur rencontrée lors de la mise à jour des briques",
                "Briques mises à jour"))
            .catch(er => console.error(er));
    }

    const deleteBrick = (brickToDelete) => {
        return fetch(`${apiRoot()}/lego/bricks/${brickToDelete.id}`, {
            method: 'DELETE',
            headers: defaultJsonHeaders()
        }).then(errorHandler("Erreur rencontrée lors de la suppression d'une brique", "brique supprimée"))
            .catch(er => console.error(er));
    }

    const downloadBaseUrl = () => {
        return `${apiRoot()}/lego/bricks`;
    }


    return {
        getAllBricks: getAllBricks,
        addBrick: addBrick,
        downloadBaseUrl: downloadBaseUrl,
        updateBricks: updateBricks,
        deleteBrick: deleteBrick
    }
}