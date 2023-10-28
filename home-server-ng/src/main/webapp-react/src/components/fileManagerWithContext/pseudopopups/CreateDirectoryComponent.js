import React, {useEffect, useState} from 'react';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import {when} from '../../../tools/when';

import "./RenameFileComponent.scss";
import keys from '../../../tools/keys';


export default function CreateDirectoryComponent() {

    const { currentDirectory, dispatchCancelCreateDirectory, dispatchFinalizeCreateDirectory } = useFileBrowserContext();

    const [newDirectoryName, setNewDirectoryName] = useState("");

    const [enableCreate, setEnableCreate] = useState(false);


    useEffect(() => {

        setNewDirectoryName("");
        setEnableCreate(false);

    }, [currentDirectory]);

    useEffect(() => {
        setEnableCreate(newDirectoryName !== "");

        const keyHandler = (event) => {
            keys(event).onCtrlEnter(() => {
                dispatchFinalizeCreateDirectory(newDirectoryName);
            })

            keys(event).onEscape(() => {
                dispatchCancelCreateDirectory();
            });
        };

        keys().register(keyHandler);

        return () => {
            keys().unregister(keyHandler);
        }

    }, [newDirectoryName]);


    return <div>
        <div>
            <h4 className="pseudo-popup-header">Créer un nouveau répertoire</h4>
        </div>
        <div className="input-field rename-form">
            <i className="material-icons prefix">edit</i>
            <input id="icon_prefix" type="text" className="validate" value={newDirectoryName} onChange={(event) => setNewDirectoryName(event.target.value)}></input>
        </div>
        <div className="right-align">
            <a href="#!" className={when(!enableCreate).thenDisableElement("waves-effect waves-light btn pseudo-popup-action-buttons blue")}
                onClick={() => dispatchFinalizeCreateDirectory(newDirectoryName)}>
                <i class="material-icons left">create_new_folder</i>
                Créer
            </a>
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons red"
                onClick={() => dispatchCancelCreateDirectory()}>
                <i class="material-icons left">cancel</i>
                Annuler
            </a>
        </div>
    </div>
}