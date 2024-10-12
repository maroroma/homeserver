
export default class FileDescriptor {



    static filter(...searchString: string[]): (item: FileDescriptor) => boolean {
        return fileDescriptor => searchString.some(aFilter => fileDescriptor.name.toLocaleLowerCase().includes(aFilter.toLocaleLowerCase())) ;
    }


    static emptyFileDescriptor(): FileDescriptor {
        return new FileDescriptor("", "", "", 0, false, false, false, false);
    }

    static sorter(): (fd1: FileDescriptor, fd2: FileDescriptor) => number {
        return (fd1, fd2) => fd1.name.toLocaleLowerCase().localeCompare(fd2.name.toLocaleLowerCase())
    }

    static downloadUrl(fileDescriptor: FileDescriptor): string {
        if (fileDescriptor) {
            return `/api/filemanager/files/${fileDescriptor.id}`
        }
        return "";
    }

    static named(fileName: string): FileDescriptor {
        const emptyFile = FileDescriptor.emptyFileDescriptor();
        emptyFile.name = fileName;
        return emptyFile;
    }


    constructor(
        public id: string,
        public name: string,
        public fullName: string,
        public size: number,
        public isFile: boolean,
        public isDirectory: boolean,
        public isProtected: boolean,
        public isHidden: boolean) { }
}