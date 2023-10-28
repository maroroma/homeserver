class FileExtension {
    constructor(extensions) {
        this.extensions = extensions;
        this.match = this.match.bind(this);
    }

    match(fileName) {
        const splitted = fileName.toLowerCase().split(".");
        const fileExtension = splitted.length > 0 ? splitted[splitted.length - 1] : "";
        return this.extensions.map(anExtension => anExtension.toLowerCase()).includes(fileExtension);
    }
}



const FileExtensions = {
    IMAGES: new FileExtension(["jpg", "jpeg", "png", "bmp", "bookpicture", "brickpicture"]),
    VIDEOS: new FileExtension(["avi", "mpeg", "mkv", "mp4"]),
    MUSIC: new FileExtension(["mp3", "ogg"]),
    TEXT: new FileExtension(["txt", "log"])
}

export {
    FileExtensions
}







