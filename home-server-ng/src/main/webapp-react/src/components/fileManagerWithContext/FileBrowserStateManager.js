import fileManagerApi from "../../apiManagement/FileManagerApi";
import {DisplayList, emptyDisplayList} from "../../tools/displayList";
import enhance from "../../tools/enhance";
import sort from "../../tools/sort";
import {isImageViewerCompatibleFile} from "../commons/ImageViewer/ImageViewerComponent";
import {defaultDirectoryIconResolver, fileIconResolver} from "../filemanager/FileIconResolver";
import {DirectoryDisplayMode} from "./DirectoryDisplayMode";
import {FileBrowserActions} from "./FileBrowserContextDefinition";


const mapDirectoryForDisplay = (directory) => {
    return {
        ...directory,
        files: new DisplayList(directory.files).updateItems(enhance().selectable()).updateSort(sort().fileName()),
        directories: new DisplayList(directory.directories).updateItems(enhance().selectable()).updateSort(sort().fileName()),
        hasSelectedFiles: false,
        nbSelectedFiles: 0,
        itemSelectionDisabled: false,
        isStartupDirectory: false,
        displayMode: DirectoryDisplayMode.BROWSING
    }
}

const mapStartupDirectoryForDisplay = (previousState, directory) => {

    const standardDisplayMapping = mapDirectoryForDisplay(previousState.computedOptions.startupDirectoryMapper(directory));

    return {
        ...standardDisplayMapping,
        itemSelectionDisabled: previousState.computedOptions.protectStartupDirectory,
        isStartupDirectory: true
    }
}

const emptyDirectory = () => {
    return {
        files: emptyDisplayList(),
        directories: emptyDisplayList(),
        hasSelectedFiles: false,
        nbSelectedFiles: 0,
        itemSelectionDisabled: false
    }
}

const updateDirectoryStack = (oldStack, newDirectory) => {

    // si le directory est déjà dans la stack on doit remonter la stack jusqu'à lui
    const indexForRemoval = oldStack.findIndex(oneDirectory => oneDirectory.id === newDirectory.id);

    return [...oldStack.filter((aDirectory, index) => indexForRemoval === -1 || index < indexForRemoval), newDirectory];
}

