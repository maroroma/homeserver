import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import FileToMoveDescriptor from "../../../model/seedbox/FileToMoveDescriptor";
import SelectableItem from "../../../model/SelectableItem";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class SwitchSelectTodoFileAction implements HomeServerAction {

    constructor(private todoFileToSwitch: SelectableItem<FileDescriptor>) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        const newSelectedTodoFiles = previousState.seedboxTodoSubState.todoFiles.switch(this.todoFileToSwitch, item => item.id)
        const filesToMove = newSelectedTodoFiles.selectedItems().map(aSelectedFile => new FileToMoveDescriptor(aSelectedFile, aSelectedFile.name))
        return {
            ...previousState,
            seedboxTodoSubState: {
                ...previousState.seedboxTodoSubState,
                todoFiles: newSelectedTodoFiles,
                nextButtonDisabled: newSelectedTodoFiles.noneSelected(),
                filesToMove: filesToMove
            }
        }

    }

}