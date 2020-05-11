import eventReactor from "../../../eventReactor/EventReactor"


export const FILE_SELECTED_CHANGE = 'FILE_SELECTED';
export const TARGET_SELECTION_CHANGE = 'TARGET_SELECTION_CHANGE';
export const FILE_RENAMING = 'FILE_RENAMING';
export const FILE_RENAMED = 'FILE_RENAMED';
export const MOVE_REQUEST_CHANGE = 'MOVE_REQUEST_CHANGE';
export const MOVE_REQUEST_SUCCESSFULL = 'MOVE_REQUEST_SUCCESSFULL';


export function todoSubEventReactor() {
    const fileSelectionChange = (selectedFiles) => eventReactor().emit(FILE_SELECTED_CHANGE, selectedFiles);
    const onFileSelectionChange = (eventHandler) => eventReactor().subscribe(FILE_SELECTED_CHANGE, eventHandler);
    const targetSelectionChange = (selectedDirectory) => eventReactor().emit(TARGET_SELECTION_CHANGE, selectedDirectory);
    const onTargetSelectionChange = (eventHandler) => eventReactor().subscribe(TARGET_SELECTION_CHANGE, eventHandler);

    const renamingFile = (renamingFile) => eventReactor().emit(FILE_RENAMING, renamingFile);
    const onFileRenaming = (eventHandler) => eventReactor().subscribe(FILE_RENAMING, eventHandler);

    const renamedFile = (renamedFile) => eventReactor().emit(FILE_RENAMED, renamedFile);
    const onFileRenamed = (eventHandler) => eventReactor().subscribe(FILE_RENAMED, eventHandler);

    const moveRequestChanged = (moveRequest) => eventReactor().emit(MOVE_REQUEST_CHANGE, moveRequest);
    const onMoveRequestChanged = (eventHandler) => eventReactor().subscribe(MOVE_REQUEST_CHANGE, eventHandler);


    const moveRequestSuccessFull = () => eventReactor().emit(MOVE_REQUEST_SUCCESSFULL);
    const onMoveRequestSuccessFull = (eventHandler) => eventReactor().subscribe(MOVE_REQUEST_SUCCESSFULL, eventHandler);


    return {
        fileSelectionChange: fileSelectionChange,
        onFileSelectionChange: onFileSelectionChange,
        targetSelectionChange: targetSelectionChange,
        onTargetSelectionChange: onTargetSelectionChange,
        renamingFile: renamingFile,
        onFileRenaming: onFileRenaming,
        renamedFile: renamedFile,
        onFileRenamed: onFileRenamed,
        moveRequestChanged: moveRequestChanged,
        onMoveRequestChanged: onMoveRequestChanged,
        moveRequestSuccessFull: moveRequestSuccessFull,
        onMoveRequestSuccessFull: onMoveRequestSuccessFull

    }
}