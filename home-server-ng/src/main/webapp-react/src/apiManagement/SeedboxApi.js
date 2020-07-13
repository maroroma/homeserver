import { defaultJsonHeaders, errorHandler, apiRoot } from './HttpUtils';

export default function seedboxApi() {

    const getRunningTorrents = () => fetch(`${apiRoot()}/seedbox/torrents`)
        .then(errorHandler("Erreur rencontrée lors de la récupération torrents en cours"))
        .catch(er => console.error(er));

    const getCompletedTorrents = () => fetch(`${apiRoot()}/seedbox/todo/completedtorrents`)
        .then(errorHandler("Erreur rencontrée lors de la récupération torrents complétés"))
        .catch(er => console.error(er));

    const getTargetDirectories = () => fetch(`${apiRoot()}/seedbox/todo/targets`)
        .then(errorHandler("Erreur rencontrée lors de la récupération répertoires cibles"))
        .catch(er => console.error(er));

    const getSubFolderContent = (currentFolder) => fetch(`${apiRoot()}/seedbox/todo/targets/${currentFolder.id}/files`)
        .then(errorHandler("Erreur rencontrée lors de la récupération du contenu du répertoire"))
        .catch(er => console.error(er));


    const moveFiles = (moveRequest) => fetch(`${apiRoot()}/seedbox/todo/sortedfile`,
        {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(moveRequest)
        })
        .then(errorHandler("Erreur rencontrée lors du tri des fichiers", "Les fichiers ont été déplacés avec succès"))
        .catch(er => console.error(er));

    const addTorrent = (magnetLinks) => {

        const addRequest = {
            magnetLinks: magnetLinks
        }

        return fetch(`${apiRoot()}/seedbox/torrents`,
            {
                method: 'POST',
                headers: defaultJsonHeaders(),
                body: JSON.stringify(addRequest)
            })
            .then(errorHandler("Erreur rencontrée lors de la demande d'ajout de torrent", "Demande d'ajout de torrent émise"))
            .catch(er => console.error(er));
    }

    const removeTorrents = (torrentList) => {
        const removeRequest = {
            idsToDelete: torrentList.map(oneTorrent => oneTorrent.id)
        }

        return fetch(`${apiRoot()}/seedbox/torrents`,
            {
                method: 'DELETE',
                headers: defaultJsonHeaders(),
                body: JSON.stringify(removeRequest)
            })
            .then(errorHandler("Erreur rencontrée lors de la demande de suppression de torrent", "Demande de suppression de torrent émise"))
            .catch(er => console.error(er));
    }


    return {
        getRunningTorrents: getRunningTorrents,
        getCompletedTorrents: getCompletedTorrents,
        getTargetDirectories: getTargetDirectories,
        getSubFolderContent: getSubFolderContent,
        moveFiles: moveFiles,
        addTorrent: addTorrent,
        removeTorrents: removeTorrents
    };
}