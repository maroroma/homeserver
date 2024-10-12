export default class Drive {
    constructor(
        public name: string,
        public totalSpace: number,
        public freeSpace: number,
        public usedSpace: number,
        public percentageUsed: number
    ) { }
}