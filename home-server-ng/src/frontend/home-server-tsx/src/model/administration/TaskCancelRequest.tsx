import Task from "./Task";

export default class TaskCancelRequest {
    public supplierType: string;
    public taskId: string;
    constructor(task: Task) {
        this.supplierType = task.supplierType;
        this.taskId = task.id
    }
}