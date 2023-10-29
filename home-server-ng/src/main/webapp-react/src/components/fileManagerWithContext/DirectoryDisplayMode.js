import React from 'react';


import {useFileBrowserContext} from "./FileBrowserContextDefinition";

// détermine comment afficher le répertoire
const DirectoryDisplayMode = {
    BROWSING: "BROWSING",
    RENAMING: "RENAMING",
    DOWNLOADING: "DOWNLOADING",
    DELETING: "DELETING",
    CREATE_DIRECTORY: "CREATE_DIRECTORY",
    UPLOADING: "UPLOADING",
    CONFIGURING: "CONFIGURING"
}

function DisplayModeVisibleComponent({ children, forDisplayMode = [DirectoryDisplayMode.BROWSING, DirectoryDisplayMode.DOWNLOADING] }) {

    const { currentDirectory } = useFileBrowserContext();

    if (forDisplayMode.includes(currentDirectory.displayMode)) {
        return <>{children}</>
    } else {
        return <></>
    }



}

export { DirectoryDisplayMode, DisplayModeVisibleComponent };