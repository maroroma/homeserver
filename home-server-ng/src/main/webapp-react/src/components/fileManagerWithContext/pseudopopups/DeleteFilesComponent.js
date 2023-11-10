import React, {useEffect} from 'react';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';

import keys from '../../../tools/keys';
import IconComponent from '../../commons/IconComponent';


export default function DeleteFilesComponent() {

    const { filesToDelete, computedOptions, dispatchCancelDeleting, dispatchConfirmDelete } = useFileBrowserContext();


    useEffect(() => {
        const keyHandler = (event) => {
            keys(event).onEscape(() => dispatchCancelDeleting());
        }

        keys().register(keyHandler);

        return () => {
            keys().unregister(keyHandler);
        }

    },[dispatchCancelDeleting]);


    return <div>
        <div>
            <h4  className="pseudo-popup-header">Êtes vous sur de vouloir supprimer les fichiers suivants ? </h4>
        </div>
        <div>

            <ul className="collection">
                {filesToDelete.map(file =>
                    <li className="collection-item">
                        <IconComponent
                            icon={file.directory ? computedOptions.directoryIconResolver(file) : computedOptions.fileIconResolver(file)}
                            classAddons="left">
                        </IconComponent>
                        <span class="title">{file.name}</span>
                    </li>
                )}

            </ul>
        </div>
        <div className="right-align pseudo-popup-action-buttons">
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons red"
                onClick={() => dispatchConfirmDelete()}>
                <i class="material-icons left">delete</i>
                Supprimer
            </a>
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons blue"
                onClick={() => dispatchCancelDeleting()}>
                <i class="material-icons left">cancel</i>
                Annuler
            </a>
        </div>
    </div>
}