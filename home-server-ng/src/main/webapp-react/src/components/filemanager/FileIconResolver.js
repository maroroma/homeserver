export function fixedIconResolver(icon) {
    return () => icon;
}

export function defaultDirectoryIconResolver(file) {
    return fixedIconResolver("folder")();
}

export function fileIconResolver(file) {

    const allMapping = [
        {
            extensions: ["jpg", "jpeg", "png", "bmp"],
            icon: "image"
        }, {
            extensions: ["avi", "mpeg", "mkv", "mp4"],
            icon: "movie"
        }, {
            extensions: ["mp3", "ogg"],
            icon: "music_note"
        }, {
            extensions: ["txt", "log"],
            icon: "insert_drive_file"
        }
    ]

    const splitted = file.name.toLowerCase().split(".");
    const extension = splitted.length > 0 ? splitted[splitted.length - 1] : "";


    const mapping = allMapping
        .find(oneMapping => oneMapping.extensions.includes(extension));


    return mapping ? mapping.icon : "insert_drive_file";
}