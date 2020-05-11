import React from 'react';
import { useEffect, useState } from 'react';
import FileBrowserComponent from '../../filemanager/FileBrowserComponent';
import seedboxApi from '../../../apiManagement/SeedboxApi';
import { defaultDirectoryIconResolver } from '../../filemanager/FileIconResolver';
import fileBrowserEventReactor from '../../filemanager/fileBrowserEventReactor';
import { todoSubEventReactor } from './TodoSubEventReactor';


export default function ChooseTargetDirectoryComponent() {


    const [startupDirectory, setStartupDirectory] = useState({});


    useEffect(() => {
        const loadRootDirectories = () => seedboxApi().getTargetDirectories().then(response => {
            setStartupDirectory({
                files: [],
                directories: response,
                name: "Cibles disponibles",
                id: "ROOT_DIRECTORY"
            });
        });

        loadRootDirectories();

        const unsubscriveOnCompleted = todoSubEventReactor().onMoveRequestSuccessFull(() => loadRootDirectories());

        const unsubscribeDirectoryDetail = fileBrowserEventReactor().onRequestDirectoryDetail(data => {
            if (data.requestedDirectory.id === "ROOT_DIRECTORY") {
                loadRootDirectories();
                todoSubEventReactor().targetSelectionChange(undefined);
            } else {
                seedboxApi()
                    .getSubFolderContent(data.requestedDirectory)
                    .then(response => {
                        todoSubEventReactor().targetSelectionChange(response);
                        if (response) {
                            fileBrowserEventReactor().directoryDetailLoaded(response);
                        }
                    });
            }
        });

        return () => {
            unsubscribeDirectoryDetail();
            unsubscriveOnCompleted();
        }
    }, []);

    const customDirectoryIconResolver = (directory) => {
        if (directory.type) {
            if (directory.type === "MOVIES") {
                return "local_movies";
            }
            if (directory.type === "TVSHOWS") {
                return "tv";
            }
        }
        return defaultDirectoryIconResolver(directory);
    }




    return <FileBrowserComponent startUpDirectory={startupDirectory} options={
        {
            displayActionMenu: false,
            disableCheckBoxSelection: true,
            directoryIconResolver: customDirectoryIconResolver
        }
    }></FileBrowserComponent>
}