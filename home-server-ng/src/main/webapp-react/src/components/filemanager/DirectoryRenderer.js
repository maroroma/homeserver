import React from 'react';
import IconComponent from '../commons/IconComponent';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import eventReactor from '../../eventReactor/EventReactor';
import { FILE_BROWSER_CHANGE_CURRENT_DIRECTORY } from '../../eventReactor/EventIds';
import { when } from '../../tools/when';
import { defaultDirectoryIconResolver } from './FileIconResolver';

export default function DirectoryRenderer({ directory, disabled = false, iconResolver = defaultDirectoryIconResolver }) {

    const onChangeSelectionEventHander = (newStatus) => {
        eventReactor().shortcuts().selectItem(directory.id, newStatus);
    }

    const onDirectoryChangeEventHandler = () => {
        eventReactor().emit(FILE_BROWSER_CHANGE_CURRENT_DIRECTORY, directory);
    }

    return <li className="collection-item">
        <div>
            <CheckBoxComponent disabled={disabled} dataswitch={directory.selected} onChange={onChangeSelectionEventHander}></CheckBoxComponent>
            <a className="waves-effect waves-teal btn-flat file-item truncate" href="#!" onClick={onDirectoryChangeEventHandler}>
                <IconComponent icon={iconResolver(directory)} classAddons={when(directory.selected).thenPlopSelectedItem("left")}></IconComponent>
                {directory.name}</a>
        </div>
    </li>;

}