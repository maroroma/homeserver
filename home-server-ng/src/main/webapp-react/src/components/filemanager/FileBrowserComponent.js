import React from 'react';
import { useEffect, useState } from 'react';
import DirectoryRenderer from "./DirectoryRenderer"
import FileRenderer from "./FileRenderer"
import "./FileBrowserComponent.scss"
import "../commons/Common.scss"
import eventReactor from '../../eventReactor/EventReactor';
import {
    FILE_BROWSER_SELECT_FILE, FILE_BROWSER_CHANGE_CURRENT_DIRECTORY,
    FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, FILE_BROWSER_DIRECTORY_DETAIL_LOADED,
    FILE_BROWSER_CREATE_NEW_DIRECTORY,
    FILE_BROWSER_DELETE_FILES,
    FILE_BROWSER_RENAME_ONE_FILE
} from '../../eventReactor/EventIds';

import { when } from "../../tools/when";

import { ModalPopupComponent, usePopupDriver } from "../commons/ModalPopupComponent";
import YesNoModalPopupComponent from "../commons/YesNoModalPopupComponent";


export default function FileBrowserComponent({ startUpDirectory, downloadBaseUrl }) {

    // répertoire à afficher
    const [currentDirectory, setCurrentDirectory] = useState({});
    // liste des répertoire dans la navbar
    const [directoryStack, setDirectoryStack] = useState([]);
    // menu d'action
    const [actionMenu, setActionMenu] = useState({});





    // fonction de tri pour les fichiers
    const sortFiles = (file1, file2) => file1.name.localeCompare(file2.name);

    // fonction de filtrage sur les fichiers sélectionnés
    const onSelectedFile = oneFile => oneFile.selected;

    // fonction de filtrage pour la recherche
    // const onSearchString = oneFile => oneFile.name

    // rajout d'attributs VO pour les fichiers
    const enhanceFiles = (file) => {
        return { ...file, selected: false }
    };

    const updateOneFileSelection = (newStatus) => {
        return (oneFile) => {
            oneFile.selected = newStatus;
            return oneFile;
        }
    };
    const updateAllFileSelection = (newStatus) => {
        setCurrentDirectory({
            ...currentDirectory,
            files: currentDirectory.files.map(updateOneFileSelection(newStatus)),
            directories: currentDirectory.directories.map(updateOneFileSelection(newStatus)),
            hasSelectedFiles: newStatus,
            nbSelectedFiles: currentDirectory.files.length + currentDirectory.directories.length
        });
    };

    const updateFileSelectedStatus = (fileList, fileIdWithStatus) => {
        if (fileList) {
            const renewedFile = fileList
                .filter(oneFile => oneFile.id === fileIdWithStatus.fileId)
                .map(oneFile => {
                    return {
                        ...oneFile,
                        selected: fileIdWithStatus.newStatus
                    }
                })[0];

            return [
                renewedFile,
                ...fileList
                    .filter(oneFile => oneFile.id !== fileIdWithStatus.fileId)
            ].filter(oneFile => oneFile).sort(sortFiles);
        }
        return fileList;
    };


    const onDirectoryStackClickHandler = (directorySelected) => {
        eventReactor().emit(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, directorySelected);
    };

    const onDirectoryUpdateHandler = (directoryToRefresh) => {
        eventReactor().emit(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, {
            requestedDirectory: directoryToRefresh
        });
    }

    /**
     * Gestion du menu
     */
    useEffect(() => {
        var elems = document.querySelectorAll('.fixed-action-btn');
        var instances = window.M.FloatingActionButton.init(elems, {
            direction: 'left',
            hoverEnabled: false
        });

        setActionMenu(instances);

        return () => {
            instances.forEach(oneInstance => oneInstance.destroy());
        }

    }, []);

    /**
     * Gestion du chargement du nouveau répertoire sélectionné (sur event)
     */
    useEffect(() =>
        eventReactor()
            .subscribe(FILE_BROWSER_DIRECTORY_DETAIL_LOADED,
                newDirectory => {

                    let newCurrentDirectory = newDirectory.directoryToDisplay;

                    if (directoryStack[directoryStack.length - 1].id !== newCurrentDirectory.id) {
                        let directoryAlreadyInStack = undefined;
                        const newDirectoryStack = directoryStack
                            .filter(oneDirectoryInStack => {
                                if (directoryAlreadyInStack) {
                                    return false;
                                }
                                if (oneDirectoryInStack.id === newCurrentDirectory.id) {
                                    directoryAlreadyInStack = oneDirectoryInStack;
                                }
                                return true;
                            });

                        if (directoryAlreadyInStack === undefined) {
                            newDirectoryStack.push(newCurrentDirectory)
                        }

                        setDirectoryStack(newDirectoryStack);
                    }

                    setCurrentDirectory({
                        ...newCurrentDirectory,
                        files: newCurrentDirectory.files?.map(enhanceFiles).sort(sortFiles),
                        directories: newCurrentDirectory.directories?.map(enhanceFiles).sort(sortFiles),
                        hasSelectedFiles: false,
                        nbSelectedFiles: 0
                    });

                })

        , [directoryStack]);


    /**
     * première initialisation, à partir du startupDirectory
     */
    useEffect(() => {
        setCurrentDirectory({
            ...startUpDirectory,
            files: startUpDirectory.files?.map(enhanceFiles).sort(sortFiles),
            directories: startUpDirectory.directories?.map(enhanceFiles).sort(sortFiles),
            hasSelectedFiles: false,
            nbSelectedFiles: 0
        });
        setDirectoryStack([startUpDirectory]);
    }, [startUpDirectory]);



    /**
     * Gestion des events selection et nouveau directory à afficher
     */
    useEffect(() => {
        const selectFileUnsubcribe = eventReactor().subscribe(FILE_BROWSER_SELECT_FILE, data => {

            const updatedFiles = updateFileSelectedStatus(currentDirectory.files, data);
            const updatedDirectories = updateFileSelectedStatus(currentDirectory.directories, data);
            const nbSelectedFiles = updatedFiles
                .concat(updatedDirectories)
                .filter(onSelectedFile).length;

            setCurrentDirectory({
                ...currentDirectory,
                files: updatedFiles,
                directories: updatedDirectories,
                hasSelectedFiles: nbSelectedFiles > 0,
                nbSelectedFiles: nbSelectedFiles
            });

            if (nbSelectedFiles > 0) {
                console.log("should open menu");
                actionMenu.forEach(oneMenu => oneMenu.open());
            } else {
                actionMenu.forEach(oneMenu => oneMenu.close());
            }
        });

        const changeCurrentDirectoryUnSubscribe = eventReactor().subscribe(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, data => {
            eventReactor().emit(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, {
                requestedDirectory: data
            });
        });

        return () => {
            selectFileUnsubcribe();
            changeCurrentDirectoryUnSubscribe();
        }

    }, [currentDirectory, directoryStack]);

    // gestion du retour arrière dans liste de fichiers
    const parentDirectory = directoryStack.length > 1 ? {
        ...directoryStack[directoryStack.length - 2],
        name: ".."
    } : undefined;

    // composant graphique correspondant
    const goToParentDirectoryComponent = parentDirectory ? <DirectoryRenderer directory={parentDirectory} disabled={true} icon="keyboard_return"></DirectoryRenderer> : undefined;


    // fonction de filtrage pour l'activation des boutons du menu
    const noFileOrDirectorySelected = () => !currentDirectory?.hasSelectedFiles;
    const noFileSelected = () => currentDirectory?.files?.filter(onSelectedFile).length === 0;
    const allFilesSelected = () => currentDirectory?.nbSelectedFiles === (currentDirectory?.files?.length + currentDirectory?.directories?.length);
    const moreThanOneFileSelected = () => currentDirectory.nbSelectedFiles > 1;
    const currentIsRoot = () => currentDirectory?.id === startUpDirectory.id;

    // gestion de la popup de création de dossier
    // pilotage la popup création d'un répertoire
    const [popupDriver, setPopupDriver] = usePopupDriver({
        id: 'popupCreateFolder',
        title: 'Créer un nouveau répertoire',
        okLabel: 'Ok',
    });

    // ouverture de la popup de création
    const openPopupForCreateFolder = (parentDirectory) => {
        setPopupDriver({
            ...popupDriver,
            open: true,
            data: {
                parentDirectory: parentDirectory,
                newDirectoryName: ''
            },
            updateData: (currentDriver, newData) => {
                setPopupDriver({
                    ...currentDriver,
                    data: { newDirectoryName: newData }
                })
            },
            onOk: (currentPopupProvider) => {
                eventReactor().emit(FILE_BROWSER_CREATE_NEW_DIRECTORY, {
                    currentDirectory: currentDirectory,
                    newDirectoryName: currentPopupProvider.data.newDirectoryName
                });
            }
        });
    };

    // gestion de la popup de renommage de fichier
    // pilotage la popup renommmage d'un fichier
    const [renamePopupDriver, setRenamePopupDriver] = usePopupDriver({
        id: 'popupRenameFile',
        title: 'Renommer un fichier',
        okLabel: 'Renommer'
    });

    const openPopupRename = () => {
        const selectedFileToRename = currentDirectory.files
            .filter(onSelectedFile)
            .concat(currentDirectory.directories.filter(onSelectedFile))
            .filter(oneFile => oneFile !== undefined)
        [0];

        setRenamePopupDriver({
            ...renamePopupDriver,
            open: true,
            data: {
                fileToRename: selectedFileToRename,
                fileNewName: selectedFileToRename.name
            },
            updateData: (currentPopupProvider, newData) => {
                setRenamePopupDriver({
                    ...currentPopupProvider,
                    data: {
                        ...currentPopupProvider.data,
                        fileNewName: newData
                    }
                })
            },
            onOk: (currentPopupProvider) => {
                eventReactor().emit(FILE_BROWSER_RENAME_ONE_FILE, {
                    ...currentPopupProvider.data,
                    currentDirectory: currentDirectory
                });
            }
        });

    };


    // gestion suppression fichier
    // pilotage suppression d'un fichier
    const [yesNoPopupDriver, setYesNoPopupDriver] = useState({
        title: 'Suppression',
        yesNoQuestion: 'Voulez-vous vraiment supprimer ce(s) fichier(s) ?'
    });

    const openDeleteFilePopup = () => {
        setYesNoPopupDriver({
            ...yesNoPopupDriver,
            open: true,
            onOk: () => {
                const filesToDelete = currentDirectory.files.filter(oneFile => oneFile.selected)
                    .concat(currentDirectory.directories.filter(oneFile => oneFile.selected));
                eventReactor().emit(FILE_BROWSER_DELETE_FILES, {
                    currentDirectory: currentDirectory,
                    filesToDelete: filesToDelete
                });
            }
        });
    };

    // gestion du téléchargement
    const [downloadPopupDriver, setDownloadPopupDriver] = usePopupDriver({
        id: 'popupDownload',
        title: 'Téléchargement de fichier(s)',
        noCancelButton: true
    });

    const openPopupDownload = () => {
        setDownloadPopupDriver({ ...downloadPopupDriver, open: true });
    }


    return (
        <div>
            <nav className="directory-stack pink accent-3">
                <div className="nav-wrapper">
                    <div className="col s12">
                        {directoryStack.map((oneDirectory, stackIndex) => (
                            <a key={stackIndex} href="#!" className="breadcrumb" onClick={() => onDirectoryStackClickHandler(oneDirectory)}>
                                {oneDirectory.name}
                            </a>))}
                    </div>
                </div>
            </nav>


            <ul className="collection">
                {goToParentDirectoryComponent}
                {currentDirectory?.directories?.map((oneDirectory, oneDirectoryIndex) => <DirectoryRenderer directory={oneDirectory} key={oneDirectoryIndex}></DirectoryRenderer>)}
                {currentDirectory?.files?.map((oneFile, oneFileIndex) => <FileRenderer file={oneFile} key={oneFileIndex}>{oneFile.name}</FileRenderer>)}
            </ul>


            <div className="fixed-action-btn">
                <a href="#!" className="btn-floating btn-large red">
                    <i className="large material-icons">menu</i>
                </a>
                <ul>
                    <li className={when(noFileOrDirectorySelected).or(currentIsRoot).thenHideElement()}><a href="#!" className="btn-floating btn-small red" onClick={openDeleteFilePopup}><i className="material-icons">delete</i></a></li>
                    <li className={when(noFileOrDirectorySelected).or(currentIsRoot).or(moreThanOneFileSelected).thenHideElement()}><a href="#!" className="btn-floating btn-small green" onClick={openPopupRename}><i className="material-icons">edit</i></a></li>
                    <li><a href="#!" className={when(currentIsRoot).thenDisableElement("btn-floating btn-small green")} onClick={() => openPopupForCreateFolder(currentDirectory)}><i className="material-icons">create_new_folder</i></a></li>
                    <li><a href="#!" className={when(currentIsRoot).or(noFileSelected).thenDisableElement("btn-floating btn-small green")} onClick={() => openPopupDownload()}><i className="material-icons">file_download</i></a></li>
                    <li><a href="#!" className={when(currentIsRoot).thenDisableElement("btn-floating btn-small green")} onClick={() => openPopupForCreateFolder(currentDirectory)}><i className="material-icons">file_upload</i></a></li>
                    <li className={when(allFilesSelected).thenHideElement()}><a href="#!" className="btn-floating btn-small blue" onClick={() => updateAllFileSelection(true)}><i className="material-icons">check_box</i></a></li>
                    <li><a href="#!" className="btn-floating btn-small blue" onClick={() => onDirectoryUpdateHandler(currentDirectory)}><i className="material-icons">sync</i></a></li>
                </ul>
            </div>

            <ModalPopupComponent popupId="popupCreateFolder" driver={popupDriver}>
                <div className="input-field">
                    <input id="propertyToEdit" type="text" className="validate" value={popupDriver.data?.newDirectoryName} onChange={(event) => popupDriver.updateData(popupDriver, event.target.value)}></input>
                </div>
            </ModalPopupComponent>

            <ModalPopupComponent popupId="popupRename" driver={renamePopupDriver}>
                <div className="input-field">
                    <input id="newName" type="text" className="validate" value={renamePopupDriver.data?.fileNewName} onChange={(event) => renamePopupDriver.updateData(renamePopupDriver, event.target.value)}></input>
                </div>
            </ModalPopupComponent>

            <ModalPopupComponent popupId="popupDownload" driver={downloadPopupDriver}>
                <ul className="collection">
                    {currentDirectory.files?.filter(onSelectedFile).map((oneFile, index) => (
                        <li className="collection-item" key={index}><a download={oneFile.name} href={`${downloadBaseUrl}/${oneFile.id}`}>{oneFile.name}</a></li>
                    ))}
                </ul>
            </ModalPopupComponent>

            <YesNoModalPopupComponent driver={yesNoPopupDriver}></YesNoModalPopupComponent>

        </div>
    );
}