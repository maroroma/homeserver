import FileToMoveDescriptor from "../../../model/seedbox/FileToMoveDescriptor";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class TodoRenameFileAction implements HomeServerAction {

    constructor(private fileToRename: FileToMoveDescriptor, private newName: string) { }



    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        const newFilesToMove = previousState.seedboxTodoSubState.filesToMove.map(aFileToMove => {
            if (aFileToMove.id === this.fileToRename.id) {
                return {
                    ...aFileToMove,
                    newName: this.newName
                }
            } else {
                return aFileToMove
            }
        })

        return {
            ...previousState,
            seedboxTodoSubState: {
                ...previousState.seedboxTodoSubState,
                filesToMove: newFilesToMove,
                nextButtonDisabled: newFilesToMove.some(aFileToMove => aFileToMove.newName === "")
            }
        }
    }

}