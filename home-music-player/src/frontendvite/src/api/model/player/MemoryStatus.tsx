export class MemoryStatus {

    public static empty():MemoryStatus {
        return new MemoryStatus(0,0,0,0);
    }

    constructor(public heapSize: number, public heapMaxSize: number, public heapFreeSize: number, public percentageUsedMemory: number) { }
}