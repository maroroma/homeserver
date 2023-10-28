import React, {useEffect, useState} from 'react';
import DirectoryRenderer from "./DirectoryRenderer"
import FileRenderer from "./FileRenderer"
import "./FileBrowserComponent.scss"
import "../commons/Common.scss"
import eventReactor from '../../eventReactor/EventReactor';
import {actionMenu, ActionMenuComponent} from '../commons/ActionMenuComponent';
import {
    FILE_BROWSER_CHANGE_CURRENT_DIRECTORY,
    FILE_BROWSER_CREATE_NEW_DIRECTORY,
    FILE_BROWSER_DELETE_FILES,
    FILE_BROWSER_RENAME_ONE_FILE,
    SELECT_ITEM
} from '../../eventReactor/EventIds';

import {when} from "../../tools/when";
import on from '../../tools/on';
import enhance from '../../tools/enhance';
import sort from '../../tools/sort';
import {DisplayList} from '../../tools/displayList';

import {ModalPopupComponent, usePopupDriver} from "../commons/ModalPopupComponent";
import YesNoModalPopupComponent from "../commons/YesNoModalPopupComponent";
import {defaultDirectoryIconResolver, fileIconResolver, fixedIconResolver} from './FileIconResolver';
import fileBrowserEventReactor from './fileBrowserEventReactor';
import {searchSubReactor} from '../mainmenu/SearchBarComponent';


