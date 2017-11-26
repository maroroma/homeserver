export class KioskDisplayOption {
    public displayWeather: boolean;
    public displayHour: boolean;
    public displayCurrentReading: boolean;
    public displayCurrentDownload: boolean;

    public static fromRaw(raw: any): KioskDisplayOption {
        const returnValue = new KioskDisplayOption();
        returnValue.displayWeather = raw.displayWeather;
        returnValue.displayHour = raw.displayHour;
        returnValue.displayCurrentReading = raw.displayCurrentReading;
        returnValue.displayCurrentDownload = raw.displayCurrentDownload;


        return returnValue;
    }
}