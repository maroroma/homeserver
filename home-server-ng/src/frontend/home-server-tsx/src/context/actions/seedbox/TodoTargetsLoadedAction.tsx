import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import TargetDirectory from "../../../model/seedbox/TargetDirectory";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class TodoTargetsLoadedAction implements HomeServerAction {

    constructor(private targets: TargetDirectory[]) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        const toasted = ToastAction.clear().applyToState(previousState);

        const root = FileDirectoryDescriptor.root(this.targets.sort(FileDescriptor.sorter()))

        return {
            ...toasted,
            seedboxTodoSubState: {
                ...toasted.seedboxTodoSubState,
                targetDirectories: this.targets.sort(FileDescriptor.sorter()),
                currentTargetDirectory: root,
                targetDirectoriesStack: [root],
                nextButtonDisabled: true
            }
        }
    }

}