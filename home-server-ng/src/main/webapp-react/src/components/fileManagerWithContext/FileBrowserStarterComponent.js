import React, {useEffect} from 'react';
import {useFileBrowserContext} from './FileBrowserContextDefinition';

export default function FileBrowserStarterComponent() {
    const {startupDirectoryPromise, dispatchStartupDirectoryLoaded} = useFileBrowserContext();

    useEffect(() => {
        startupDirectoryPromise.then(loadedStartupDirectory => dispatchStartupDirectoryLoaded(loadedStartupDirectory));
    }, [startupDirectoryPromise]);

    return <></>
    
}