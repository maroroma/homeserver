import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import OrderedTodoSteps from "../../../model/seedbox/OrderedTodoSteps";
import {TodoSteps} from "../../../model/seedbox/TodoSteps";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class TodoNextStepAction implements HomeServerAction {

    constructor(private nextStepSupplier: (currentStep: TodoSteps) => TodoSteps) {

    }

    static next(): TodoNextStepAction {
        return new TodoNextStepAction(currentStep => OrderedTodoSteps.next(currentStep));
    }

    static previous(): TodoNextStepAction {
        return new TodoNextStepAction(currentStep => OrderedTodoSteps.previous(currentStep));
    }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        const nextStep = this.nextStepSupplier(previousState.seedboxTodoSubState.currentStep);
        let nextButtonDisabled = true;
        switch (nextStep) {
            case TodoSteps.SELECT_TODO_FILES:
                nextButtonDisabled = previousState.seedboxTodoSubState.todoFiles.noneSelected()
                break;
            case TodoSteps.SELECT_TARGET:
                nextButtonDisabled = FileDirectoryDescriptor.isRoot(previousState.seedboxTodoSubState.currentTargetDirectory)
                break;
            case TodoSteps.RENAME_TODO_FILES:
                nextButtonDisabled = previousState.seedboxTodoSubState.filesToMove.some(aFileToMove => aFileToMove.newName === "")
                break;

        }
        return {
            ...previousState,
            seedboxTodoSubState: {
                ...previousState.seedboxTodoSubState,
                currentStep: nextStep,
                nextButtonDisabled: nextButtonDisabled,
                backButtonDisabled: nextStep === TodoSteps.SELECT_TODO_FILES
            }
        }
    }
}