import React from 'react';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import {when} from '../../../tools/when';
import FloatingMenuComponent from '../../commons/FloatingMenu/FloatingMenuComponent';


export default function FileBrowserActionMenu() {

    const { currentDirectory,
        dispatchStartRenaming,
        dispatchStartDeleting,
        dispatchStartCreateDirectory,
        disptachEnableDownloading,
        dispatchStartUploadFiles
    } = useFileBrowserContext();



    return <FloatingMenuComponent isOpen={currentDirectory.hasSelectedFiles}>
        <li className={when(!currentDirectory.hasSelectedFiles).or(() => currentDirectory.isStartupDirectory).thenHideElement()}>
            <a href="#!" className="btn-floating btn-small red" onClick={() => { dispatchStartDeleting() }}>
                <i className="material-icons">delete</i>
            </a>
        </li>
        <li className={when(!currentDirectory.hasSelectedFiles).or(() => currentDirectory.isStartupDirectory).or(() => currentDirectory.nbSelectedFiles > 1).thenHideElement()}>
            <a href="#!" className="btn-floating btn-small green" onClick={() => { dispatchStartRenaming() }}>
                <i className="material-icons">edit</i>
            </a>
        </li>
        <li>
            <a href="#!" className={when(currentDirectory.isStartupDirectory).thenDisableElement("btn-floating btn-small green")} onClick={() => { dispatchStartCreateDirectory() }}>
                <i className="material-icons">create_new_folder</i>
            </a>
        </li>
        <li>
            <a href="#!" className={when(currentDirectory.isStartupDirectory).or(() => currentDirectory.files.isEmpty()).thenDisableElement("btn-floating btn-small blue lighten-2")} onClick={() => { disptachEnableDownloading() }}>
                <i className="material-icons">cloud_download</i>
            </a>
        </li>
        <li>
            <a href="#!" className={when(currentDirectory.isStartupDirectory).thenDisableElement("btn-floating btn-small green")} onClick={() => { dispatchStartUploadFiles() }}>
                <i className="material-icons">cloud_upload</i>
            </a>
        </li>
    </FloatingMenuComponent >
}