import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import SelectableItem from "../../../model/SelectableItem";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class SwitchSelectFileAction implements HomeServerAction {

    constructor(private fileToSwitch: SelectableItem<FileDescriptor>) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            fileManagerSubState: {
                ...previousState.fileManagerSubState,
                filesFromCurrentDirectory: previousState.fileManagerSubState.filesFromCurrentDirectory.switch(this.fileToSwitch, item => item.id)
            }
        }

    }

}