export default function FileBrowserComponent({ startUpDirectory, options = {} }) {

    // répertoire à afficher
    const [currentDirectory, setCurrentDirectory] = useState({});
    // liste des répertoire dans la navbar
    const [directoryStack, setDirectoryStack] = useState([]);

    const computedOptions = {
        downloadBaseUrl: options.downloadBaseUrl ? options.downloadBaseUrl : "",
        displayActionMenu: options.displayActionMenu !== undefined ? options.displayActionMenu : true,
        disableCheckBoxSelection: options.disableCheckBoxSelection !== undefined ? options.disableCheckBoxSelection : false,
        directoryIconResolver: options.directoryIconResolver ? options.directoryIconResolver : defaultDirectoryIconResolver,
        fileIconResolver: options.fileIconResolver ? options.fileIconResolver : fileIconResolver
    }

    const updateAllFileSelection = (newStatus) => {
        setCurrentDirectory({
            ...currentDirectory,
            files: currentDirectory.files.updateAllSelectableItems(newStatus),
            directories: currentDirectory.directories.updateAllSelectableItems(newStatus),
            hasSelectedFiles: newStatus,
            nbSelectedFiles: currentDirectory.files.length + currentDirectory.directories.length
        });
    };

    const onDirectoryStackClickHandler = (directorySelected) => {
        eventReactor().emit(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, directorySelected);
    };

    const onDirectoryUpdateHandler = (directoryToRefresh) => fileBrowserEventReactor().requestDirectoryDetail(directoryToRefresh);

    /**
     * Gestion du chargement du nouveau répertoire sélectionné (sur event)
     */
    useEffect(() =>
        fileBrowserEventReactor()
            .onDirectoryDetailLoaded(
                newDirectory => {

                    searchSubReactor().clearSearchBar();

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
                        files: new DisplayList(newCurrentDirectory.files).updateItems(enhance().selectable()).updateSort(sort().fileName()),
                        directories: new DisplayList(newCurrentDirectory.directories).updateItems(enhance().selectable()).updateSort(sort().fileName()),
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
            files: new DisplayList(startUpDirectory.files).updateItems(enhance().selectable()).updateSort(sort().fileName()),
            directories: new DisplayList(startUpDirectory.directories).updateItems(enhance().selectable()).updateSort(sort().fileName()),
            hasSelectedFiles: false,
            nbSelectedFiles: 0
        });
        setDirectoryStack([startUpDirectory]);
    }, [startUpDirectory]);



    /**
     * Gestion des events selection et nouveau directory à afficher
     */
    useEffect(() => {
        const selectFileUnsubcribe = eventReactor().subscribe(SELECT_ITEM, data => {

            const updatedFiles = currentDirectory.files.updateSelectableItems(data.itemId, data.newStatus);
            const updatedDirectories = currentDirectory.directories.updateSelectableItems(data.itemId, data.newStatus);
            const nbSelectedFiles = updatedFiles.selectedItemsCount() + updatedDirectories.selectedItemsCount();

            setCurrentDirectory({
                ...currentDirectory,
                files: updatedFiles,
                directories: updatedDirectories,
                hasSelectedFiles: nbSelectedFiles > 0,
                nbSelectedFiles: nbSelectedFiles
            });

            if (nbSelectedFiles > 0) {
                actionMenu().open();
            } else {
                actionMenu().close()
            }
        });

        const changeCurrentDirectoryUnSubscribe = eventReactor().subscribe(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, data => {
            fileBrowserEventReactor().requestDirectoryDetail(data);
        });

        const searchStringUnSubscribe = searchSubReactor().onSearchEvent(searchString =>
            setCurrentDirectory({
                ...currentDirectory,
                files: currentDirectory.files.updateFilter(on().stringContains(searchString, oneFile => oneFile.name)),
                directories: currentDirectory.directories.updateFilter(on().stringContains(searchString, oneFile => oneFile.name)),
            })
        );

        return () => {
            selectFileUnsubcribe();
            changeCurrentDirectoryUnSubscribe();
            searchStringUnSubscribe();
        }

    }, [currentDirectory, directoryStack]);

    // gestion du retour arrière dans liste de fichiers
    const parentDirectory = directoryStack.length > 1 ? {
        ...directoryStack[directoryStack.length - 2],
        name: ".."
    } : undefined;

    // composant graphique correspondant
    const goToParentDirectoryComponent = parentDirectory ? <DirectoryRenderer directory={parentDirectory} disabled={true} iconResolver={fixedIconResolver("keyboard_return")}></DirectoryRenderer> : undefined;


    // fonction de filtrage pour l'activation des boutons du menu
    const noFileOrDirectorySelected = () => !currentDirectory?.hasSelectedFiles;
    const noFileSelected = () => currentDirectory?.files?.hasNoSelectedItems();
    // const noFileSelected = () => currentDirectory?.files?.displayList?.filter(on().selected()).length === 0;
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
        const selectedFileToRename = currentDirectory.files.rawList
            .filter(on().selected())
            .concat(currentDirectory.directories.rawList.filter(on().selected()))
            .filter(on().defined())
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
                const filesToDelete = currentDirectory.files.rawList.filter(on().selected())
                    .concat(currentDirectory.directories.rawList.filter(on().selected()));
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

    // le menu d'action n'est pas forcément affiché
    const actionMenuComponent = !computedOptions.displayActionMenu ? null :
        (<ActionMenuComponent>
            <li className={when(noFileOrDirectorySelected).or(currentIsRoot).thenHideElement()}><a href="#!" className="btn-floating btn-small red" onClick={openDeleteFilePopup}><i className="material-icons">delete</i></a></li>
            <li className={when(noFileOrDirectorySelected).or(currentIsRoot).or(moreThanOneFileSelected).thenHideElement()}><a href="#!" className="btn-floating btn-small green" onClick={openPopupRename}><i className="material-icons">edit</i></a></li>
            <li><a href="#!" className={when(currentIsRoot).thenDisableElement("btn-floating btn-small green")} onClick={() => openPopupForCreateFolder(currentDirectory)}><i className="material-icons">create_new_folder</i></a></li>
            <li><a href="#!" className={when(currentIsRoot).or(noFileSelected).thenDisableElement("btn-floating btn-small green")} onClick={() => openPopupDownload()}><i className="material-icons">file_download</i></a></li>
            <li><a href="#!" className={when(currentIsRoot).thenDisableElement("btn-floating btn-small green")} onClick={() => openPopupForCreateFolder(currentDirectory)}><i className="material-icons">file_upload</i></a></li>
            <li className={when(allFilesSelected).thenHideElement()}><a href="#!" className="btn-floating btn-small blue" onClick={() => updateAllFileSelection(true)}><i className="material-icons">check_box</i></a></li>
            <li><a href="#!" className="btn-floating btn-small blue" onClick={() => onDirectoryUpdateHandler(currentDirectory)}><i className="material-icons">sync</i></a></li>
        </ActionMenuComponent>);

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
                {currentDirectory?.directories?.displayList.map((oneDirectory, oneDirectoryIndex) => <DirectoryRenderer directory={oneDirectory} key={oneDirectoryIndex} disabled={computedOptions.disableCheckBoxSelection} iconResolver={computedOptions.directoryIconResolver}></DirectoryRenderer>)}
                {currentDirectory?.files?.displayList.map((oneFile, oneFileIndex) => <FileRenderer file={oneFile} key={oneFileIndex} disabled={computedOptions.disableCheckBoxSelection} iconResolver={computedOptions.fileIconResolver}>{oneFile.name}</FileRenderer>)}
            </ul>


            {actionMenuComponent}

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
                    {currentDirectory.files?.displayList?.filter(on().selected()).map((oneFile, index) => (
                        <li className="collection-item" key={index}><a download={oneFile.name} href={`${computedOptions.downloadBaseUrl}/${oneFile.id}`}>{oneFile.name}</a></li>
                    ))}
                </ul>
            </ModalPopupComponent>

            <YesNoModalPopupComponent driver={yesNoPopupDriver}></YesNoModalPopupComponent>

        </div>
    );
}