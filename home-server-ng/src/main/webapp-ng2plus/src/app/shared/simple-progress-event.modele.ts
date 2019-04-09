export class SimpleProgressEvent {

    public percentageDone: number;

    constructor(public done: number, public total: number, public isCompleted: boolean) {

        this.percentageDone = done / total * 100;

    }

    public static progress(done: number, total: number): SimpleProgressEvent {
        return new SimpleProgressEvent(done, total, false);
    }

    public static complete(done: number) {
        return new SimpleProgressEvent(done, done, true);
    }
}