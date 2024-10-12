export default class Task {
    constructor(
        public id: string,
        public supplierType: "KODI" | "TORRENT",
        public title: string,
        public isRunning: boolean,
        public done: number,
        public remaining: number,
        public labelTotal: string,
        public labelDone: string,
        public labelRemaining: string
    ) {
    }

}