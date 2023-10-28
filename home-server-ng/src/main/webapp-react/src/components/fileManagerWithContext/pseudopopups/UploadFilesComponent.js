import React, {useEffect, useState} from 'react';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import {when} from '../../../tools/when';


export default function UploadFilesComponent() {

    const { dispatchCancelUploadFiles, currentDirectory, dispatchExecuteUploadFiles } = useFileBrowserContext();

    const [filesToUpload, setFilesToUpload] = useState([]);

    const [enableUpload, setEnableUpload] = useState(false);

    useEffect(() => {
        setEnableUpload(filesToUpload.length > 0);
    }, [filesToUpload])

    return <div>
        <div>
            <h4 className="pseudo-popup-header">Ajout des fichier à {currentDirectory.name}</h4>
        </div>

        <div>
            <div className="file-field input-field">
                <div className="btn">
                    <span>FILES...</span>
                    <input type="file" multiple onChange={(event) => {
                        setFilesToUpload(Array.from(event.target.files))
                    }}></input>
                </div>
                <div className="file-path-wrapper">
                    <input className="file-path validate" type="text" placeholder="Choisir les fichiers"></input>
                </div>
            </div>
            <div>
                <div className="collection">
                    {filesToUpload.map(aFile => <li className="collection-item">{aFile.name}</li>)}
                </div>
            </div>
        </div>

        <div className="right-align">
            <a href="#!" className={when(!enableUpload).thenDisableElement("waves-effect waves-light btn pseudo-popup-action-buttons blue")}
                onClick={() => { dispatchExecuteUploadFiles(filesToUpload) }}>
                <i class="material-icons left">cloud_upload</i>
                Uploader
            </a>
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons red"
                onClick={() => dispatchCancelUploadFiles()}>
                <i class="material-icons left">cancel</i>
                Annuler
            </a>
        </div>
    </div>
}