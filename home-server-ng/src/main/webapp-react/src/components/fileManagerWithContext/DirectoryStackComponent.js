import React from 'react';
import {useFileBrowserContext} from './FileBrowserContextDefinition';

import "../commons/Common.scss";
import "./DirectoryStackComponent.scss";


export default function DirectoryStackComponent() {


    const { directoryStack, dispatchLoadDirectory } = useFileBrowserContext();
    return <nav className="directory-stack pink accent-3">
        <div className="nav-wrapper">
            <div className="col s12">
                {directoryStack.map((oneDirectory, stackIndex) => (
                    <a key={stackIndex} href="#!" className="breadcrumb" onClick={() => dispatchLoadDirectory(oneDirectory)}>
                        {oneDirectory.name}
                    </a>))}
            </div>
        </div>
    </nav>
}