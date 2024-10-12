import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import SelectableItems from "../../../model/SelectableItems";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class SwitchAllDirectoryContentAction implements HomeServerAction {

    static selectAll(): SwitchAllDirectoryContentAction {
        return new SwitchAllDirectoryContentAction(true);
    }

    static unselectAll(): SwitchAllDirectoryContentAction {
        return new SwitchAllDirectoryContentAction(false);
    }

    constructor(private expectedStatus: boolean) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        let switcher: (selectableItems: SelectableItems<FileDescriptor>) => SelectableItems<FileDescriptor>;
        if (this.expectedStatus === true) {
            switcher = seletactableItems => seletactableItems.selectAll();
        } else {
            switcher = seletactableItems => seletactableItems.unselectAll();
        }

        return {
            ...previousState,
            fileManagerSubState: {
                ...previousState.fileManagerSubState,
                filesFromCurrentDirectory: switcher(previousState.fileManagerSubState.filesFromCurrentDirectory),
                directoriesFromCurrentDirectory: switcher(previousState.fileManagerSubState.directoriesFromCurrentDirectory)
            },
            searchString: ""
        }

    }

}