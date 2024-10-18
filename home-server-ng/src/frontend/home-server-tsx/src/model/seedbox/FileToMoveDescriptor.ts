import FileDescriptor from "../filemanager/FileDescriptor";

export default class FileToMoveDescriptor extends FileDescriptor {

    static isRenamed(fileToMove: FileToMoveDescriptor): boolean {
        return fileToMove.name !== fileToMove.newName
    }

    constructor(initialFile: FileDescriptor, public newName: string = "") {
        super(initialFile.id,
            initialFile.name,
            initialFile.fullName,
            initialFile.size,
            initialFile.isFile,
            initialFile.isDirectory,
            initialFile.isProtected,
            initialFile.isHidden);
    }
}