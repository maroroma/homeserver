import FileDescriptor from "../../model/filemanager/FileDescriptor"
import FileDirectoryDescriptor from "../../model/filemanager/FileDirectoryDescriptor"
import SelectableItems from "../../model/SelectableItems"

export type FileManagerSubState = {
    rootDirectories: FileDirectoryDescriptor[],
    currentDirectory: FileDirectoryDescriptor,
    filesFromCurrentDirectory: SelectableItems<FileDescriptor>,
    directoriesFromCurrentDirectory: SelectableItems<FileDescriptor>
    directoriesStack: FileDescriptor[]
}