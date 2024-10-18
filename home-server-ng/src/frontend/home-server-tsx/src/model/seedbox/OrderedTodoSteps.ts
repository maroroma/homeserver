import {TodoSteps} from "./TodoSteps";

export default class OrderedTodoSteps {
    static readonly ORDERED_TODO_STEPS = [
        TodoSteps.SELECT_TODO_FILES,
        TodoSteps.SELECT_TARGET,
        TodoSteps.RENAME_TODO_FILES,
        TodoSteps.MOVE_SYNTHESIS
    ]

    static next(currentStep: TodoSteps): TodoSteps {
        let currentIndex = OrderedTodoSteps.ORDERED_TODO_STEPS.findIndex(aStep => aStep === currentStep);

        if (currentIndex + 1 < OrderedTodoSteps.ORDERED_TODO_STEPS.length) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }

        return OrderedTodoSteps.ORDERED_TODO_STEPS[currentIndex];
    }

    static previous(currentStep:TodoSteps) : TodoSteps {
        let currentIndex = OrderedTodoSteps.ORDERED_TODO_STEPS.findIndex(aStep => aStep === currentStep);

        if (currentIndex - 1  < 0) {
            currentIndex = 0;
        } else {
            currentIndex--;
        }

        return OrderedTodoSteps.ORDERED_TODO_STEPS[currentIndex];
    }
}