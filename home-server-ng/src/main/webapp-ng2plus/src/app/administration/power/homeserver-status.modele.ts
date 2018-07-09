import { Drive } from './drive.modele';
import { JsonTools } from 'app/shared/json-tools.service';
export class HomeServerStatus {
    public version: string;
    public startUpTime: Date;
    public hostName: string;
    public operatingSystem: string;
    public drives: Array<Drive>;
    public ipAddress: string;

    public static mapFromJson(rawValue: any) {
        const returnValue = new HomeServerStatus();

        returnValue.version = rawValue.version;
        returnValue.startUpTime = rawValue.startUpTime;
        returnValue.hostName = rawValue.hostName;
        returnValue.operatingSystem = rawValue.operatingSystem;
        returnValue.ipAddress = rawValue.ipAddress;

        returnValue.drives = JsonTools.map(rawValue.drives, Drive.fromRaw);


        return returnValue;
    }
}