const fileManagerStateManager = {
    // init du state
    initializeState: (startupDirectoryPromise, options) => {
        return {
            startupDirectoryPromise: startupDirectoryPromise,
            startupDirectory: emptyDirectory(),
            currentDirectory: emptyDirectory(),
            fileToRename: {},
            filesToDelete: [],
            directoryStack: [],
            displayDirectoryStack: true,
            workInProgress: true,
            imageViewerState: {
                display: false,
                imageUrlList: [],
                imageBaseUrl: options.downloadBaseUrl,
                selectedIndex: 0
            },
            computedOptions: {
                downloadBaseUrl: options.downloadBaseUrl ? options.downloadBaseUrl : "",
                displayActionMenu: options.displayActionMenu !== undefined ? options.displayActionMenu : true,
                disableCheckBoxSelection: options.disableCheckBoxSelection !== undefined ? options.disableCheckBoxSelection : false,
                directoryIconResolver: options.directoryIconResolver ? options.directoryIconResolver : defaultDirectoryIconResolver,
                fileIconResolver: options.fileIconResolver ? options.fileIconResolver : fileIconResolver,
                startupDirectoryMapper: options.startupDirectoryMapper ? options.startupDirectoryMapper : (item) => item,
                protectStartupDirectory: options.protectStartupDirectory ? options.protectStartupDirectory : true
            }
        };
    },
    // chargement du répertoire initial
    loadStartupDirectory: (previousState, startupDirectory) => {
        const computedStartupDirectory = mapStartupDirectoryForDisplay(previousState, startupDirectory);

        return {
            ...previousState,
            startupDirectory: startupDirectory,
            currentDirectory: computedStartupDirectory,
            directoryStack: [computedStartupDirectory],
            workInProgress: false
        }

    },
    // lancement du chargement du répertoire
    runLoadDirectory: (previousState, directoryRequired, dispatch) => {

        // si le répertoire est le startup directory, on le reload
        if (directoryRequired.isStartupDirectory) {
            dispatch({ type: FileBrowserActions.STARTUP_DIRECTORY_LOADED, startupDirectory: previousState.startupDirectory })
        } else {
            // sinon on dl la liste des fichiers pour le répertoire
            fileManagerApi().getDirectoryDetails(directoryRequired)
                .then(result => dispatch({ type: FileBrowserActions.DIRECTORY_LOADED, directoryLoaded: result }))
                .catch(error => dispatch({ type: FileBrowserActions.CANCEL_WIP }))
        }

        return {
            ...previousState,
            workInProgress: true
        }
    },
    // le répertoir est chargé, on l'affiche
    directoryLoaded: (previousState, directoryLoaded, displayMapping = (item) => mapDirectoryForDisplay(item)) => {
        return {
            ...previousState,
            workInProgress: false,
            currentDirectory: displayMapping(directoryLoaded),
            directoryStack: updateDirectoryStack(previousState.directoryStack, displayMapping(directoryLoaded))
        }
    },
    // changement de sélection pour un fichier
    selectFile: (previousState, fileWithSelectStateToSwitch) => {
        const newFilesList = previousState.currentDirectory.files.updateSelectableItems(fileWithSelectStateToSwitch.id, !fileWithSelectStateToSwitch.selected);
        const newDirectoryList = previousState.currentDirectory.directories.updateSelectableItems(fileWithSelectStateToSwitch.id, !fileWithSelectStateToSwitch.selected);
        const nbSelectedItems = newFilesList.selectedItemsCount() + newDirectoryList.selectedItemsCount();
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                files: newFilesList,
                directories: newDirectoryList,
                nbSelectedFiles: nbSelectedItems,
                hasSelectedFiles: nbSelectedItems > 0
            }
        }
    },
    // gestion du click sur un fichier (affichage du content...)
    clickOnFile: (previousState, fileClicked) => {

        const shouldDisplayFile = isImageViewerCompatibleFile(fileClicked);

        if (shouldDisplayFile) {
            const filesToDisplay = previousState.currentDirectory.files.rawList
                .filter(isImageViewerCompatibleFile)
                .map(aFile => aFile.id);
            const fileIndexToDisplay = filesToDisplay.findIndex(aFileId => aFileId === fileClicked.id);


            return {
                ...previousState,
                imageViewerState: {
                    ...previousState.imageViewerState,
                    display: shouldDisplayFile,
                    imageUrlList: filesToDisplay,
                    selectedIndex: fileIndexToDisplay
                }
            }
        }

        return previousState;


    },
    closeViewer: (previousState) => {
        return {
            ...previousState,
            imageViewerState: {
                ...previousState.imageViewerState,
                display: false,
                imageUrlList: []
            }
        };
    },
    startRenameElement: (previousState) => {

        if (previousState.currentDirectory.hasSelectedFiles) {
            const fileToRename = previousState.currentDirectory.files.getFirstSelectedItem();
            const directoryToRename = previousState.currentDirectory.directories.getFirstSelectedItem();

            return {
                ...previousState,
                fileToRename: directoryToRename !== null ? directoryToRename : fileToRename,
                currentDirectory: {
                    ...previousState.currentDirectory,
                    displayMode: DirectoryDisplayMode.RENAMING
                }
            }
        } else {
            return previousState;
        }


    },
    cancelRenameElement: (previousState) => {
        return {
            ...previousState,
            fileToRename: {},
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.BROWSING
            }
        }
    },
    finalizeRenameElement: (previousState, newName, dispatch) => {

        fileManagerApi().renameFile(previousState.fileToRename, newName)
            .then(result => dispatch({ type: FileBrowserActions.LOAD_DIRECTORY, directoryRequired: previousState.currentDirectory, dispatch }))
            .catch(error => dispatch({ type: FileBrowserActions.CANCEL_WIP }));

        return {
            ...previousState,
            workInProgress: true,
            fileToRename: {},
        }
    },
    startDeletingElements: (previousState) => {
        if (previousState.currentDirectory.hasSelectedFiles) {
            const filesToDelete = previousState.currentDirectory.files.getSelectedItems();
            const directoriesToDelete = previousState.currentDirectory.directories.getSelectedItems();

            return {
                ...previousState,
                filesToDelete: filesToDelete.concat(directoriesToDelete),
                currentDirectory: {
                    ...previousState.currentDirectory,
                    displayMode: DirectoryDisplayMode.DELETING
                }
            }
        } else {
            return previousState;
        }
    },
    cancelDeletingElements: (previousState) => {
        return {
            ...previousState,
            filesToDelete: [],
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.BROWSING
            }
        }
    },
    confirmDeleteElements: (previousState, dispatch) => {

        fileManagerApi().deleteFiles(previousState.filesToDelete)
            .then(result => dispatch({ type: FileBrowserActions.LOAD_DIRECTORY, directoryRequired: previousState.currentDirectory, dispatch }))
            .catch(error => {
                dispatch({ type: FileBrowserActions.CANCEL_WIP })
            });

        return {
            ...previousState,
            workInProgress: true,
            filesToDelete: []
        }
    },
    startCreateDirectory: (previousState) => {
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.CREATE_DIRECTORY
            }
        }
    },
    cancelCreateDirectory: (previousState) => {
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.BROWSING
            }
        }
    },
    confirmCreateDirectory: (previousState, newdirectoryName, dispatch) => {

        fileManagerApi().createNewDirectory(previousState.currentDirectory, newdirectoryName)
            .then(result => dispatch({ type: FileBrowserActions.LOAD_DIRECTORY, directoryRequired: previousState.currentDirectory, dispatch }))
            .catch(error => dispatch({ type: FileBrowserActions.CANCEL_WIP }));


        return {
            ...previousState,
            workInProgress: true,
        }
    },
    enableDownloadindMode: (previousState) => {
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.DOWNLOADING
            }
        }
    },
    disableDownloadingMode: (previousState) => {
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.BROWSING
            }
        }
    },
    cancelWip: (previousState) => {
        return {
            ...previousState,
            workInProgress: false
        }
    },
    startUploadFiles: (previousState) => {
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.UPLOADING
            }
        }
    },
    cancelUploadFiles: (previousState) => {
        return {
            ...previousState,
            currentDirectory: {
                ...previousState.currentDirectory,
                displayMode: DirectoryDisplayMode.BROWSING
            }
        }
    },
    executeUploadFiles: (previousState, filesToUpload, dispatch) => {

        fileManagerApi().uploadFiles(previousState.currentDirectory, filesToUpload)
            .then(result => dispatch({ type: FileBrowserActions.LOAD_DIRECTORY, directoryRequired: previousState.currentDirectory, dispatch }))
            .catch(error => dispatch({ type: FileBrowserActions.CANCEL_WIP }));

        return {
            ...previousState,
            workInProgress:true
        }

    }

}


export default fileManagerStateManager;


