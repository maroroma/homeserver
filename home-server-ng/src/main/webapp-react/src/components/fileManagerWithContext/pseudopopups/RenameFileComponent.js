import React, {useEffect, useState} from 'react';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import {when} from '../../../tools/when';

import "./RenameFileComponent.scss";
import keys from '../../../tools/keys';


export default function RenameFileComponent() {

    const { fileToRename, currentDirectory, dispatchCancelRenaming, dispatchFinalizeRenaming } = useFileBrowserContext();

    const [newName, setNewName] = useState("");

    const [enableSave, setEnableSave] = useState(false);


    useEffect(() => {

        setNewName(fileToRename.name);
        setEnableSave(false);

    }, [fileToRename]);

    useEffect(() => {
        setEnableSave(newName !== "" && newName !== fileToRename.name);

        const keyHandler = (event) => {
            keys(event).onCtrlEnter(() => {
                dispatchFinalizeRenaming(newName);
            })

            keys(event).onEscape(() => {
                dispatchCancelRenaming();
            });
        };

        keys().register(keyHandler);

        return () => {
            keys().unregister(keyHandler);
        }

    }, [newName, fileToRename]);


    return <div>
        <div>
            <h4 className="pseudo-popup-header">Renommer <span className='strong'>{fileToRename.name}</span> </h4>
        </div>
        <div className="input-field rename-form">
            <i className="material-icons prefix">edit</i>
            <input id="icon_prefix" type="text" className="validate" value={newName} onChange={(event) => setNewName(event.target.value)}></input>
        </div>
        <div className="right-align">
            <a href="#!" className={when(!enableSave).thenDisableElement("waves-effect waves-light btn pseudo-popup-action-buttons blue")}
                onClick={() => dispatchFinalizeRenaming(newName)}>
                <i class="material-icons left">save</i>
                Renommer
            </a>
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons red"
                onClick={() => dispatchCancelRenaming()}>
                <i class="material-icons left">cancel</i>
                Annuler
            </a>
        </div>
    </div>
}