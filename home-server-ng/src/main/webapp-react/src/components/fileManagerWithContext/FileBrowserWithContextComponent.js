import DirectoryComponent from "./DirectoryComponent";
import DirectoryStackComponent from "./DirectoryStackComponent";
import {FileBrowserProvider} from "./FileBrowserContextDefinition";
import React from 'react';
import FileBrowserStarterComponent from "./FileBrowserStarterComponent";
import FileBrowserOperationInProgressComponent from "./FileBrowserOperationInProgressComponent";
import FileBrowserActionMenu from "./actionmenus/FileBrowserActionMenu";
import RenameFileComponent from "./pseudopopups/RenameFileComponent";
import {DirectoryDisplayMode, DisplayModeVisibleComponent} from "./DirectoryDisplayMode";
import DeleteFilesComponent from "./pseudopopups/DeleteFilesComponent";
import CreateDirectoryComponent from "./pseudopopups/CreateDirectoryComponent";
import FileDownloadActionMenu from "./actionmenus/FileDownloadActionMenu";
import UploadFilesComponent from "./pseudopopups/UploadFilesComponent";


export default function FileBrowserWithContextComponent({ options, startupDirectoryPromise }) {


    return <FileBrowserProvider options={options} startupDirectoryPromise={startupDirectoryPromise}>
        <FileBrowserStarterComponent></FileBrowserStarterComponent>

        <DisplayModeVisibleComponent>
            <DirectoryStackComponent></DirectoryStackComponent>
            <DirectoryComponent></DirectoryComponent>
        </DisplayModeVisibleComponent>
        <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.BROWSING}>
            <FileBrowserActionMenu></FileBrowserActionMenu>
        </DisplayModeVisibleComponent>
        <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.DOWNLOADING}>
            <FileDownloadActionMenu></FileDownloadActionMenu>
        </DisplayModeVisibleComponent>

        <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.RENAMING}>
            <RenameFileComponent></RenameFileComponent>
        </DisplayModeVisibleComponent>
        <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.DELETING}>
            <DeleteFilesComponent></DeleteFilesComponent>
        </DisplayModeVisibleComponent>
        <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.CREATE_DIRECTORY}>
            <CreateDirectoryComponent></CreateDirectoryComponent>
        </DisplayModeVisibleComponent>
        <DisplayModeVisibleComponent forDisplayMode={DirectoryDisplayMode.UPLOADING}>
            <UploadFilesComponent></UploadFilesComponent>
        </DisplayModeVisibleComponent>

        <FileBrowserOperationInProgressComponent></FileBrowserOperationInProgressComponent>
    </FileBrowserProvider>

}