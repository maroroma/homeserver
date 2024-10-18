export default class RunningTorrent {
    constructor(
        public id: string,
        public name: string,
        public done: number,
        public remaining: number,
        public total: number,
        public estimatedTime: string,
        public percentDone: number,
        public completed: boolean
    ) { }
}