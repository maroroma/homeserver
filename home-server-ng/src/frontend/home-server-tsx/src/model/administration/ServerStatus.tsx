import Drive from "./Drive";

export default class ServerStatus {

    static empty(): ServerStatus {
        return new ServerStatus("", "", "", "", "", 0, []);
    }


    constructor(
        public version: string,
        public startUpTime	: string,
        public hostName: string,
        public ipAddress: string,
        public operatingSystem: string,
        public port: number,
        public drives: Drive[]
    ) { }
}