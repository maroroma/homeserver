import eventReactor from "../../eventReactor/EventReactor";

const FILE_BROWSER_REQUEST_DIRECTORY_DETAIL = "FILE_BROWSER_REQUEST_DIRECTORY_DETAIL";
const FILE_BROWSER_DIRECTORY_DETAIL_LOADED = "FILE_BROWSER_DIRECTORY_DETAIL_LOADED";

export default function fileBrowserEventReactor() {

    const requestDirectoryDetail = (requestedDirectory) => eventReactor().emit(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, {
        requestedDirectory: requestedDirectory
    });

    const onRequestDirectoryDetail = (requestHandler) => eventReactor().subscribe(FILE_BROWSER_REQUEST_DIRECTORY_DETAIL, requestHandler);

    const directoryDetailLoaded = (directoryWithDetails) => eventReactor().emit(FILE_BROWSER_DIRECTORY_DETAIL_LOADED, {
        directoryToDisplay: directoryWithDetails
    });

    const onDirectoryDetailLoaded = (requestHandler) => eventReactor().subscribe(FILE_BROWSER_DIRECTORY_DETAIL_LOADED, requestHandler);

    return {
        requestDirectoryDetail: requestDirectoryDetail,
        onRequestDirectoryDetail: onRequestDirectoryDetail,
        directoryDetailLoaded: directoryDetailLoaded,
        onDirectoryDetailLoaded: onDirectoryDetailLoaded
    }


}