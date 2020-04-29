import React from 'react';
import IconComponent from '../commons/IconComponent';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import eventReactor from '../../eventReactor/EventReactor';
import { FILE_BROWSER_SELECT_FILE, FILE_BROWSER_CHANGE_CURRENT_DIRECTORY } from '../../eventReactor/EventIds';
import fileIconResolver from "./FileIconResolver"


export default function FileRenderer({ file }) {

    const onChangeSelectionEventHander = (newStatus) => {
        eventReactor().emit(FILE_BROWSER_SELECT_FILE, {
            fileId: file.id,
            newStatus: newStatus
        });
    }

    const onDirectoryChangeEventHandler = () => {
        // eventReactor().emit(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, file);
    }

    return <li className="collection-item">
        <div>
            <CheckBoxComponent dataswitch={file.selected} onChange={onChangeSelectionEventHander}></CheckBoxComponent>
            <a className="waves-effect waves-teal btn-flat file-item" href="#!" onClick={onDirectoryChangeEventHandler}>
                <IconComponent icon={fileIconResolver(file)} classAddons="left"></IconComponent>
                <span className="truncate">
                    {file.name}
                </span>
            </a>
        </div>
    </li>;

}