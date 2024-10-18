import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class TodoSubTargetsLoadedAction implements HomeServerAction {

    constructor(private newCurrentTarget: FileDirectoryDescriptor) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        const toasted = ToastAction.clear().applyToState(previousState);

        return {
            ...toasted,
            seedboxTodoSubState: {
                ...toasted.seedboxTodoSubState,
                currentTargetDirectory: this.newCurrentTarget,
                targetDirectoriesStack: this.rebuildStack(toasted.seedboxTodoSubState.targetDirectoriesStack, this.newCurrentTarget),
                nextButtonDisabled: FileDirectoryDescriptor.isRoot(this.newCurrentTarget)
            }
        }
    }

    private rebuildStack(previousStack: FileDescriptor[], directoryToAppend: FileDescriptor): FileDescriptor[] {
        // si le directory est déjà dans la stack on doit remonter la stack jusqu'à lui
        const indexForRemoval = previousStack.findIndex(oneDirectory => oneDirectory.id === directoryToAppend.id);

        return [...previousStack.filter((aDirectory, index) => indexForRemoval === -1 || index < indexForRemoval), directoryToAppend];
    }

}