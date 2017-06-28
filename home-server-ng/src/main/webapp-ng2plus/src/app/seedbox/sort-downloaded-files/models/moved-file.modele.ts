import { CompletedFile } from './completed-file.modele';


export class MovedFile {

    public sourceFile: CompletedFile;
    public finalPath: string;
    public success: boolean;

    public static fromRaw(rawJson: any): MovedFile {
        const returnValue = new MovedFile();
        returnValue.sourceFile = rawJson.sourceFile;
        returnValue.finalPath = rawJson.finalPath;
        returnValue.success = rawJson.success;

        return returnValue;
    }


}
