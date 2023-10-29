import React, {createContext, useContext, useReducer} from "react";
import fileManagerStateManager from "./FileBrowserStateManager";


const FileBrowserActions = {
    HELLO_WORLD: "HELLO_WORLD",
    STARTUP_DIRECTORY_LOADED: "STARTUP_DIRECTORY_LOADED",
    LOAD_DIRECTORY: "LOAD_DIRECTORY",
    DIRECTORY_LOADED: "DIRECTORY_LOADED",
    SELECT_FILE: "SELECT_FILE",
    CLICK_ON_FILE: "CLICK_ON_FILE",
    CLOSE_VIEWER: "CLOSE_VIEWER",
    START_RENAME_ELEMENT: "START_RENAME_ELEMENT",
    CANCEL_RENAME_ELEMENT: "CANCEL_RENAME_ELEMENT",
    FINALIZE_RENAME_ELEMENT: "FINALIZE_RENAME_ELEMENT",
    START_DELETING_ELEMENTS: "START_DELETING_ELEMENTS",
    CANCEL_DELETING_ELEMENTS: "CANCEL_DELETING_ELEMENTS",
    CONFIRM_DELETING_ELEMENTS: "CONFIRM_DELETING_ELEMENTS",
    START_CREATE_DIRECTORY: "START_CREATE_DIRECTORY",
    CANCEL_CREATE_DIRECTORY: "CANCEL_CREATE_DIRECTORY",
    CONFIRM_CREATE_DIRECTORY: "CONFIRM_CREATE_DIRECTORY",
    ENABLE_DOWNLOAD_MODE: "ENABLE_DOWNLOAD_MODE",
    DISABLE_DOWNLOAD_MODE: "DISABLE_DOWNLOAD_MODE",
    CANCEL_WIP: "CANCEL_WIP",
    START_UPLOAD_FILES: "START_UPLOAD_FILES",
    EXECUTE_UPLOAD_FILES: "EXECUTE_UPLOAD_FILES",
    CANCEL_UPLOAD_FILES: "CANCEL_UPLOAD_FILES",
    START_CONFIGURING: "START_CONFIGURING",
    END_CONFIGURING: "END_CONFIGURING",

};

const reducer = (previousState, action) => {
    // console.log("FileManagerWithContext Action", action, previousState);
    switch (action.type) {
        case FileBrowserActions.HELLO_WORLD:
            // return updateSelectedSerie(previousState, action);
            return previousState;
        case FileBrowserActions.STARTUP_DIRECTORY_LOADED:
            return fileManagerStateManager.loadStartupDirectory(previousState, action.startupDirectory);
        case FileBrowserActions.LOAD_DIRECTORY:
            return fileManagerStateManager.runLoadDirectory(previousState, action.directoryRequired, action.dispatch);
        case FileBrowserActions.DIRECTORY_LOADED:
            return fileManagerStateManager.directoryLoaded(previousState, action.directoryLoaded);
        case FileBrowserActions.SELECT_FILE:
            return fileManagerStateManager.selectFile(previousState, action.fileWithSelectStateToSwitch);
        case FileBrowserActions.CLICK_ON_FILE:
            return fileManagerStateManager.clickOnFile(previousState, action.fileClicked);
        case FileBrowserActions.CLOSE_VIEWER:
            return fileManagerStateManager.closeViewer(previousState);
        case FileBrowserActions.START_RENAME_ELEMENT:
            return fileManagerStateManager.startRenameElement(previousState);
        case FileBrowserActions.CANCEL_RENAME_ELEMENT:
            return fileManagerStateManager.cancelRenameElement(previousState);
        case FileBrowserActions.FINALIZE_RENAME_ELEMENT:
            return fileManagerStateManager.finalizeRenameElement(previousState, action.newName, action.dispatch);
        case FileBrowserActions.START_DELETING_ELEMENTS:
            return fileManagerStateManager.startDeletingElements(previousState);
        case FileBrowserActions.CANCEL_DELETING_ELEMENTS:
            return fileManagerStateManager.cancelDeletingElements(previousState);
        case FileBrowserActions.CONFIRM_DELETING_ELEMENTS:
            return fileManagerStateManager.confirmDeleteElements(previousState, action.dispatch);
        case FileBrowserActions.START_CREATE_DIRECTORY:
            return fileManagerStateManager.startCreateDirectory(previousState);
        case FileBrowserActions.CANCEL_CREATE_DIRECTORY:
            return fileManagerStateManager.cancelCreateDirectory(previousState);
        case FileBrowserActions.CONFIRM_CREATE_DIRECTORY:
            return fileManagerStateManager.confirmCreateDirectory(previousState, action.newDirectoryName, action.dispatch);
        case FileBrowserActions.ENABLE_DOWNLOAD_MODE:
            return fileManagerStateManager.enableDownloadindMode(previousState);
        case FileBrowserActions.DISABLE_DOWNLOAD_MODE:
            return fileManagerStateManager.disableDownloadingMode(previousState);
        case FileBrowserActions.CANCEL_WIP:
            return fileManagerStateManager.cancelWip(previousState);
        case FileBrowserActions.START_UPLOAD_FILES:
            return fileManagerStateManager.startUploadFiles(previousState);
        case FileBrowserActions.CANCEL_UPLOAD_FILES:
            return fileManagerStateManager.cancelUploadFiles(previousState);
        case FileBrowserActions.EXECUTE_UPLOAD_FILES:
            return fileManagerStateManager.executeUploadFiles(previousState, action.filesToUpload, action.dispatch);
        case FileBrowserActions.START_CONFIGURING:
            return fileManagerStateManager.startConfiguring(previousState);
        case FileBrowserActions.END_CONFIGURING:
            return fileManagerStateManager.endConfiguring(previousState, action.dispatch);

        default:
            return previousState;
    }
};



