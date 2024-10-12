import FileDirectoryDescriptor from "./FileDirectoryDescriptor";

export default class DirectoryCreationRequest {
    static empty(): DirectoryCreationRequest {
        return new DirectoryCreationRequest(FileDirectoryDescriptor.emptyFileDirectoryDescriptor(), "");
    }


    constructor(public parentDirectory: FileDirectoryDescriptor, public directoryName: string) { }
}