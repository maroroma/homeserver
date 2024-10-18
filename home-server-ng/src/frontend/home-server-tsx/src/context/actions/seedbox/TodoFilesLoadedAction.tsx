import TodoFile from "../../../components/seedbox/TodoFile";
import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import {TodoSteps} from "../../../model/seedbox/TodoSteps";
import SelectableItems from "../../../model/SelectableItems";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class TodoFilesLoadedAction implements HomeServerAction {

    constructor(private todoFiles: TodoFile[]) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        const toasted = ToastAction.clear().applyToState(previousState);
        return {
            ...toasted,
            seedboxTodoSubState: {
                ...toasted.seedboxTodoSubState,
                todoFiles: SelectableItems.of(this.todoFiles.sort(FileDescriptor.sorter())),


                targetDirectories: [],
                currentTargetDirectory: FileDirectoryDescriptor.emptyFileDirectoryDescriptor(),
                currentStep: TodoSteps.SELECT_TODO_FILES,
                backButtonDisabled: true,
                nextButtonDisabled: true,
                targetDirectoriesStack: [],
                filesToMove: []
            }
        }
    }

}