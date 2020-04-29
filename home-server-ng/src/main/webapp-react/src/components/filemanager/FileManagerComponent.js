import React from 'react';

import { useEffect, useState } from 'react';

import eventReactor from '../../eventReactor/EventReactor';
import {
    FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, FILE_BROWSER_DIRECTORY_DETAIL_LOADED,
    FILE_BROWSER_CREATE_NEW_DIRECTORY, FILE_BROWSER_DELETE_FILES,
    FILE_BROWSER_RENAME_ONE_FILE
} from '../../eventReactor/EventIds';


import fileManagerApi from '../../apiManagement/FileManagerApi';
import FileBrowserComponent from './FileBrowserComponent';

export default function FileManagerComponent() {


    const [rootDirectory, setRootDirectory] = useState({});

    useEffect(() => {

        const loadRootDirectories = () =>

            fileManagerApi().getRootDirectories().then(response => {
                setRootDirectory({
                    files: [],
                    directories: response,
                    name: "ROOT",
                    id: "ROOT_DIRECTORY"
                });
            });

        loadRootDirectories();

        const unsubscribeDirectoryDetail = eventReactor().subscribe(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, data => {
            if (data.requestedDirectory.id === "ROOT_DIRECTORY") {
                loadRootDirectories();
            } else {
                fileManagerApi()
                    .getDirectoryDetails(data.requestedDirectory)
                    .then(response => {
                        if (response) {
                            eventReactor().emit(FILE_BROWSER_DIRECTORY_DETAIL_LOADED, {
                                directoryToDisplay: response
                            });
                        }
                    });
            }
        });

        const unsubscribeDirectoryCreation = eventReactor().subscribe(FILE_BROWSER_CREATE_NEW_DIRECTORY, data => {
            fileManagerApi()
                .createNewDirectory(data.currentDirectory, data.newDirectoryName)
                .then(reponse => eventReactor().emit(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, {
                    requestedDirectory: data.currentDirectory
                }));
        });

        const unsubscribeDeleteDirectory = eventReactor().subscribe(FILE_BROWSER_DELETE_FILES, data => {
            fileManagerApi()
                .deleteFiles(data.filesToDelete)
                .then(response => eventReactor().emit(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, {
                    requestedDirectory: data.currentDirectory
                }));
        });

        const unsubscribeRenameFile = eventReactor().subscribe(FILE_BROWSER_RENAME_ONE_FILE, data => {
            fileManagerApi()
                .renameFile(data.fileToRename, data.fileNewName)
                .then(response => eventReactor().emit(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, {
                    requestedDirectory: data.currentDirectory
                }));
        });

        return () => {
            unsubscribeDirectoryDetail();
            unsubscribeDirectoryCreation();
            unsubscribeDeleteDirectory();
            unsubscribeRenameFile();
        }




    }, []);


    return <FileBrowserComponent startUpDirectory={rootDirectory} downloadBaseUrl={fileManagerApi().downloadBaseUrl()}></FileBrowserComponent>;
}