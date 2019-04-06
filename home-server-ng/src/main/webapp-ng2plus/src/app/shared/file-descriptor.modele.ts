
import { FilterTools } from './filter-tools.service';
export class FileDescriptor {

    private static readonly MOVIE_EXTENSIONS = ['mp4', 'avi', 'mkv'];
    private static readonly FILE_EXTENSIONS = ['txt', 'nfo'];
    private static readonly SUBTITLE_EXTENSIONS = ['srt'];
    private static readonly MUSIC_EXTENSIONS = ['mp3', 'wav'];
    private static readonly IMAGE_EXTENSIONS = ['jpeg', 'png', 'jpg'];
    private static readonly ARCHIVE_EXTENSIONS = ['tar', 'zip', 'rar', '7z'];

    public name: string;
    public fullName: string;
    public extension: string;
    public id: string;
    public size: number;

    protected static genericFromRaw<T extends FileDescriptor>(rawFile: any, type: { new(): T; }): T {
        const returnValue = new type();
        returnValue.name = rawFile.name;
        returnValue.fullName = rawFile.fullName;
        returnValue.id = rawFile.base64FullName;
        returnValue.extension = FileDescriptor.getFileExtension(returnValue.name);
        returnValue.size = rawFile.size;

        return returnValue;
    }

    public static fromRaw(rawFile: any): FileDescriptor {
        return FileDescriptor.genericFromRaw(rawFile, FileDescriptor);
    }

    public static getFileExtension(fileName: string): string {
        const splitted = fileName.split('.');

        if (splitted.length > 0) {
            return FilterTools.last(splitted);
        } else {
            return '';
        }
    }

    private static isFileOfType(extensions: Array<string>, file: FileDescriptor | File) {
        let extension: string;
        if (file instanceof FileDescriptor) {
            extension = (file as FileDescriptor).extension;
        } else {
            extension = FileDescriptor.getFileExtension((file as File).name);
        }

        return FilterTools.contains(extensions, extension);
    }

    public static isVideoFile(file: FileDescriptor | File): boolean {
        return FileDescriptor.isFileOfType(FileDescriptor.MOVIE_EXTENSIONS, file);
    }

    public static isCommonFile(file: FileDescriptor | File): boolean {
        return FileDescriptor.isFileOfType(FileDescriptor.FILE_EXTENSIONS, file);
    }

    public static isMusicFile(file: FileDescriptor | File): boolean {
        return FileDescriptor.isFileOfType(FileDescriptor.MUSIC_EXTENSIONS, file);
    }
    public static isSubtitleFile(file: FileDescriptor | File): boolean {
        return FileDescriptor.isFileOfType(FileDescriptor.SUBTITLE_EXTENSIONS, file);
    }

    public static isImageFile(file: FileDescriptor | File): boolean {
        return FileDescriptor.isFileOfType(FileDescriptor.IMAGE_EXTENSIONS, file);
    }

    public static isArchiveFile(file: FileDescriptor | File): boolean {
        return FileDescriptor.isFileOfType(FileDescriptor.ARCHIVE_EXTENSIONS, file);
    }
}
