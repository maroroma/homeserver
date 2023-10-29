import React from "react"
import FileBrowserWithContextComponent from "./FileBrowserWithContextComponent"
import fileManagerApi from "../../apiManagement/FileManagerApi";


export default function FileManagerWithProviderComponent() {

    return <FileBrowserWithContextComponent startupDirectoryPromise={() => fileManagerApi().getRootDirectories()} options={
        {
            downloadBaseUrl: fileManagerApi().downloadBaseUrl(),
            startupDirectoryMapper: (rootDirectories) => {
                return {
                    files: [],
                    directories: rootDirectories,
                    name: "ROOT",
                    id: "ROOT_DIRECTORY"
                }
            }
        }}></FileBrowserWithContextComponent>
}