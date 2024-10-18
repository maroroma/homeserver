import TodoFile from "../../components/seedbox/TodoFile"
import FileDescriptor from "../../model/filemanager/FileDescriptor"
import FileDirectoryDescriptor from "../../model/filemanager/FileDirectoryDescriptor"
import FileToMoveDescriptor from "../../model/seedbox/FileToMoveDescriptor"
import TargetDirectory from "../../model/seedbox/TargetDirectory"
import {TodoSteps} from "../../model/seedbox/TodoSteps"
import SelectableItems from "../../model/SelectableItems"

export type SeedboxTodoSubState = {
    todoFiles: SelectableItems<TodoFile>,
    targetDirectories: TargetDirectory[],
    currentTargetDirectory: FileDirectoryDescriptor,
    currentStep: TodoSteps,
    backButtonDisabled: boolean,
    nextButtonDisabled: boolean,
    targetDirectoriesStack: FileDescriptor[],
    filesToMove: FileToMoveDescriptor[]

}