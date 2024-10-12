import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import SelectableItems from "../../../model/SelectableItems";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class LoadedDirectoryAction implements HomeServerAction {

    constructor(private loadedDirectory: FileDirectoryDescriptor, private toastMessage:string = "") { }



    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        const toastedState = this.toastMessage === "" ? ToastAction.clear().applyToState(previousState) : ToastAction.info(this.toastMessage).applyToState(previousState);

        return {
            ...toastedState,
            fileManagerSubState: {
                ...toastedState.fileManagerSubState,
                currentDirectory: this.loadedDirectory,
                directoriesStack: this.rebuildStack(toastedState.fileManagerSubState.directoriesStack, this.loadedDirectory),
                directoriesFromCurrentDirectory: SelectableItems.of(this.loadedDirectory.directories.sort(FileDescriptor.sorter())),
                filesFromCurrentDirectory: SelectableItems.of(this.loadedDirectory.files.sort(FileDescriptor.sorter()))
            },
            workInProgress: false,
            searchString: ""
        }
    }

    private rebuildStack(previousStack: FileDescriptor[], directoryToAppend: FileDescriptor): FileDescriptor[] {
        // si le directory est déjà dans la stack on doit remonter la stack jusqu'à lui
        const indexForRemoval = previousStack.findIndex(oneDirectory => oneDirectory.id === directoryToAppend.id);

        return [...previousStack.filter((aDirectory, index) => indexForRemoval === -1 || index < indexForRemoval), directoryToAppend];
    }
}