const FileBrowserContext = createContext();

const FileBrowserProvider = ({ children, options, startupDirectoryPromise }) => {

    const [state, dispatch] = useReducer(reducer, fileManagerStateManager.initializeState(startupDirectoryPromise, options));

    const value = {
        startupDirectoryPromise: state.startupDirectoryPromise,
        startupDirectory: state.startupDirectory,
        currentDirectory: state.currentDirectory,
        directoryStack: state.directoryStack,
        displayDirectoryStack: state.displayDirectoryStack,
        computedOptions: state.computedOptions,
        workInProgress: state.workInProgress,
        imageViewerState: state.imageViewerState,
        fileToRename: state.fileToRename,
        filesToDelete: state.filesToDelete,

        dispatchStartupDirectoryLoaded: (loadedStartupDirectory) => {
            dispatch({ type: FileBrowserActions.STARTUP_DIRECTORY_LOADED, startupDirectory: loadedStartupDirectory })
        },
        dispatchLoadDirectory: (directoryRequired) => {
            dispatch({ type: FileBrowserActions.LOAD_DIRECTORY, directoryRequired: directoryRequired, dispatch: dispatch })
        },
        dispatchSelectFile: (fileWithSelectStateToSwitch) => {
            dispatch({ type: FileBrowserActions.SELECT_FILE, fileWithSelectStateToSwitch: fileWithSelectStateToSwitch })
        },
        dispatchClickOnFile: (fileClicked) => {
            dispatch({ type: FileBrowserActions.CLICK_ON_FILE, fileClicked: fileClicked })
        },
        dispatchCloseViewer: () => {
            dispatch({ type: FileBrowserActions.CLOSE_VIEWER })
        },
        dispatchStartRenaming: () => {
            dispatch({ type: FileBrowserActions.START_RENAME_ELEMENT })
        },
        dispatchCancelRenaming: () => {
            dispatch({ type: FileBrowserActions.CANCEL_RENAME_ELEMENT })
        },
        dispatchFinalizeRenaming: (newName) => {
            dispatch({ type: FileBrowserActions.FINALIZE_RENAME_ELEMENT, newName: newName, dispatch: dispatch })
        },
        dispatchStartDeleting: () => {
            dispatch({ type: FileBrowserActions.START_DELETING_ELEMENTS });
        },
        dispatchCancelDeleting: () => {
            dispatch({ type: FileBrowserActions.CANCEL_DELETING_ELEMENTS });
        },
        dispatchConfirmDelete: () => {
            dispatch({ type: FileBrowserActions.CONFIRM_DELETING_ELEMENTS, dispatch: dispatch })
        },
        dispatchStartCreateDirectory: () => {
            dispatch({ type: FileBrowserActions.START_CREATE_DIRECTORY });
        },
        dispatchCancelCreateDirectory: () => {
            dispatch({ type: FileBrowserActions.CANCEL_CREATE_DIRECTORY });
        },
        dispatchFinalizeCreateDirectory: (newDirectoryName) => {
            dispatch({ type: FileBrowserActions.CONFIRM_CREATE_DIRECTORY, dispatch: dispatch, newDirectoryName: newDirectoryName })
        },
        disptachEnableDownloading: () => {
            dispatch({ type: FileBrowserActions.ENABLE_DOWNLOAD_MODE });
        },
        disptachDisableDownloading: () => {
            dispatch({ type: FileBrowserActions.DISABLE_DOWNLOAD_MODE });
        },
        dispatchCancelWip: () => {
            dispatch({ type: FileBrowserActions.CANCEL_WIP });
        },
        dispatchStartUploadFiles: () => {
            dispatch({ type: FileBrowserActions.START_UPLOAD_FILES });
        },
        dispatchExecuteUploadFiles: (filesToUpload) => {
            dispatch({ type: FileBrowserActions.EXECUTE_UPLOAD_FILES, filesToUpload: filesToUpload, dispatch: dispatch });
        },
        dispatchCancelUploadFiles: () => {
            dispatch({ type: FileBrowserActions.CANCEL_UPLOAD_FILES });
        },
        dispatchStartConfiguring: () => {
            dispatch({ type: FileBrowserActions.START_CONFIGURING });
        },
        dispatchEndConfiguring: () => {
            dispatch({ type: FileBrowserActions.END_CONFIGURING, dispatch: dispatch });
        }
    };
    return <FileBrowserContext.Provider value={value}>
        {children}
    </FileBrowserContext.Provider>
}

const useFileBrowserContext = () => useContext(FileBrowserContext);

export {
    FileBrowserContext,
    FileBrowserProvider,
    FileBrowserActions,
    useFileBrowserContext
}