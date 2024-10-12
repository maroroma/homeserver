import FileDescriptor from "./FileDescriptor";

export default class FileDirectoryDescriptor extends FileDescriptor {

    static ROOT_ID: string = "ROOT"


    static emptyFileDirectoryDescriptor(): FileDirectoryDescriptor {
        return new FileDirectoryDescriptor([], [], "", "", "", 0, false, false, false, false);
    }

    static root(rootDirectories: FileDirectoryDescriptor[]): FileDirectoryDescriptor {
        const empty = FileDirectoryDescriptor.emptyFileDirectoryDescriptor();

        empty.directories = rootDirectories.map(aDirectory => {
            aDirectory.isProtected = true;
            return aDirectory;
        });
        empty.id = FileDirectoryDescriptor.ROOT_ID;
        empty.name = FileDirectoryDescriptor.ROOT_ID;


        return empty;
    }

    static isRoot(directory: FileDescriptor): boolean {
        return directory.id === FileDirectoryDescriptor.ROOT_ID;
    }

    constructor(
        public files: FileDescriptor[],
        public directories: FileDescriptor[],
        id: string,
        name: string,
        fullName: string,
        size: number,
        isFile: boolean,
        isDirectory: boolean,
        isProtected: boolean,
        isHidden: boolean) {
        super(id, name, fullName, size, isFile, isDirectory, isProtected, isHidden);
    }
}

