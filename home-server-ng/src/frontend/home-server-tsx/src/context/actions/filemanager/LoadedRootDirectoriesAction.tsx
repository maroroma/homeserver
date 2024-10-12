import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import SelectableItems from "../../../model/SelectableItems";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class LoadedRootDirectoriesAction implements HomeServerAction {


    constructor(private rootDirectories: FileDirectoryDescriptor[]) { }

    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toasted = ToastAction.clear().applyToState(previousState);


        var dummyDirectoryForRoot = FileDirectoryDescriptor.root(this.rootDirectories)


        return {
            ...toasted,
            fileManagerSubState: {
                ...toasted.fileManagerSubState,
                rootDirectories: this.rootDirectories,
                currentDirectory: dummyDirectoryForRoot,
                filesFromCurrentDirectory: SelectableItems.empty(),
                directoriesFromCurrentDirectory: SelectableItems.of(this.rootDirectories.sort(FileDescriptor.sorter())),
                directoriesStack: [dummyDirectoryForRoot]
            },
            workInProgress: false,
            searchString: ""
        }
    }

}