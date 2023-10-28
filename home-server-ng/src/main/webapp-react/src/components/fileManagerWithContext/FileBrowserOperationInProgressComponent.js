import React from 'react';
import {useFileBrowserContext} from './FileBrowserContextDefinition';

export default function FileBrowserOperationInProgressComponent() {


    const {workInProgress} = useFileBrowserContext();

    return workInProgress ? 
    <div className="progress">
        <div className="indeterminate"></div>
    </div> : <></>



}