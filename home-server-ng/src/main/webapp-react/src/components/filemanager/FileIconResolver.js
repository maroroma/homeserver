import {FileExtensions} from "../../tools/fileTypes";

export function fixedIconResolver(icon) {
    return () => icon;
}

export function defaultDirectoryIconResolver(file) {
    return fixedIconResolver("folder")();
}

export function fileIconResolver(file) {


    const allMapping = [
        {
            extensions: FileExtensions.IMAGES,
            icon: "image"
        }, {
            extensions: FileExtensions.VIDEOS,
            icon: "movie"
        }, {
            extensions: FileExtensions.MUSIC,
            icon: "music_note"
        }, {
            extensions: FileExtensions.TEXT,
            icon: "insert_drive_file"
        }
    ];

    const mapping = allMapping
        .find(oneMapping => oneMapping.extensions.match(file.name));

    return mapping ? mapping.icon : "insert_drive_file";

}