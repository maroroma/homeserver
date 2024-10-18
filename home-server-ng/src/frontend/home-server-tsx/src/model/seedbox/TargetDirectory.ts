import FileDescriptor from "../filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../filemanager/FileDirectoryDescriptor";
import {TargetDirectoryType} from "./TargetDirectoryType";

export default class TargetDirectory extends FileDirectoryDescriptor {
    constructor(public type: TargetDirectoryType,
        public subDirectories: FileDescriptor[],
        id: string,
        name: string,
        fullName: string,
        size: number,
        isFile: boolean,
        isDirectory: boolean,
        isProtected: boolean,
        isHidden: boolean
    ) {
        super([], [], id, name, fullName, size, isFile, isDirectory, isProtected, isHidden);
    }
}