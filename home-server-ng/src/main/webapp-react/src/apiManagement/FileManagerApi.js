import { defaultJsonHeaders, errorHandler,apiRoot } from './HttpUtils';


export default function fileManagerApi() {

    const getRootDirectories = () =>
        fetch(`${apiRoot()}/filemanager/rootdirectories`)
            .then(errorHandler("Erreur lors de la récupération des répertoires racines"))
            .catch(er => console.error(er));

    const getDirectoryDetails = (directoryToLoad) =>
        fetch(`${apiRoot()}/filemanager/directories/${directoryToLoad.id}`)
            .then(errorHandler(
                `Erreur de récupération du répertoire ${directoryToLoad.name}`
            ))
            .catch(er => console.error(er));

    const createNewDirectory = (currentDirectory, newDirectoryName) => {
        const request = {
            parentDirectory: currentDirectory,
            directoryName: newDirectoryName
        };

        return fetch(`${apiRoot()}/filemanager/directory`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler(`Erreur rencontrée lors de la création du répertoire ${newDirectoryName}`,
                `Répertoire ${newDirectoryName} créé`))
            .catch(er => console.error(er));


    }

    const deleteFiles = (filesToDelete) => {
        const allDeletePromises = filesToDelete.map(oneFileToDelete =>
            fetch(`${apiRoot()}/filemanager/files/${oneFileToDelete.id}`, {
                method: 'DELETE',
                headers: defaultJsonHeaders()
            }));

        return Promise.all(allDeletePromises)
            .then(errorHandler("Erreur rencontrées lors de la suppression des fichiers", "Fichiers supprimés"))
            .catch(er => console.error(er));
    };

    const renameFile = (fileToRename, fileNewName) => {
        const request = {
            originalFile: fileToRename,
            newName: fileNewName
        };

        return fetch(`${apiRoot()}/filemanager/files`, {
            method: 'PATCH',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler(`Erreur rencontrée lors du renommage du fichier ${fileNewName}`,
                `Fichier ${fileNewName} renommé`))
            .catch(er => console.error(er));

    };

    const downloadBaseUrl = () => {
        return `${apiRoot()}/filemanager/files`;
    }


    return {
        getRootDirectories: getRootDirectories,
        getDirectoryDetails: getDirectoryDetails,
        createNewDirectory: createNewDirectory,
        deleteFiles: deleteFiles,
        renameFile: renameFile,
        downloadBaseUrl:downloadBaseUrl
    };

}