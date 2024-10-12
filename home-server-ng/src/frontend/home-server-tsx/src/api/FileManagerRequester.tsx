import DirectoryCreationRequest from "../model/filemanager/DirectoryCreationRequest";
import FileDescriptor from "../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../model/filemanager/FileDirectoryDescriptor";
import RenameFileDescriptor from "../model/filemanager/RenameFileDescriptor";
import RootDirectoryConfiguration from "../model/filemanager/RootDirectoryConfiguration";
import {RootDirectoryConfigurationCreationRequest} from "../model/filemanager/RootDirectoryConfigurationCreationRequest";
import {RequesterUtils} from "./RequesterUtils";

export default class FileManagerRequester {
    static getRootDirectories(): Promise<FileDirectoryDescriptor[]> {
        return RequesterUtils.get("/api/filemanager/rootdirectories")
    }

    static getDirectoryDetails(directoryToLoad: FileDescriptor): Promise<FileDirectoryDescriptor> {
        return RequesterUtils.get(`/api/filemanager/directories/${directoryToLoad.id}`)
    }

    static renameFile(renameFile: RenameFileDescriptor): Promise<any> {
        return RequesterUtils.update("/api/filemanager/files", renameFile)
    }

    static deleteFiles(filesToDelete: FileDescriptor[]): Promise<any> {
        return RequesterUtils.post("/api/filemanager/files/batchdelete", filesToDelete.map(aFile => aFile.id));
    }

    static uploadFiles(currentDirectory: FileDescriptor, filesToUpload: File[]): Promise<any> {
        const request = new FormData();
        filesToUpload.forEach(oneFile => request.append("file", oneFile));
        console.log(request);
        return RequesterUtils.upload(`/api/filemanager/files/${currentDirectory.id}`, request);
    }

    static getRootDirectoriesConfiguration(): Promise<RootDirectoryConfiguration[]> {
        return RequesterUtils.get<any>("/api/filemanager/configuration/rootDirectories")
            .then(response => response.map((aRawRootDirectory: { id: string; protected: boolean; hidden: boolean; rawPath: string; }) => RootDirectoryConfiguration.mapFromResponse(aRawRootDirectory)))
    }

    static updateRootDirectoryConfiguration(rootDirectoryConfiguration: RootDirectoryConfiguration): Promise<RootDirectoryConfiguration[]> {
        return RequesterUtils.update<any>(`/api/filemanager/configuration/rootDirectories/${rootDirectoryConfiguration.id}`, {
            id: rootDirectoryConfiguration.id,
            protected: rootDirectoryConfiguration.isProtected,
            hidden: rootDirectoryConfiguration.hidden,
            rawPath: rootDirectoryConfiguration.rawPath
        })
            .then(response => response.map((aRawRootDirectory: { id: string; protected: boolean; hidden: boolean; rawPath: string; }) => RootDirectoryConfiguration.mapFromResponse(aRawRootDirectory)))

    }

    static addRootDirectory(rawPath: string): Promise<RootDirectoryConfiguration[]> {
        return RequesterUtils.post<any>("/api/filemanager/configuration/rootDirectories", new RootDirectoryConfigurationCreationRequest(rawPath))
            .then(response => response.map((aRawRootDirectory: { id: string; protected: boolean; hidden: boolean; rawPath: string; }) => RootDirectoryConfiguration.mapFromResponse(aRawRootDirectory)))
    }

    static deleteRootDirectory(rootDirectoryToDelete: RootDirectoryConfiguration): Promise<RootDirectoryConfiguration[]> {
        return RequesterUtils.delete<any>(`/api/filemanager/configuration/rootDirectories/${rootDirectoryToDelete.id}`)
            .then(response => response.map((aRawRootDirectory: { id: string; protected: boolean; hidden: boolean; rawPath: string; }) => RootDirectoryConfiguration.mapFromResponse(aRawRootDirectory)))
    }

    static addNewDirectory(request: DirectoryCreationRequest): Promise<any> {
        return RequesterUtils.post("/api/filemanager/directory", request);
    }
}