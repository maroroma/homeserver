import React from 'react';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import {when} from '../../tools/when';
import IconComponent from '../commons/IconComponent';

import "./FileBrowserCommon.scss";
import {useFileBrowserContext} from './FileBrowserContextDefinition';


export default function OneDirectoryRenderer({ directory, selectionDisabled = false }) {

    const { computedOptions, dispatchLoadDirectory, dispatchSelectFile } = useFileBrowserContext();

    const innerSelection = () => {
        if (selectionDisabled !== true) {
            dispatchSelectFile(directory);
        }
    }

    return <li className="collection-item">
        <div>
            <CheckBoxComponent disabled={selectionDisabled} dataswitch={directory.selected} onChange={innerSelection}></CheckBoxComponent>
            <a className="waves-effect waves-teal btn-flat file-item truncate" href="#!" onClick={() => dispatchLoadDirectory(directory)}>
                <IconComponent
                    icon={computedOptions.directoryIconResolver(directory)}
                    classAddons={when(directory.selected).thenPlopSelectedItem("left")}>
                </IconComponent>
                {directory.name}
            </a>
        </div>
    </li>;
}