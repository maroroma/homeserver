
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

    protected static genericFromRaw<T extends FileDescriptor>(rawFile: any, type: { new (): T; }): T {
        const returnValue = new type();
        returnValue.name = rawFile.name;
        returnValue.fullName = rawFile.fullName;
        returnValue.id = rawFile.base64FullName;
        returnValue.extension = FileDescriptor.getFileExtension(returnValue.name);

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

    public static isVideoFile(file: FileDescriptor): boolean {
        return FilterTools.contains(FileDescriptor.MOVIE_EXTENSIONS, file.extension);
    }

    public static isCommonFile(file: FileDescriptor): boolean {
        return FilterTools.contains(FileDescriptor.FILE_EXTENSIONS, file.extension);
    }

    public static isMusicFile(file: FileDescriptor): boolean {
        return FilterTools.contains(FileDescriptor.MUSIC_EXTENSIONS, file.extension);
    }
    public static isSubtitleFile(file: FileDescriptor): boolean {
        return FilterTools.contains(FileDescriptor.SUBTITLE_EXTENSIONS, file.extension);
    }

    public static isImageFile(file: FileDescriptor): boolean {
        return FilterTools.contains(FileDescriptor.IMAGE_EXTENSIONS, file.extension);
    }

    public static isArchiveFile(file: FileDescriptor): boolean {
        return FilterTools.contains(FileDescriptor.ARCHIVE_EXTENSIONS, file.extension);
    }
}
