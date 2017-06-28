export class Step {
    public label: string;
    public isCurrent: boolean;
    public isCompleted: boolean;
    public isTodo: boolean;
    public index: number;

    public current(): void {
        this.isCurrent = true;
        this.isCompleted = false;
        this.isTodo = false;
    }
    public todo(): void {
        this.isCurrent = false;
        this.isCompleted = false;
        this.isTodo = true;
    }
    public completed(): void {
        this.isCurrent = false;
        this.isCompleted = true;
        this.isTodo = false;
    }
}