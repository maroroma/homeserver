import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import SelectableItem from "../../../model/SelectableItem";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class SwitchSelectDirectoryAction implements HomeServerAction {

    constructor(private directoryToSwitch: SelectableItem<FileDescriptor>) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            fileManagerSubState: {
                ...previousState.fileManagerSubState,
                directoriesFromCurrentDirectory: previousState.fileManagerSubState.directoriesFromCurrentDirectory.switch(this.directoryToSwitch, item => item.id)
            }
        }

    }

}