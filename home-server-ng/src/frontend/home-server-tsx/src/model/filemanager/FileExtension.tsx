import {ReactElement} from "react";
import {FileEarmark, FileEarmarkImage, FileEarmarkMusic, FileEarmarkPlay, FileEarmarkText} from "react-bootstrap-icons";
import FileDescriptor from "./FileDescriptor";
import {FileViewers} from "./FileViewers";

export default class FileExtension {

    static readonly IMAGES = new FileExtension(["jpg", "jpeg", "png", "bmp", "bookpicture", "brickpicture"], <FileEarmarkImage />, FileViewers.IMAGE);
    static readonly VIDEOS = new FileExtension(["avi", "mpeg", "mkv", "mp4"], <FileEarmarkPlay />);
    static readonly MUSIC = new FileExtension(["mp3", "ogg", "wav", "flac"], <FileEarmarkMusic />, FileViewers.MUSIC);
    static readonly TEXT = new FileExtension(["txt", "log"], <FileEarmarkText />);
    static readonly UNKNOWN = new FileExtension([], <FileEarmark />);

    static readonly KNOWN_FILE_EXTENSIONS: FileExtension[] = [
        FileExtension.IMAGES,
        FileExtension.VIDEOS,
        FileExtension.MUSIC,
        FileExtension.TEXT
    ];

    static resolve(fileDescriptor: FileDescriptor): FileExtension {
        const fileEXtension = FileExtension.KNOWN_FILE_EXTENSIONS.find(anExtension => anExtension.match(fileDescriptor.name));
        if (fileEXtension !== undefined) {
            return fileEXtension;
        }

        return FileExtension.UNKNOWN;
    }


    constructor(private extensions: string[], public icon: ReactElement, public viewer: FileViewers = FileViewers.NONE) {
        this.match = this.match.bind(this);
    }

    match(fileName: string): boolean {
        const splitted = fileName.toLowerCase().split(".");
        const fileExtension = splitted.length > 0 ? splitted[splitted.length - 1] : "";
        return this.extensions.map(anExtension => anExtension.toLowerCase()).includes(fileExtension);
    }

    fileDescriptorFilter() : (fd:FileDescriptor) => boolean  {
        return fileDescriptor =>  this.match(fileDescriptor.name);
    }
}