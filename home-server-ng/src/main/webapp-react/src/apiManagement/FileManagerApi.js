import {apiRoot, defaultJsonHeaders, errorHandler, errorHandlerWithoutJson} from './HttpUtils';


export default function fileManagerApi() {

    const getRootDirectories = () =>
        fetch(`${apiRoot()}/filemanager/rootdirectories`)
            .then(errorHandler("Erreur lors de la récupération des répertoires racines"))
            .catch(er => console.error(er));

    const getDirectoryDetails = (directoryToLoad) =>
        fetch(`${apiRoot()}/filemanager/directories/${directoryToLoad.id}`)
            .then(errorHandler(
                `Erreur de récupération du répertoire ${directoryToLoad.name}`
            ));

    const createNewDirectory = (currentDirectory, newDirectoryName) => {

        const request = {
            parentDirectory: { id: currentDirectory.id },
            directoryName: newDirectoryName
        };

        return fetch(`${apiRoot()}/filemanager/directory`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler(`Erreur rencontrée lors de la création du répertoire ${newDirectoryName}`,
                `Répertoire ${newDirectoryName} créé`));


    }

    const deleteFiles = (filesToDelete) => {
        const allDeletePromises = filesToDelete.map(oneFileToDelete =>
            fetch(`${apiRoot()}/filemanager/files/${oneFileToDelete.id}`, {
                method: 'DELETE',
                headers: defaultJsonHeaders()
            }));

        return Promise.all(allDeletePromises)
            .then(errorHandlerWithoutJson("Erreur rencontrées lors de la suppression des fichiers", "Fichiers supprimés"))
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
                `Fichier ${fileNewName} renommé`));

    };

    const uploadFiles = (targetDirectory, filesToUpload) => {

        const request = new FormData();
        filesToUpload.forEach(oneFile => request.append("file", oneFile));

        return fetch(`${apiRoot()}/filemanager/files/${targetDirectory.id}`, {
            method: 'POST',
            // headers: defaultJsonHeaders(),
            body: request
        })
            .then(errorHandler(`Erreur rencontrée lors l'upload des fichiers`,
                `Fichiers uploadés`));


    }

    const downloadBaseUrl = () => {
        return `${apiRoot()}/filemanager/files`;
    }

    const getRootDirectoriesConfiguration = () => {
        return fetch(`${apiRoot()}/filemanager/configuration/rootDirectories`)
            .then(errorHandler("Erreur lors de la récupération des répertoires racines"))
    }

    const addNewDirectoryConfiguration = (rawPath) => {
        const request = {
            rawPath: rawPath
        };

        return fetch(`${apiRoot()}/filemanager/configuration/rootDirectories`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler(`Erreur rencontrée lors de l'ajout de la nouvelle racine'`,
                `Nouvelle racine créée`));
    }


    const updateRootDirectoryConfiguration = (rootDirectoryConfiguration) => {

        return fetch(`${apiRoot()}/filemanager/configuration/rootDirectories/${rootDirectoryConfiguration.id}`, {
            method: 'PATCH',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(rootDirectoryConfiguration)
        })
            .then(errorHandler(`Erreur rencontrée de la mise à jour de la racine ${rootDirectoryConfiguration.rawPath}`,
                `Racine ${rootDirectoryConfiguration.rawPath} modifié`));

    };

    const deleteRootDirectoryConfiguration = (rootDirectoryConfiguration) => {
        return fetch(`${apiRoot()}/filemanager/configuration/rootDirectories/${rootDirectoryConfiguration.id}`, {
            method: 'DELETE',
            headers: defaultJsonHeaders()
        }).then(errorHandler(`Erreur rencontrée de la suppression de la racine ${rootDirectoryConfiguration.rawPath}`,
            `Racine ${rootDirectoryConfiguration.rawPath} supprimée`));
    }



    return {
        getRootDirectories: getRootDirectories,
        getDirectoryDetails: getDirectoryDetails,
        createNewDirectory: createNewDirectory,
        deleteFiles: deleteFiles,
        renameFile: renameFile,
        downloadBaseUrl: downloadBaseUrl,
        uploadFiles: uploadFiles,
        getRootDirectoriesConfiguration: getRootDirectoriesConfiguration,
        addNewDirectoryConfiguration: addNewDirectoryConfiguration,
        updateRootDirectoryConfiguration: updateRootDirectoryConfiguration,
        deleteRootDirectoryConfiguration: deleteRootDirectoryConfiguration
    };

}