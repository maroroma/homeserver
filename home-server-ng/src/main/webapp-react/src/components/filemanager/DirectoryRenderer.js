import React from 'react';
import IconComponent from '../commons/IconComponent';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import eventReactor from '../../eventReactor/EventReactor';
import { FILE_BROWSER_SELECT_FILE, FILE_BROWSER_CHANGE_CURRENT_DIRECTORY } from '../../eventReactor/EventIds';

export default function DirectoryRenderer({ directory, disabled=false, icon="folder" }) {

    const onChangeSelectionEventHander = (newStatus) => {
        eventReactor().emit(FILE_BROWSER_SELECT_FILE, {
            fileId: directory.id,
            newStatus: newStatus
        });
    }

    const onDirectoryChangeEventHandler = () => {
        eventReactor().emit(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, directory);
    }

    return <li className="collection-item">
        <div>
            <CheckBoxComponent disabled={disabled} dataswitch={directory.selected} onChange={onChangeSelectionEventHander}></CheckBoxComponent>
            <a className="waves-effect waves-teal btn-flat file-item truncate" href="#!" onClick={onDirectoryChangeEventHandler}>
                <IconComponent icon={icon} classAddons="left"></IconComponent>
                {directory.name}</a>
        </div>
    </li>;

}