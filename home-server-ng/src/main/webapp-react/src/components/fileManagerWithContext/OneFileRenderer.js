import React from 'react';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import {when} from '../../tools/when';
import IconComponent from '../commons/IconComponent';

import "./FileBrowserCommon.scss";
import "./OneFileRenderer.scss";
import {useFileBrowserContext} from './FileBrowserContextDefinition';
import {DirectoryDisplayMode, DisplayModeVisibleComponent} from './DirectoryDisplayMode';


export default function OneFileRenderer({ file, selectionDisabled = false }) {

    const { computedOptions, dispatchSelectFile, dispatchClickOnFile, currentDirectory } = useFileBrowserContext();

    const innerSelection = () => {
        if (selectionDisabled !== true) {
            dispatchSelectFile(file);
        }
    }

    return <li className="collection-item">
        <div>
            <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.BROWSING}>
                <CheckBoxComponent disabled={selectionDisabled} dataswitch={file.selected} onChange={innerSelection}></CheckBoxComponent>
                <a className="waves-effect waves-teal btn-flat file-item truncate" href="#!" onClick={() => dispatchClickOnFile(file)}>
                    <IconComponent
                        icon={computedOptions.fileIconResolver(file)}
                        classAddons={when(file.selected).thenPlopSelectedItem("left")}>
                    </IconComponent>
                    {file.name}
                </a>
            </DisplayModeVisibleComponent>
            <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.DOWNLOADING}>
                <CheckBoxComponent disabled={true} dataswitch={false}></CheckBoxComponent>
                <a className="waves-effect waves-teal btn-flat file-item truncate downloadable blue lighten-4" download={file.name} href={`${computedOptions.downloadBaseUrl}/${file.id}`}>
                    <IconComponent
                        icon="file_download"
                        classAddons="left">
                    </IconComponent>
                    {file.name}

                </a>
            </DisplayModeVisibleComponent>
        </div>
    </li>;
}